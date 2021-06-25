package com.colorworld.manbocash.tutorial.tutorials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cleveroad.slidingtutorial.IndicatorOptions;
import com.cleveroad.slidingtutorial.OnTutorialPageChangeListener;
import com.cleveroad.slidingtutorial.TutorialOptions;
import com.cleveroad.slidingtutorial.TutorialPageOptionsProvider;
import com.cleveroad.slidingtutorial.TutorialPageProvider;
import com.cleveroad.slidingtutorial.TutorialSupportFragment;

import com.colorworld.manbocash.R;
import com.colorworld.manbocash.tutorial.renderer.CircleRenderer;

import static com.colorworld.manbocash.tutorial.tutorials.TutorialSupportActivity.KEY_ROLLBACK;

public class CustomTutorialSupportFragment extends TutorialSupportFragment
        implements OnTutorialPageChangeListener {

    private static final String TAG = "CustomTutorialSFragment";
    private static final int TOTAL_PAGES = 5;

    private final View.OnClickListener mOnSkipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Skip button clicked", Toast.LENGTH_SHORT).show();


        }
    };

    private final TutorialPageOptionsProvider mTutorialPageOptionsProvider = new CustomTutorialOptionsProvider();
    private final TutorialPageProvider<Fragment> mTutorialPageProvider = new CustomTutorialPageProvider();

    private int[] pagesColors;
    private boolean noRollback;

    public static CustomTutorialSupportFragment newInstance(boolean noRollback) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_ROLLBACK, noRollback);
        CustomTutorialSupportFragment fragment = new CustomTutorialSupportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) noRollback = args.getBoolean(KEY_ROLLBACK, false);

        if (pagesColors == null) {
            Context requireContext = requireContext();
            pagesColors = new int[]{
                    ContextCompat.getColor(requireContext, android.R.color.transparent),
                    ContextCompat.getColor(requireContext, android.R.color.transparent),
                    ContextCompat.getColor(requireContext, android.R.color.transparent),
                    ContextCompat.getColor(requireContext, android.R.color.transparent),
                    ContextCompat.getColor(requireContext, android.R.color.transparent)
            };
        }
        addOnTutorialPageChangeListener(this);
    }

    @Override
    protected TutorialOptions provideTutorialOptions() {
        return newTutorialOptionsBuilder(requireContext())
                .setUseAutoRemoveTutorialFragment(true)
                .setUseInfiniteScroll(false)
                .setNoRollBack(noRollback)
                .setPagesColors(pagesColors)
                .setPagesCount(TOTAL_PAGES)
                .setIndicatorOptions(IndicatorOptions.newBuilder(requireContext())
                        .setElementSizeRes(R.dimen.indicator_size)
                        .setElementSpacingRes(R.dimen.indicator_spacing)
                        .setElementColor(Color.LTGRAY)
                        .setSelectedElementColorRes(R.color.color_manbo)
                        .setRenderer(CircleRenderer.create())
                        .build())
                .setOnSkipClickListener(mOnSkipClickListener)
                .setTutorialPageProvider(mTutorialPageOptionsProvider)
                .setTutorialPageProvider(mTutorialPageProvider)
                .build();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.custom_tutorial_layout;
    }

    @Override
    public void onPageChanged(int position) {
        Log.i(TAG, "onPageChanged: position = " + position);

        if (position == 4) {
            Log.e("ios", "skip");

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(CustomTutorialSupportFragment.this)
                    .commitAllowingStateLoss();
//            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) 넣으면 에러

        }

        if (position == TutorialSupportFragment.EMPTY_FRAGMENT_POSITION) {
            Log.i(TAG, "onPageChanged: Empty fragment is visible");
        }


    }
}