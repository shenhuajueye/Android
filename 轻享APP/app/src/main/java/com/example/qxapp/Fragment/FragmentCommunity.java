package com.example.qxapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.MyCommunityAdapter;
import com.example.qxapp.Adapter.MyStarCommunityAdapter;
import com.example.qxapp.Adapter.MyStarContentAdapter;
import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;
import com.example.qxapp.activity.MyCommunity;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentCommunity extends Fragment {
    private SwipeRefreshLayout starCommunitySwipeRefreshLayout;
    private RecyclerView starCommunityRecyclerView;
    private List<Community> data;
    private TextView noStarCommunity;
    private MyStarCommunityAdapter myStarCommunityAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_starcommunity,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //逻辑处理
        initView();
        //初始时刷新
        refresh();

        starCommunitySwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);

        starCommunitySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refresh() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Community> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("relation",user);
        bmobQuery.findObjects(new FindListener<Community>() {
            @Override
            public void done(List<Community> list, BmobException e) {
                starCommunitySwipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    if(list.size()>0){
                        Log.i("tab", "1");
                        starCommunitySwipeRefreshLayout.setVisibility(View.VISIBLE);
                        data = list;
                        myStarCommunityAdapter = new MyStarCommunityAdapter(getActivity(),data);
                        starCommunityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        starCommunityRecyclerView.setAdapter(myStarCommunityAdapter);
                    }else{
                        noStarCommunity.setVisibility(View.VISIBLE);
                        starCommunitySwipeRefreshLayout.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        starCommunitySwipeRefreshLayout = getActivity().findViewById(R.id.starCommunitySwipeRefreshLayout);
        starCommunityRecyclerView = getActivity().findViewById(R.id.starCommunityRecyclerView);
        noStarCommunity = getActivity().findViewById(R.id.noStarCommunity);
    }
}
