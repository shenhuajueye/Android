package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;
import com.google.gson.internal.$Gson$Preconditions;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class Receive extends AppCompatActivity {
    private TextView username,content,time;
    private ImageView back,receiveStar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        initView();
        initData();
        isStared();
        //监听返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        receiveStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                BmobQuery<Post> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(id, new QueryListener<Post>() {
                    @Override
                    public void done(Post post, BmobException e) {
                        if(post.getIsrelated().equals("0")){
                            //未被收藏
                            Intent intent = getIntent();
                            String id = intent.getStringExtra("id");
                            User user = BmobUser.getCurrentUser(User.class);
                            Post po = new Post();
                            po.setIsrelated("1");
                            po.setObjectId(id);
                            BmobRelation bmobRelation = new BmobRelation();
                            bmobRelation.add(user);
                            po.setRelation(bmobRelation);
                            po.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        receiveStar.setImageResource(R.drawable.shoucang_black);
                                        Toast.makeText(Receive.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Receive.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Intent intent = getIntent();
                            String id = intent.getStringExtra("id");
                            User user = BmobUser.getCurrentUser(User.class);
                            Post po = new Post();
                            po.setIsrelated("0");
                            po.setObjectId(id);
                            BmobRelation bmobRelation = new BmobRelation();
                            bmobRelation.remove(user);
                            po.setRelation(bmobRelation);
                            po.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        receiveStar.setImageResource(R.drawable.sc_normal);
                                        Toast.makeText(Receive.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Receive.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
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
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<Post>() {
            @Override
            public void done(Post post, BmobException e) {
                if(post.getIsrelated().equals("1")){
                    //已被收藏
                    receiveStar.setImageResource(R.drawable.shoucang_black);
                }else{

                }
            }
        });
    }

    private void initData() {
        //第二种
        Intent a = getIntent(); //username
        Intent b = getIntent(); //content
        Intent c = getIntent();//time
        String receiveUsername = a.getStringExtra("username");
        String receiveContent = b.getStringExtra("content");
        String receiveTime = c.getStringExtra("time");
        username.setText(receiveUsername);
        content.setText(receiveContent);
        time.setText(receiveTime);


        //第一种
//        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");
//        Post post = new Post();
//        BmobQuery<Post> query = new BmobQuery<>();
//        query.getObject(id, new QueryListener<Post>() {
//            @Override
//            public void done(Post post, BmobException e) {
//                if(e==null){
//                    username.setText(post.getName());
//                    content.setText(post.getContent());
//                    time.setText(post.getCreatedAt());
//                }else{
//                    Toast.makeText(Receive.this, "获取失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void initView() {
        username = findViewById(R.id.receiveUsername);
        content = findViewById(R.id.receiveContent);
        time = findViewById(R.id.receiveTime);
        back = findViewById(R.id.back);
        receiveStar = findViewById(R.id.receiveStar);
    }
}
