package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {
    private EditText registerUsername,registerPassword,registerNickname;
    private Button register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        registerNickname = findViewById(R.id.registerNickname);
        register = findViewById(R.id.register);
        //对注册按钮进行监听
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUsername(registerUsername.getText().toString().trim());
                user.setPassword(registerPassword.getText().toString().trim());
                user.setNickname(registerNickname.getText().toString().trim());
                if(registerUsername.getText().toString().equals("")){
                    Toast.makeText(Register.this, "用户名没有输入", Toast.LENGTH_SHORT).show();
                }else if(registerPassword.getText().toString().equals("")){
                    Toast.makeText(Register.this, "密码没有输入", Toast.LENGTH_SHORT).show();
                }else{
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
