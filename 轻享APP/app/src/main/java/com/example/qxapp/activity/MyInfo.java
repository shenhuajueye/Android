package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MyInfo extends AppCompatActivity {
    private ImageView myInfoBack;
    private TextView myInfoUsername,contentNum,communityNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        myInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取个人信息
        getInfo();
    }
    //获取个人信息
    private void getInfo() {
        User user = BmobUser.getCurrentUser(User.class);
        String id = user.getObjectId();
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    myInfoUsername.setText(user.getUsername());
                }else{
                    Toast.makeText(MyInfo.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        myInfoBack = findViewById(R.id.myInfoBack);
        myInfoUsername = findViewById(R.id.myInfoUsername);
        contentNum = findViewById(R.id.contentNum);
        communityNum = findViewById(R.id.communityNum);
    }
}
