package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReceiveCommunity extends AppCompatActivity {
    private TextView receiveCommunityName,receiveCommunityInfo,receiveCommunityUser;
    private ImageView receiveCommunityBack,receiveCommunityStar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivecommunity);
        initView();
        initData();
        isStared();
        receiveCommunityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        receiveCommunityStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                BmobQuery<Community> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(id, new QueryListener<Community>() {
                    @Override
                    public void done(Community community, BmobException e) {
                        if(community.getIsrelated().equals("0")){
                            Intent intent = getIntent();
                            String id = intent.getStringExtra("id");
                            User user = BmobUser.getCurrentUser(User.class);
                            Community com = new Community();
                            com.setObjectId(id);
                            BmobRelation bmobRelation = new BmobRelation();
                            bmobRelation.add(user);
                            com.setIsrelated("1");
                            com.setRelation(bmobRelation);
                            com.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        receiveCommunityStar.setImageResource(R.drawable.shoucang_black);
                                        Toast.makeText(ReceiveCommunity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ReceiveCommunity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Intent intent = getIntent();
                            String id = intent.getStringExtra("id");
                            User user = BmobUser.getCurrentUser(User.class);
                            Community com = new Community();
                            com.setIsrelated("0");
                            com.setObjectId(id);
                            BmobRelation bmobRelation = new BmobRelation();
                            bmobRelation.remove(user);
                            com.setRelation(bmobRelation);
                            com.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        receiveCommunityStar.setImageResource(R.drawable.sc_normal);
                                        Toast.makeText(ReceiveCommunity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ReceiveCommunity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void isStared() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        BmobQuery<Community> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<Community>() {
            @Override
            public void done(Community community, BmobException e) {
                if(community.getIsrelated().equals("1")){
                    //已被收藏
                    receiveCommunityStar.setImageResource(R.drawable.shoucang_black);
                }else{

                }
            }
        });
    }

    private void initData() {
        Intent a = getIntent(); //communityName
        Intent b = getIntent(); //info
        Intent c = getIntent();//username
        String communityName = a.getStringExtra("communityName");
        String communityInfo = b.getStringExtra("communityInfo");
        String communityUser = c.getStringExtra("communityUser");
        receiveCommunityName.setText(communityName);
        receiveCommunityInfo.setText(communityInfo);
        receiveCommunityUser.setText(communityUser);
    }

    private void initView() {
        receiveCommunityName = findViewById(R.id.receiveCommunityName);
        receiveCommunityInfo = findViewById(R.id.receiveCommunityInfo);
        receiveCommunityUser = findViewById(R.id.receiveCommunityUser);
        receiveCommunityBack = findViewById(R.id.receiveCommunityBack);
        receiveCommunityStar = findViewById(R.id.receiveCommunityStar);
    }
}
