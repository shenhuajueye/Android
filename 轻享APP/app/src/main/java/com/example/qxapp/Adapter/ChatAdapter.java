package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.Bean.Community;
import com.example.qxapp.R;
import com.example.qxapp.activity.Receive;
import com.example.qxapp.activity.ReceiveCommunity;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Community> communities;
    private final int N_TYPE = 0;
    private final int F_TYPE = 1;
    private Boolean isFootView = true; //是否有footView
    private int maxNum = 15; //预加载的数据 一共15条
    public ChatAdapter(Context context,List<Community> communities){
        this.context = context;
        this.communities = communities;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_item,viewGroup,false);
        View footView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foot_item,viewGroup,false);
        if(viewType == F_TYPE){
            return new ChatAdapter.RecyclerViewHolder(footView,F_TYPE);
        }else {
            return new ChatAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(isFootView&&getItemViewType(position)==F_TYPE){
            //底部加载提示
            final ChatAdapter.RecyclerViewHolder recyclerViewHolder = (ChatAdapter.RecyclerViewHolder) holder;
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
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            final Community community = communities.get(position);
            recyclerViewHolder.communityName.setText(community.getName());
            recyclerViewHolder.communityInfo.setText(community.getInfo());
            recyclerViewHolder.communityUser.setText(community.getOwner());
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerViewHolder.getAdapterPosition();
                    Intent intent = new Intent(context, ReceiveCommunity.class);
                    intent.putExtra("communityName",community.getName());
                    intent.putExtra("communityInfo",community.getInfo());
                    intent.putExtra("communityUser",community.getUser().getUsername());
                    intent.putExtra("id",communities.get(position).getObjectId());
                    context.startActivity(intent);
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
        if(communities.size()<maxNum){
            return communities.size();
        }
        return maxNum;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView communityName,communityInfo,communityUser; //community_item的TextView
        public TextView loading;
        public RecyclerViewHolder(View itemView, int view_type) {
            super(itemView);
            if(view_type==N_TYPE){
                communityName =itemView.findViewById(R.id.communityName);
                communityInfo = itemView.findViewById(R.id.communityInfo);
                communityUser = itemView.findViewById(R.id.communityUser);
            }else if(view_type==F_TYPE){
                loading = itemView.findViewById(R.id.footText);
            }
        }
    }
}
