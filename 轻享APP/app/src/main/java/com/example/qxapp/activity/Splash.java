package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //延时操作
        Timer timer = new Timer();
        timer.schedule(timerTask,2000);
        Bmob.initialize(this,"214236e258b7532dea4e74e059616c92");
    }
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
//            startActivity(new Intent(Splash.this, MainActivity.class));
            //如果已登录 跳转到主界面 没有 跳转至登录界面
            BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
            if(bmobUser!=null){
                startActivity(new Intent(Splash.this, MainActivity.class));
            }else{
                //没有登录
                startActivity(new Intent(Splash.this, Login.class));
            }
            finish();
        }
    };
}
