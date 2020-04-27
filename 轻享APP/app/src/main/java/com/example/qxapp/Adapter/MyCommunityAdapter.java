package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.Bean.Community;
import com.example.qxapp.Bean.Post;
import com.example.qxapp.R;
import com.example.qxapp.activity.Login;
import com.example.qxapp.activity.Receive;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyCommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Community> data;
    private final int N_TYPE = 0;
    private final int F_TYPE = 1;
    private Boolean isFootView = true; //是否有footView
    private int maxNum = 15; //预加载的数据 一共15条
    public MyCommunityAdapter(Context context,List<Community> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycommunity_item,viewGroup,false);
        View footView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foot_item,viewGroup,false);
        if(viewType == F_TYPE){
            return new MyCommunityAdapter.RecyclerViewHolder(footView,F_TYPE);
        }else {
            return new MyCommunityAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(isFootView&&getItemViewType(position)==F_TYPE){
            //底部加载提示
            final MyCommunityAdapter.RecyclerViewHolder recyclerViewHolder = (MyCommunityAdapter.RecyclerViewHolder) holder;
            recyclerViewHolder.loading.setText("加载中...");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    maxNum += 8;
                    notifyDataSetChanged();
                }
            },2000);
        }else{
            //这是ord_item的内容
            final MyCommunityAdapter.RecyclerViewHolder recyclerViewHolder = (MyCommunityAdapter.RecyclerViewHolder) holder;
            final Community community = data.get(position);
            recyclerViewHolder.username.setText(community.getName());
            recyclerViewHolder.communityName.setText(community.getName());
            recyclerViewHolder.communityInfo.setText(community.getInfo());
            recyclerViewHolder.username.setText(community.getOwner());
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerViewHolder.getAdapterPosition();
                    if(BmobUser.getCurrentUser(BmobUser.class) !=null){
                        //需要改动
                        Intent intent = new Intent(context, Receive.class);
                        intent.putExtra("communityName",community.getName());
                        intent.putExtra("communityInfo",community.getInfo());
                        intent.putExtra("username",community.getOwner());
                        intent.putExtra("id",data.get(position).getObjectId());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, Login.class));
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==maxNum-1){
            return F_TYPE;
        }
        return N_TYPE;
    }

    @Override
    public int getItemCount() {
        if(data.size()<maxNum){
            return data.size();
        }
        return maxNum;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView communityName,communityInfo,username; //community_item的TextView
        public TextView loading;
        public RecyclerViewHolder(View itemView, int view_type) {
            super(itemView);
            if(view_type==N_TYPE){
                communityName =itemView.findViewById(R.id.myCommuniytName);
                communityInfo = itemView.findViewById(R.id.myCommunityInfo);
                username = itemView.findViewById(R.id.myCommunityUserName);
            }else if(view_type==F_TYPE){
                loading = itemView.findViewById(R.id.footText);
            }
        }
    }
}
