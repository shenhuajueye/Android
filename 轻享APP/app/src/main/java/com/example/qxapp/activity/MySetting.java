package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;

import cn.bmob.v3.BmobUser;

public class MySetting extends AppCompatActivity {
    private ImageView mySettingBack;
    private Button exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting);
        initView();

        mySettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                startActivity(new Intent(MySetting.this, Login.class));
                finish();
            }
        });

    }

    private void initView() {
        mySettingBack = findViewById(R.id.mySettingBack);
        exit = findViewById(R.id.exit);
    }
}
