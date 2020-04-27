package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.qxapp.Fragment.FragmentCommunity;
import com.example.qxapp.Fragment.FragmentContent;
import com.example.qxapp.R;
import com.het.smarttab.SmartTabLayout;
import com.het.smarttab.v4.FragmentPagerItem;
import com.het.smarttab.v4.FragmentPagerItems;
import com.het.smarttab.v4.FragmentStatePagerItemAdapter;

public class MyStar extends AppCompatActivity {
    private SmartTabLayout myStarSmartTabLayout;
    private ViewPager myStarViewPager;
    private FragmentStatePagerItemAdapter fragmentStatePagerItemAdapter;
    ImageView myStarBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystar);
        initView();
        myStarViewPager.setOffscreenPageLimit(3);
        initTab();
        myStarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab() {
        String[] tabs = new String[]{"帖子","论坛"};
        FragmentPagerItems fragmentPagerItems = new FragmentPagerItems(MyStar.this);
        for(int i=0;i<tabs.length;i++){
            Bundle bundle = new Bundle();
            bundle.putString("name",tabs[i]);
        }
        fragmentPagerItems.add(FragmentPagerItem.of(tabs[0], FragmentContent.class));
        fragmentPagerItems.add(FragmentPagerItem.of(tabs[1], FragmentCommunity.class));
        myStarViewPager.removeAllViews();
        fragmentStatePagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(),fragmentPagerItems);
        myStarViewPager.setAdapter(fragmentStatePagerItemAdapter);
        myStarSmartTabLayout.setViewPager(myStarViewPager);
    }

    private void initView() {
        myStarSmartTabLayout = findViewById(R.id.myStarSmartTabLayout);
        myStarViewPager = findViewById(R.id.myStarViewPager);
        myStarBack = findViewById(R.id.myStarBack);
    }
}
