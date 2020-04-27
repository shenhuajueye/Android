package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PushCommunity extends AppCompatActivity {
    private ImageView communityBack;
    private EditText communityName,communityInfo;
    private Button pushCommunity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushcommunity);
        initView();
        communityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pushCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(User.class);
                Community community = new Community();
                community.setName(communityName.getText().toString());
                community.setInfo(communityInfo.getText().toString());
                community.setIsrelated("0");
                community.setUser(user);
                community.setOwner(user.getUsername());
                community.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(PushCommunity.this, "创建成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(PushCommunity.this, "创建失败" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        communityBack = findViewById(R.id.communityBack);
        communityName = findViewById(R.id.communityName);
        communityInfo = findViewById(R.id.communityInfo);
        pushCommunity = findViewById(R.id.pushCommunity);
    }

}
