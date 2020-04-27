package com.example.qxapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.MyCommunityAdapter;
import com.example.qxapp.Adapter.MyContentAdapter;
import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.Bean.User;
import com.example.qxapp.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyCommunity extends AppCompatActivity {
    private SwipeMenuRecyclerView myCommunitySwipeMenuRecyclerView;
    private TextView noCommunity;
    private SwipeRefreshLayout myCommunitySwipeRefreshLayout;
    private List<Community> data;
    private MyCommunityAdapter myCommunityAdapter;
    private ImageView myCommunityBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycommunity);
        //初始化Bmob
        Bmob.initialize(MyCommunity.this,"214236e258b7532dea4e74e059616c92");

        initView();

        //初始时刷新
        refresh();
        myCommunitySwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);

        myCommunitySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        myCommunityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void refresh() {
        //获取我发布的帖子
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Community> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("user",user);
        bmobQuery.findObjects(new FindListener<Community>() {
            @Override
            public void done(List<Community> list, BmobException e) {
                myCommunitySwipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    data = list;
                    if(data.size()>0){
                        myCommunitySwipeRefreshLayout.setVisibility(View.VISIBLE);
                        myCommunitySwipeMenuRecyclerView.addItemDecoration(new DefaultItemDecoration(Color.RED));
                        myCommunitySwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
                        myCommunitySwipeMenuRecyclerView.setSwipeMenuItemClickListener(swipeItemClickListen);
                        myCommunityAdapter = new MyCommunityAdapter(MyCommunity.this,data);
                        myCommunitySwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(MyCommunity.this));
                        myCommunitySwipeMenuRecyclerView.setAdapter(myCommunityAdapter);

                    }else{
                        noCommunity.setVisibility(View.VISIBLE);
                    }
                }else{
                    myCommunitySwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MyCommunity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //设置菜单监听器
    SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator(){
        //创建菜单
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyCommunity.this)
                    .setTextColor(Color.WHITE)
                    .setBackgroundColor(Color.RED)
                    .setText("删除")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    //菜单点击监听
    SwipeMenuItemClickListener swipeItemClickListen = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            //任何操作必须先关闭菜单，否则可能出现Item菜单状态错乱
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); //右边还是左边菜单
            final int adapterPosition = menuBridge.getAdapterPosition();//RecycleView的Item的position
            int position = menuBridge.getPosition(); //菜单在RecycleView的Item中的position
            if(direction==SwipeMenuRecyclerView.RIGHT_DIRECTION){
                Community community = new Community();
                community.setObjectId(data.get(adapterPosition).getObjectId());
                community.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            data.remove(adapterPosition);//删除item
                            myCommunityAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(MyCommunity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private void initView() {
        myCommunitySwipeMenuRecyclerView = findViewById(R.id.myCommunitySwipeMenuRecyclerView);
        noCommunity =findViewById(R.id.noCommunity);
        myCommunitySwipeRefreshLayout = findViewById(R.id.myCommunitySwipeRefreshLayout);
        myCommunityBack = findViewById(R.id.myCommunityBack);
    }
}
