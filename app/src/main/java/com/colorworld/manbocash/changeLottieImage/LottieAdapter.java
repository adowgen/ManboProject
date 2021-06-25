package com.colorworld.manbocash.changeLottieImage;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.R;

import java.util.List;
import com.colorworld.manbocash.mainFragments.homeFragment;
import com.google.common.collect.BiMap;

public class LottieAdapter extends RecyclerView.Adapter<LottieAdapter.ViewHolder> {
    private Context mContext;
    private List<LottieItem> data;

    public LottieAdapter(Context context, List<LottieItem> data) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_lottie_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.image.setAnimation(data.get(position).getImage());
        if (data.get(position).isFlip()) {
            holder.image.setScaleX(-1);
        }else {
            holder.image.setScaleX(1);
        }

        if (data.get(position).getId() == Const.lottieImageType.WALKINGDOG) {
            holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.lottie_image_width_dog);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.lottie_image_height_dog);

            holder.image.setLayoutParams(layoutParams);

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) holder.image.getLayoutParams();
            marginParams.bottomMargin = 0;


        }else if (data.get(position).getId() == Const.lottieImageType.DOGWITHMAN) {
            holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.lottie_image_width_dogWithMan);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.lottie_image_height_dogWithMan);

            holder.image.setLayoutParams(layoutParams);

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) holder.image.getLayoutParams();
            marginParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.lottie_image_bottom_margin_dogWithMan);

        }else {
            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
            layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.lottie_image_width);
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.lottie_image_height);

            holder.image.setLayoutParams(layoutParams);

            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) holder.image.getLayoutParams();
            marginParams.bottomMargin = 0;

        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((homeFragment)homeFragment.mStaticHFContext).setLottieImage(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LottieAnimationView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
