package com.example.qxapp.Fragment;

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

import com.example.qxapp.Adapter.MyStarContentAdapter;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentContent extends Fragment {
    private SwipeRefreshLayout starContentSwipeRefreshLayout;
    private RecyclerView starContentRecyclerView;
    private List<Post> data;
    private MyStarContentAdapter myStarContentAdapter;
    private TextView noStarContent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_starcontent,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //逻辑处理
        initView();
        //初始时刷新
        refresh();
        
        starContentSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);

        starContentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refresh() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("relation",user);
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                starContentSwipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    if(list.size()>0){
                        Log.i("tab", "1");
                        starContentSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        data = list;
                        myStarContentAdapter = new MyStarContentAdapter(getActivity(),data);
                        starContentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        starContentRecyclerView.setAdapter(myStarContentAdapter);
                    }else{
                        noStarContent.setVisibility(View.VISIBLE);
                        starContentSwipeRefreshLayout.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        starContentSwipeRefreshLayout = getActivity().findViewById(R.id.starContentSwipeRefreshLayout);
        starContentRecyclerView = getActivity().findViewById(R.id.starContentRecyclerView);
        noStarContent = getActivity().findViewById(R.id.noStarContent);
    }
}
