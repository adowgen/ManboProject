package com.colorworld.manbocash.mainFragments;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.colorworld.manbocash.R;
import com.colorworld.manbocash.chartFragment.ChartPageAdapter;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class chartFragment extends Fragment {

    private ViewPager mViewPager;
    private ChartPageAdapter chartPageAdapter;
    private NavigationTabStrip mCenterNavigationTabStrip;
    private TextView mCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_chart, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.vp);
        mCenterNavigationTabStrip = (NavigationTabStrip) rootView.findViewById(R.id.nts_center);
        chartPageAdapter = new ChartPageAdapter(getActivity());
        mViewPager.setAdapter(chartPageAdapter);

        mCenterNavigationTabStrip.setViewPager(mViewPager, 0);


        return rootView;

    }
}
