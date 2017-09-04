package com.disney4a.baymax_example.app.activity.baymax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxActivity;
import com.disney4a.baymax.utils.ViewSelector;
import com.disney4a.baymax_example.R;
import com.disney4a.baymax_example.app.adapter.CommonAdapter;
import com.disney4a.baymax_example.app.entity.DbEntity;
import com.disney4a.baymax_example.app.entity.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

@Tag_Activity(name = "dbTest")
public class DbTestActivity extends BaymaxActivity {
    @ViewSelector.GetViewById(R.id.userName)
    private EditText userName;

    @ViewSelector.GetViewById(R.id.users)
    private ListView users;

    private CommonAdapter<UserInfo> adapter;

    private UserInfo editUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_test_layout);
        adapter = new CommonAdapter<UserInfo>(this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = View.inflate(context, R.layout.db_userinfo_item_layout, null);
                    convertView.setTag(convertView.findViewById(R.id.userName));
                }
                TextView userName = (TextView) convertView.findViewById(R.id.userName);
                UserInfo userInfo = getItem(position);
                userName.setText(userInfo.getUserName());
                return convertView;
            }
        };
        users.setAdapter(adapter);
        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editUser = adapter.getItem(position);
                userName.setText(editUser.getUserName());
            }
        });
        users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo userInfo = adapter.getItem(position);
                userInfo.delete();
                editUser = null;
                userName.setText("");
                selectAll();
                return true;
            }
        });
        selectAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        users.setAdapter(null);
    }

    @ViewSelector.OnClick(ids = {R.id.commit})
    public void commit(View v) {
        if(editUser == null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userName.getText().toString());
            userInfo.save();
        }
        else {
            editUser.setUserName(userName.getText().toString());
            int result = editUser.update(editUser.getId());
            if(result == 1) {
                editUser = null;
            }
        }
        // Select All
        selectAll();
    }

    private void selectAll() {
        List<UserInfo> userInfos = DbEntity.findAll(UserInfo.class);
        adapter.setData(userInfos);
        adapter.notifyDataSetChanged();
    }
}
