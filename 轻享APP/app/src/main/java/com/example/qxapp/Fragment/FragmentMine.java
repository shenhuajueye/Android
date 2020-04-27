package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qxapp.Bean.User;
import com.example.qxapp.R;
import com.example.qxapp.activity.Login;
import com.example.qxapp.activity.MyCommunity;
import com.example.qxapp.activity.MyContent;
import com.example.qxapp.activity.MyInfo;
import com.example.qxapp.activity.MySetting;
import com.example.qxapp.activity.MyStar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FragmentMine extends Fragment {
    private TextView username;
    private LinearLayout myInfo,mineContent,mineCommunity,mineStar,mineSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        //加载个人信息
        getMyinfo();

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至我的信息界面
                startActivity(new Intent(getActivity(), MyInfo.class));
            }
        });

        mineContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyContent.class));
            }
        });

        mineCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCommunity.class));
            }
        });

        mineStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyStar.class));
            }
        });
        mineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MySetting.class));
            }
        });

    }

    private void getMyinfo() {
        BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
        String id = bmobUser.getObjectId();
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    username.setText(user.getUsername());
                }else{
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        username = getActivity().findViewById(R.id.mineUsername);
        myInfo = getActivity().findViewById(R.id.myInfo);
        mineContent = getActivity().findViewById(R.id.mineContent);
        mineCommunity = getActivity().findViewById(R.id.mineCommunity);
        mineStar = getActivity().findViewById(R.id.mineStar);
        mineSetting = getActivity().findViewById(R.id.mineSetting);
    }
}
