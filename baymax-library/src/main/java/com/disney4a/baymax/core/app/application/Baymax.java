package com.disney4a.baymax.core.app.application;

import android.app.Activity;
import android.app.Service;
import android.content.ContentProvider;
import android.content.Context;
import android.util.Log;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.annotations.Tag_Controller;
import com.disney4a.baymax.annotations.Tag_Http;
import com.disney4a.baymax.annotations.Tag_Provider;
import com.disney4a.baymax.annotations.Tag_Return;
import com.disney4a.baymax.annotations.Tag_Service;
import com.disney4a.baymax.core.attr.Correspondents;
import com.disney4a.baymax.core.packaging.ActivityPackaging;
import com.disney4a.baymax.core.packaging.ControllerPackaging;
import com.disney4a.baymax.core.packaging.ProviderPackaging;
import com.disney4a.baymax.core.packaging.ServicePackaging;
import com.disney4a.baymax.utils.ClassScanner;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tjy on 2017/8/15 0015.
 *
 * @see #activity(String)
 * @see #service(String)
 * @see #provider(String)
 * @see #controller(String)
 *
 * 重点关注该类，该类的全局使用的工具类。
 */

public class Baymax {

    public static final class IntentConfig {
        /** 原生标记，使用该标记后走系统原生鉴权方法，也就不再免注册 **/
        public static final String NATIVE_FLAG = "native_flag";

    }

    private HashMap<String, ActivityPackaging> activityPackagingHashMap = new HashMap<>();
    private HashMap<String, ServicePackaging> servicePackagingHashMap = new HashMap<>();
    private HashMap<String, ProviderPackaging> providerPackagingHashMap = new HashMap<>();
    private HashMap<String, ControllerPackaging> controllerPackagingHashMap = new HashMap<>();
    private Context context;
    private Baymax() {}

    /**
     * 是否执行过Hook
     */
    private boolean hooked;

    /**
     * 已经被执行过注解解析
     */
    private boolean played;

    /**
     * Hook 工具
     */
    private SysHook mSysHook;

    /**
     * Hook嵌入
     * @param proxyActivity
     * @param proxyService
     */
    final Baymax hook(Class<? extends Activity> proxyActivity, Class<? extends Service> proxyService) {
        if(!hooked) {
            // Hook
            try {
                mSysHook = new SysHook(context, proxyActivity, proxyService);
                mSysHook.hookAms();
                mSysHook.hookSystemHandler();
                hooked = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 初始化
     * @param context
     */
    final static Baymax initialize(Context context) {
        singleInstance = new Baymax();
        singleInstance.context = context;
        return singleInstance;
    }

    /**
     * 销毁
     */
    public static void shutdown() {
        if(singleInstance != null) {
            singleInstance.context = null;
            singleInstance = null;
        }
    }

    /**
     * 開始
     */
    public final void play() {
        // 是否重复执行play()方法
        if(played)  {
            throw new RuntimeException("You can't play multiple times!");
        }
        // 是否执hook成功
        if(!hooked) {
            throw new RuntimeException("You must be hooked!");
        }
        // 没有指定注解所在包
        if(annotationsPackages == null) {
            throw new RuntimeException("AnnotationsPackages cannot be empty!");
        }

        List<Class<?>> all = new ArrayList<>();
        for(String annotationsPackage : annotationsPackages) {
            List<Class<?>> packageClasses = ClassScanner.getAllClasses(annotationsPackage);
            all.addAll(packageClasses);
        }

        List<Class<?>> systemClasses = ClassScanner.getAllClasses("com.disney4a.baymax.core.app");
        all.addAll(systemClasses);
        for(Class<?> clz :all) {
            Annotation[] classAnnotations = clz.getAnnotations();
            for(Annotation annotation : classAnnotations) {
                if(annotation.annotationType() == Tag_Activity.class) {
                    Tag_Activity activity = (Tag_Activity) annotation;
                    String name = activity.name();
                    Class<? extends Activity> className = (Class<? extends Activity>) clz;
                    ActivityPackaging activityPackaging = new ActivityPackaging(name, className);
                    activityPackagingHashMap.put(activityPackaging.getName(), activityPackaging);
                }
                else if(annotation.annotationType() == Tag_Service.class) {
                    Tag_Service service = (Tag_Service) annotation;
                    String name = service.name();
                    Class<? extends Service> className = (Class<? extends Service>) clz;
                    ServicePackaging servicePackaging = new ServicePackaging(name, className);
                    servicePackagingHashMap.put(servicePackaging.getName(), servicePackaging);
                }
                else if(annotation.annotationType() == Tag_Provider.class) {
                    Tag_Provider provider = (Tag_Provider) annotation;
                    String name = provider.name();
                    Class<? extends ContentProvider> className = (Class<? extends ContentProvider>) clz;
                    ProviderPackaging providerPackaging = new ProviderPackaging(name, className);
                    providerPackagingHashMap.put(providerPackaging.getName(), providerPackaging);
                }
                else if(annotation.annotationType() == Tag_Controller.class) {
                    Tag_Controller controller = (Tag_Controller) annotation;
                    String uri = controller.uri();
                    System.out.println(controller+" -> uri："+uri);

                    Object obj = null;
                    try {
                        obj = clz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // 解析方法
                    Method[] allMethod = clz.getDeclaredMethods();
                    int i = 0;
                    for(; i < allMethod.length; i++) {
                        Method method = allMethod[i];
                        Tag_Controller.Path path = method.getAnnotation(Tag_Controller.Path.class);
                        if(path != null) {
                            String value = path.value();
                            ControllerPackaging controllerPackaging = new ControllerPackaging(uri, value);
                            controllerPackaging.setName(path.name());
                            controllerPackaging.setFromClass(clz);
                            controllerPackaging.setObj(obj);
                            controllerPackaging.setMethod(method);
                            controllerPackaging.setTag_http(method.getAnnotation(Tag_Http.class));
                            controllerPackaging.setTag_return(method.getAnnotation(Tag_Return.class));
                            controllerPackagingHashMap.put(controllerPackaging.getName(), controllerPackaging);
                        }
                    }
                }
            }
        }
        Log.i("DEBUG", this+"");
        played = true;
    }

    /**
     * 执行方法
     * @param pathName 注解Path的name属性
     * @param args 执行该方法所需要的参数（除自动生成的参数外）
     * @param <T> 返回值类型
     * @return
     */
    public <T> T execute(String pathName, Object ... args) {
        Object result;
        ControllerPackaging controllerPackaging;
        try {
            controllerPackaging = controllerPackagingHashMap.get(pathName);
            Class<?>[] needArgs = controllerPackaging.getMethod().getParameterTypes();
            onBeforeRequest(controllerPackaging);
            if(needArgs.length > 0 && needArgs[0].isAssignableFrom(Correspondents.class)) {
                Object[] realArgs = createMethodArgs(controllerPackaging, args);
                result = controllerPackaging.getMethod().invoke(controllerPackaging.getObj(), realArgs);
            }
            else {
                result = controllerPackaging.getMethod().invoke(controllerPackaging.getObj(), args);
            }
            onAfterRequest(controllerPackaging);
        } catch (Exception e) {
            result = "error";
            jump2(result);
            return (T) result;
        }
        // 根据注解类型执行处理结果
        if(controllerPackaging.getTag_return() != null) {
            switch (controllerPackaging.getTag_return().type()) {
                case Tag_Return.REDIRECT: {
                    if(!jump2(result)) {
                        throw new UnsupportedOperationException("You can't find Activity or Service it by name '"+result+"'");
                    }
                    break;
                }
                case Tag_Return.JSON_FORMAT: {
                    result = new Gson().fromJson(result.toString(), controllerPackaging.getTag_return().toClass());
                    break;
                }
            }
        }
        return (T) result;
    }

    private boolean jump2(Object result) {
        if (activityPackagingHashMap.containsKey(result)) {
            ActivityPackaging activityPackaging = activityPackagingHashMap.get(result);
            activityPackaging.start();
            return true;
        } else if (servicePackagingHashMap.containsKey(result)) {
            ServicePackaging servicePackaging = servicePackagingHashMap.get(result);
            servicePackaging.start();
            return true;
        }
        return false;
    }

    Object[] createMethodArgs(ControllerPackaging controllerPackaging, Object[] args) {
        Object[] realArgs = new Object[args.length + 1];
        Correspondents correspondents = null;
        try {
            correspondents = (Correspondents) controllerPackaging.getMethod().getParameterTypes()[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        correspondents.setUri(controllerPackaging.getFullPath());
        realArgs[0] = correspondents;
        System.arraycopy(args, 0, realArgs, 1, args.length);
        if(controllerPackaging.getTag_http() != null){
            correspondents.setPackaging(controllerPackaging);
        }
        return realArgs;
    }

    protected void onBeforeRequest(ControllerPackaging controllerPackaging) {
        mSysHook.update_all_aop_onBeforeRequest("controller->"+controllerPackaging.getMethod().getName(), controllerPackaging.getMethod(), controllerPackaging);};

    protected void onAfterRequest(ControllerPackaging controllerPackaging) {
        mSysHook.update_all_aop_onAfterRequest("controller->"+controllerPackaging.getMethod().getName(), controllerPackaging.getMethod(), controllerPackaging);}

    /**
     * 注解所在的包名
     */
    private String[] annotationsPackages;

    public Baymax setAnnotationsPackage(String ... annotationsPackages) {
        this.annotationsPackages = annotationsPackages;
        return this;
    }

    public SysHook getSysHook() {
        return mSysHook;
    }

    /**
     * 获得Activity
     * @param name
     * @return
     */
    public ActivityPackaging activity(String name) {
        return activityPackagingHashMap.get(name);
    }

    /**
     * 获得Service
     * @param name
     * @return
     */
    public ServicePackaging service(String name) {
        return servicePackagingHashMap.get(name);
    }

    /**
     * 获取ContentPrivate
     * @param name
     * @return
     */
    public ProviderPackaging provider(String name) {
        return providerPackagingHashMap.get(name);
    }

    final HashMap<String, ActivityPackaging> getActivityPackagingHashMap() {
        return activityPackagingHashMap;
    }

    final HashMap<String, ServicePackaging> getServicePackagingHashMap() {
        return servicePackagingHashMap;
    }

    final HashMap<String, ProviderPackaging> getProviderPackagingHashMap() {
        return providerPackagingHashMap;
    }

    final HashMap<String, ControllerPackaging> getControllerPackagingHashMap() {
        return controllerPackagingHashMap;
    }

    /**
     * 获得Controller
     * @param name
     * @return
     */
    public ControllerPackaging controller(String name) {
        return controllerPackagingHashMap.get(name);
    }

    public Context getContext() {
        return context;
    }

    // 单例
    private static Baymax singleInstance;
    public static Baymax single() {
        if(singleInstance == null) {
            throw new UnsupportedOperationException("You have to initialize it!");
        }
        return singleInstance;
    }
}
