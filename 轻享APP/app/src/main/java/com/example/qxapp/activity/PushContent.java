package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PushContent extends AppCompatActivity {
    private EditText contentInfo;
    private ImageView contentBack;
    private Button pushContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushcontent);
        initView();
        pushContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentInfo.getText().toString().isEmpty()){
                    Toast.makeText(PushContent.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }else{
                    User user = BmobUser.getCurrentUser(User.class);
                    Post post = new Post();
                    post.setName(user.getUsername());
                    post.setContent(contentInfo.getText().toString());
                    post.setIsrelated("0");
                    post.setAuthor(user);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                contentInfo.setText("");
                                Toast.makeText(PushContent.this, "发送成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(PushContent.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        contentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        contentInfo = findViewById(R.id.contentInfo);
        pushContent = findViewById(R.id.pushContent);
        contentBack = findViewById(R.id.contentBack);
    }
}
