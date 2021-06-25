package com.colorworld.manbocash.rank;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public class RankInfo {

    public int kRank;
    public Bitmap profile_Image;
    public String profile_Id;
    public String user_walk_count;


    public RankInfo (int rank, Bitmap profile_Image, String profile_Id, String user_walk_count) {
        this.kRank = rank;
        this.profile_Image = profile_Image;
        this.profile_Id = profile_Id;
        this.user_walk_count = user_walk_count;
    }


    public int getRank() {
        return kRank;
    }

    public void setRank(int rank) {
        kRank = rank;
    }

    public Bitmap getProfile_Image() {
        return profile_Image;
    }

    public void setProfile_Image(Bitmap profile_Image) {
        this.profile_Image = profile_Image;
    }

    public String getProfile_Id() {
        return profile_Id;
    }

    public void setProfile_Id(String profile_Id) {
        this.profile_Id = profile_Id;
    }

    public String getUser_walk_count() {
        return user_walk_count;
    }

    public void setUser_walk_count(String user_walk_count) {
        this.user_walk_count = user_walk_count;
    }
}
