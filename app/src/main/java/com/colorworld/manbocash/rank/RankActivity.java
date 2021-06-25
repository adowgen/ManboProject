package com.colorworld.manbocash.rank;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colorworld.manbocash.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {

    Context mContext;

    public TextView myRank, myId, myCount, userRank, userId, userCount;
    public ImageView myImage, userImage, backBtn;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;

    public PullToRefreshView mPullToRefreshView;
    public static final int REFRESH_DELAY = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_recycleview);

        mContext = getApplicationContext();

        myRank = (TextView) findViewById(R.id.my_rank_tv);
        myImage = (ImageView) findViewById(R.id.my_pr_img);
        myId = (TextView) findViewById(R.id.my_id_tv);
        myCount = (TextView) findViewById(R.id.my_walkcount_tv);
        backBtn = (ImageView) findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rank_recycleview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        ArrayList<RankInfo> rankInfoArrayList = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            if (i == 1) {
                rankInfoArrayList.add(new RankInfo( i, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rank_user_empty_img_gold), "일등 " + i , "7,777 걸음"));
            }else if (i == 2) {
                rankInfoArrayList.add(new RankInfo( i, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rank_user_empty_img_silver), "이등 " + i , "7,776 걸음"));
            }else if (i == 3) {
                rankInfoArrayList.add(new RankInfo( i, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rank_user_empty_img_bronze), "삼등 " + i , "7,775 걸음"));
            }else {
                rankInfoArrayList.add(new RankInfo( i, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rank_user_empty_img_nor), "테스터 " + i , "7,774 걸음"));
            }

        }

        RankAdapter myAdapter = new RankAdapter(rankInfoArrayList);

        mRecyclerView.setAdapter(myAdapter);



        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

    }
}
