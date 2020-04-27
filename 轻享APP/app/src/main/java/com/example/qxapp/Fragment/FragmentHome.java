package com.example.qxapp.Fragment;

import android.os.Bundle;
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

import com.example.qxapp.Adapter.HomeAdapter;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView helloHome,username,welcome;
    private List<Post> data;
    private HomeAdapter homeAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //逻辑处理
        initView();

        Bmob.initialize(getActivity(),"214236e258b7532dea4e74e059616c92");
        //初始时刷新
        refresh();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                refresh();
            }
        });
        //user加载  XX欢迎您
        BmobUser bmobUser = BmobUser.getCurrentUser(User.class);
        String userId = bmobUser.getObjectId();
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    username.setText(user.getUsername());
                }else{
                    username.setText(" ");
                    welcome.setText(" ");
                }
            }
        });

    }

    private void refresh() {
        BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.setLimit(1000);
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    data = list;
                    homeAdapter = new HomeAdapter(getActivity(),data);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(homeAdapter);
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        recyclerView = getActivity().findViewById(R.id.recyclerView);
        swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        helloHome = getActivity().findViewById(R.id.helloHome);
        username = getActivity().findViewById(R.id.user);
        welcome = getActivity().findViewById(R.id.welcome);
    }
}
