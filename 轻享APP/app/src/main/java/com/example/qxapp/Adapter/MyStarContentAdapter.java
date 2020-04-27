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

import com.example.qxapp.Bean.Post;
import com.example.qxapp.R;
import com.example.qxapp.activity.Login;
import com.example.qxapp.activity.Receive;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyStarContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Post> data;
    private final int N_TYPE = 0;
    private final int F_TYPE = 1;
    private Boolean isFootView = true; //是否有footView
    private int maxNum = 15; //预加载的数据 一共15条
    public MyStarContentAdapter(Context context,List<Post> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mystar_content,viewGroup,false);
        View footView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foot_item,viewGroup,false);
        if(viewType == F_TYPE){
            return new MyStarContentAdapter.RecyclerViewHolder(footView,F_TYPE);
        }else {
            return new MyStarContentAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(isFootView&&getItemViewType(position)==F_TYPE){
            //底部加载提示
            final MyStarContentAdapter.RecyclerViewHolder recyclerViewHolder = (MyStarContentAdapter.RecyclerViewHolder) holder;
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
            //这是mystar_content的内容
            final MyStarContentAdapter.RecyclerViewHolder recyclerViewHolder = (MyStarContentAdapter.RecyclerViewHolder) holder;
            final Post post = data.get(position);
            recyclerViewHolder.username.setText(post.getName());
            recyclerViewHolder.nickname.setText(post.getNickname());
            recyclerViewHolder.content.setText(post.getContent());
            recyclerViewHolder.time.setText(post.getCreatedAt());
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerViewHolder.getAdapterPosition();
                    if(BmobUser.getCurrentUser(BmobUser.class) !=null){
                        Intent intent = new Intent(context, Receive.class);
                        intent.putExtra("username",post.getName());
                        intent.putExtra("content",post.getContent());
                        intent.putExtra("time",post.getCreatedAt());
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
        public TextView username,nickname,content,time; //mystar_content的TextView
        public TextView loading;
        public RecyclerViewHolder(View itemView, int view_type) {
            super(itemView);
            if(view_type==N_TYPE){
                username =itemView.findViewById(R.id.myStarContentUsername);
                nickname = itemView.findViewById(R.id.myStarContentNickname);
                content = itemView.findViewById(R.id.myStarContent);
                time = itemView.findViewById(R.id.myStarContentTime);
            }else if(view_type==F_TYPE){
                loading = itemView.findViewById(R.id.footText);
            }
        }
    }
}
