package com.colorworld.manbocash.rank;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colorworld.manbocash.R;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class RankRowViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userRank, userId, userWalkCount;

        RankRowViewHolder(View view){
            super(view);
            userImage = view.findViewById(R.id.recycle_row_pr_img);
            userRank = view.findViewById(R.id.recycle_row_rank_tv);
            userId = view.findViewById(R.id.recycle_row_id_tv);
            userWalkCount = view.findViewById(R.id.recycle_row_walkcount_tv);

        }
    }

    private ArrayList<RankInfo> rankInfoArrayList;
    RankAdapter(ArrayList<RankInfo> rankInfoArrayList){
        this.rankInfoArrayList = rankInfoArrayList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_recycler_row, parent, false);

        return new RankRowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        RankRowViewHolder rankViewHolder = (RankRowViewHolder) holder;

        rankViewHolder.userImage.setImageBitmap(rankInfoArrayList.get(position).profile_Image);
        rankViewHolder.userRank.setText(String.valueOf(rankInfoArrayList.get(position).kRank));
        rankViewHolder.userId.setText(rankInfoArrayList.get(position).profile_Id);
        rankViewHolder.userWalkCount.setText(rankInfoArrayList.get(position).user_walk_count);


    }

    @Override
    public int getItemCount() {
        return rankInfoArrayList.size();
    }
}
