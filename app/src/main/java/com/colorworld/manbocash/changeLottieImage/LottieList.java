package com.colorworld.manbocash.changeLottieImage;

import android.content.Context;
import android.content.SharedPreferences;

import com.colorworld.manbocash.R;

import java.util.Arrays;
import java.util.List;

public class LottieList {
    public static LottieList get() {
        return new LottieList();
    }

    public List<LottieItem> getData() {
        return Arrays.asList(
                new LottieItem(1, R.raw.dogandman, false, 120, 120, "Insan Budi Maulana"),
                new LottieItem(2, R.raw.walkingdog, false, 60, 60, "Anna Song"),
                new LottieItem(3, R.raw.couple, true, 60, 60, "Artem Peretrukhin"),
                new LottieItem(4, R.raw.woman_with_dog, true, 100, 100, "Insan Budi Maulana"),
                new LottieItem(5, R.raw.deadpool, true, 100, 100, "Anastasiia Vlasenko"),
                new LottieItem(6, R.raw.girl_in_reddress, false, 100, 100, "Alexander Rozhkov"),
                new LottieItem(7, R.raw.walking_robot, true, 100, 100, "Alexander Rozhkov")

        );
    }




//                new LottieItem(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
//                new LottieItem(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
//                new LottieItem(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
//                new LottieItem(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6));


}
