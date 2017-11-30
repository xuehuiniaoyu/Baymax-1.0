package com.disney4a.baymax.core.app.application;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.disney4a.baymax.core.packaging.ProviderPackaging;
import com.disney4a.baymax.utils.Reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tjy on 2017/8/15 0015.
 * 你不必关心，该类你只需要关注aop接口。
 * <p>
 * Hook切入点工具，通过Android Hook技术实现Activity或Service免注册启动。
 * 主要实现原理为：伪装
 * 通过伪装把非清单注册的Activity/Service伪装成注册过的Activity/Service去通过检测。
 * 一旦通过检测在创建实例对象之前卸下伪装创建真实对象，这样就能够实现免注册启动。
 * 这样的技术以前用在热更新框架中，比如360的DroidPlugin的fuck技术！
 * <p>
 * 在Little中也同样使用，但是目的不在热更新，而在免注册。通过注解的方式实现一个MVC模型。
 * 当然它也可以实现热更新（如果你有兴趣可以尝试扩展）。
 * <p>
 * 然后我还希望能够提供一些面向切面的接口出来。
 */

public final class SysHook {

    public static final String TAG = SysHook.class.getSimpleName();

    private Context context;

    Object activityManager;
    Object activityThread;

    /**
     * ServiceInfo代理对象，用于保存ServiceInfo对象
     */
    class ServiceInfoProxy {
        Object info;
        Object applicationInfo;
        Object compatInfo;
        Object packageInfo;
    }

    // 保存Service的ServiceInfo对象
    final HashMap<IBinder, ServiceInfoProxy> mServiceInfos = new HashMap<>();

    // 切面监听结合
    final List<AOP_Listener> m_aopListeners = new ArrayList<>();

    // 反射工具
    private Reflect reflect;

    /**
     * 监听接口
     */
    public interface AOP_Listener {
        void aop_onBeforeRequestIntercept(String name, Method method, Object[] args);

        void aop_onAfterRequestIntercept(String name, Method method, Object[] args);

        void aop_onBeforeHandleMessageIntercept(Message msg);

        void aop_onAfterHandleMessageIntercept(Message msg);
    }

    /**
     * 监听接口对象
     */
    public static class SimpleAOP_Listener implements AOP_Listener {
        @Override
        public void aop_onBeforeRequestIntercept(String name, Method method, Object[] args) {

        }

        @Override
        public void aop_onAfterRequestIntercept(String name, Method method, Object[] args) {

        }

        @Override
        public void aop_onBeforeHandleMessageIntercept(Message msg) {

        }

        @Override
        public void aop_onAfterHandleMessageIntercept(Message msg) {

        }
    }

    SysHook(Context context, Class<? extends Activity> proxyActivity, Class<? extends Service> proxyService) {
        this.context = context;
        mappings.put("startActivity", proxyActivity);
        mappings.put("startService", proxyService);
        mappings.put("stopService", proxyService);
        reflect = Reflect.newInstance();
    }

    private HashMap<String, Class<?>> mappings = new HashMap<>();

    void hookAms() throws ClassNotFoundException {
        Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        activityManager = reflect.clear().on(activityManagerNativeClass).get("gDefault");
//        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Object iActivityManagerObject = reflect.clear().on(activityManager).get("mInstance");
        Class<?> iActivityManagerIntercept = Class.forName("android.app.IActivityManager");

        class AmsInvocationHandler implements InvocationHandler {
            private Object iActivityManagerObject;
            /**
             * 系统中可以被使用的ProviderInfo对象，一般系统启动后都会调用一些ContentProvider
             * 我们通过拦截获取这个ProviderInfo对象作为载体，将来在这个载体上复制出新的ProviderInfo
             */
            private ProviderInfo availableProviderInfo;

            public AmsInvocationHandler(Object iActivityManagerObject) {
                this.iActivityManagerObject = iActivityManagerObject;
            }

            Intent dispatchIntercept(String methodName, Object[] args) {
                Intent intent = null;
                int index = -1;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        intent = (Intent) args[i];
                        index = i;
                        break;
                    }
                }
                String className = null;
                if (index != -1) {
                    if(intent.getComponent() != null) {
                        // 检查是否为系统内部类
                        className = intent.getComponent().getClassName();
                        Log.i(TAG, "class identification:" + className);
                    }
                    else {
                        Log.i(TAG, "intent="+intent);
                    }
                }
                if(className != null) {
                    try {
                        Class.forName(className); // 验证
                        Intent proxyIntent = new Intent();
                        ComponentName componentName = new ComponentName(SysHook.this.context.getPackageName(), mappings.get(methodName).getName());
                        proxyIntent.setComponent(componentName);
                        proxyIntent.putExtra("bindIntent", intent);
                        args[index] = proxyIntent;
                    } catch (ClassNotFoundException e) {
                        // 不能加载
                        Log.e(TAG, className + " 不是进程内部类");
                    }
                }
                return intent;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().contains("startActivity")) {
                    dispatchIntercept("startActivity", args);
                } else if (method.getName().contains("startService")) {
                    dispatchIntercept("startService", args);
                } else if (method.getName().contains("stopService")) {
                    dispatchIntercept("stopService", args);
                }
                Object result;
                update_all_aop_onBeforeRequest("sys->" + method.getName(), method, args);
                result = method.invoke(iActivityManagerObject, args);
                if (method.getName().contains("getContentProvider")) {
                    if (result != null && availableProviderInfo == null) {
                        availableProviderInfo = reflect.clear().on(result).get("info");
                        Object mPackageInfo = reflect.clear().on(context).get("mPackageInfo");
                        ApplicationInfo mApplicationInfo = reflect.clear().on(mPackageInfo).get("mApplicationInfo");
                        // 安装Provider
                        Collection<ProviderPackaging> c = Baymax.single().getProviderPackagingHashMap().values();
                        List<ProviderInfo> providerInfos = new ArrayList<>();
                        for (ProviderPackaging packaging : c) {
                            ProviderInfo providerInfo = new ProviderInfo(availableProviderInfo);
                            providerInfo.authority = packaging.getName();
                            providerInfo.name = packaging.getProvider().getName();
                            providerInfo.applicationInfo = new ApplicationInfo(mApplicationInfo);
                            providerInfos.add(providerInfo);
                        }
                        reflect.clear().on(activityThread).method("installContentProviders", Context.class, List.class).invoke(context, providerInfos);
                        Log.i("INFO", "installed provider count " + c.size());
                    }
                }
                update_all_aop_onAfterRequest("sys->" + method.getName(), method, args);
                return result;
            }
        }
        AmsInvocationHandler amsInvocationHandler = new AmsInvocationHandler(iActivityManagerObject);
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{iActivityManagerIntercept}, amsInvocationHandler);
        reflect.clear().on(activityManager).set("mInstance", proxy);
    }

    /**
     * 所有的拦截都会经过handleMessage接收并处理。
     */
    private final class Callback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            update_all_aop_onBeforeHandleMessage(msg);
            switch (msg.what) {
                case 100: // LAUNCH_ACTIVITY
                    handleLaunchActivity(msg);
                    break;
                case 114: // CREATE_SERVICE
                    IBinder token = reflect.clear().on(msg.obj).get("token");
                    Object info = reflect.clear().on(msg.obj).get("info");
                    Object applicationInfo = reflect.clear().on(info).get("applicationInfo");
                    Object compatInfo = reflect.clear().on(msg.obj).get("compatInfo");
                    Object packageInfo = null;
                    try {
                        packageInfo = reflect.clear().on(activityThread).method("getPackageInfoNoCheck"
                                , ApplicationInfo.class, Class.forName("android.content.res.CompatibilityInfo"))
                                .invoke(applicationInfo, compatInfo);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    ServiceInfoProxy serviceInfoProxy = new ServiceInfoProxy();
                    serviceInfoProxy.info = info;
                    serviceInfoProxy.applicationInfo = applicationInfo;
                    serviceInfoProxy.compatInfo = compatInfo;
                    serviceInfoProxy.packageInfo = packageInfo;
                    mServiceInfos.put(token, serviceInfoProxy);
                    break;
                case 115: // SERVICE_ARGS
                    try { 
						handleLaunchService(msg);
					} catch(Exception e) {
					
					}
                    break;
                case 116: // STOP_SERVICE
                    mServiceInfos.remove(msg.obj);
                    break;
                case 119: // CLEAN_UP_CONTEXT
                    break;
            }
            Handler mH = reflect.clear().on(activityThread).get("mH");
            mH.handleMessage(msg);
            update_all_aop_onAfterHandleMessage(msg);
            return true;
        }

        void handleLaunchActivity(Message msg) {
            Intent proxyIntent = reflect.clear().on(msg.obj).get("intent");
            Intent realIntent = proxyIntent.getParcelableExtra("bindIntent");
            if (realIntent != null) {
//                reflect.clear().on(proxyIntent.getComponent()).set("mClass", realIntent.getComponent().getClassName());
                reflect.clear().on(msg.obj).set("intent", realIntent);
                proxyIntent.removeExtra("bindIntent");
                realIntent.putExtra("bindIntent", proxyIntent);
            }
        }

        void handleLaunchService(Message msg) {
            IBinder token = reflect.clear().on(msg.obj).get("token");
            Intent proxyIntent = reflect.clear().on(msg.obj).get("args");
            Intent realIntent = proxyIntent.getParcelableExtra("bindIntent");
            if (realIntent != null) {
                reflect.clear().on(msg.obj).set("args", proxyIntent = realIntent);
                ServiceInfoProxy serviceInfoProxy = mServiceInfos.get(token);
                String name = realIntent.getComponent().getClassName();
//                    Object applicationInfo = serviceInfoProxy.applicationInfo;
//                    Object compatInfo = serviceInfoProxy.compatInfo;
                Object packageInfo = serviceInfoProxy.packageInfo;
                reflect.clear().on(serviceInfoProxy.info).set("name", name);
//                reflect.clear().on(proxyIntent.getComponent()).set("mClass", name);
//                proxyIntent.putExtras(realIntent.getExtras());
                Map<IBinder, Service> mService = reflect.clear().on(activityThread).get("mServices");
                Service service = mService.get(token);
                Object context = reflect.on(service).get("mBase");
                try {
                    Class<?> realServiceClass = Class.forName(proxyIntent.getComponent().getClassName());
                    if (service != null && service.getClass() != realServiceClass) {
                        // 创建服务
                        service = (Service) realServiceClass.newInstance();
//                            Object context = reflect.clear().on(Class.forName("android.app.ContextImpl")).method("createAppContext"
//                                    , Class.forName("android.app.ActivityThread"), Class.forName("android.app.LoadedApk"))
//                                    .invoke(activityThread, packageInfo);
                        reflect.clear().on(context).method("setOuterContext", Context.class).invoke(service);
                        Object mInstrumentation = reflect.clear().on(activityThread).get("mInstrumentation");
                        Application app = reflect.clear().on(packageInfo).method("makeApplication", boolean.class, Instrumentation.class).invoke(false, mInstrumentation);
                        reflect.clear().on(service).method("attach", Context.class, Class.forName("android.app.ActivityThread"), String.class, IBinder.class, Application.class, Object.class)
                                .invoke(context, activityThread, name, token, app, activityManager);
                        service.onCreate();
                        mService.put(token, service);
//                            System.out.println("base1="+reflect.clear().on(service).get("mBase"));
//                            reflect.clear().on(service).set("mBase", context);
//                            System.out.println("base2="+reflect.clear().on(service).get("mBase"));
//                            reflect.clear().on(service).set("mPackageInfo", packageInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void hookSystemHandler() throws ClassNotFoundException {
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        activityThread = reflect.clear().on(activityThreadClass).method("currentActivityThread").invoke();
        Handler mH = reflect.clear().on(activityThread).get("mH");
        reflect.clear().on(mH).set("mCallback", new Callback());
    }

    /**
     * 插入aop监听
     *
     * @param listener
     */
    public final synchronized void inset_aop(AOP_Listener listener) {
        if (m_aopListeners.contains(listener))
            throw new UnsupportedOperationException(listener + " has been registered!");
        m_aopListeners.add(listener);
    }

    /**
     * 卸載aop监听
     *
     * @param listener
     */
    public final synchronized void pluck_aop(AOP_Listener listener) {
        if (!m_aopListeners.contains(listener))
            throw new UnsupportedOperationException(listener + " has been unregistered?");
        m_aopListeners.remove(listener);
    }

    synchronized void update_all_aop_onBeforeRequest(String name, Method method, Object... args) {
        for (AOP_Listener listener : m_aopListeners) {
            listener.aop_onBeforeRequestIntercept(name, method, args);
        }
    }

    synchronized void update_all_aop_onAfterRequest(String name, Method method, Object... args) {
        for (AOP_Listener listener : m_aopListeners) {
            listener.aop_onAfterRequestIntercept(name, method, args);
        }
    }

    synchronized void update_all_aop_onBeforeHandleMessage(Message message) {
        for (AOP_Listener listener : m_aopListeners) {
            listener.aop_onBeforeHandleMessageIntercept(message);
        }
    }

    synchronized void update_all_aop_onAfterHandleMessage(Message message) {
        for (AOP_Listener listener : m_aopListeners) {
            listener.aop_onAfterHandleMessageIntercept(message);
        }
    }
}
