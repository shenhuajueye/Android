package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.ChatAdapter;
import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.R;
import com.example.qxapp.activity.PushCommunity;
import com.example.qxapp.activity.PushContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentChat extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton addIcon,addContent,addCommunity;
    private PopupWindow popupWindow;
    private List<Community> data;
    private ChatAdapter chatAdapter;
    private RelativeLayout relativeLayout;
    private View view;
    //论坛界面
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.pop_item,null);
        initView();
        //初始刷新一次
        refresh();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                refresh();
            }
        });
        //对floatingActionButton进行监听
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(view, 250,700,true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER,400,300);
            }
        });
        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PushContent.class));
            }
        });
        addCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PushCommunity.class));
            }
        });

    }

    private void refresh() {
        BmobQuery<Community> communityBmobQuery = new BmobQuery<>();
        communityBmobQuery.setLimit(1000);
        communityBmobQuery.order("-createdAt");
        communityBmobQuery.findObjects(new FindListener<Community>() {
            @Override
            public void done(List<Community> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    data = list;
                    chatAdapter = new ChatAdapter(getActivity(),data);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //将布局解析出来
                    recyclerView.setAdapter(chatAdapter);
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        recyclerView = getActivity().findViewById(R.id.chatRecyclerView);
        swipeRefreshLayout = getActivity().findViewById(R.id.chatSwipeRefreshLayout);
        addIcon = getActivity().findViewById(R.id.addIcon);
        addContent = view.findViewById(R.id.addContent);
        addCommunity = view.findViewById(R.id.addCommunity);
        relativeLayout = getActivity().findViewById(R.id.relativeLayout);
    }
}
