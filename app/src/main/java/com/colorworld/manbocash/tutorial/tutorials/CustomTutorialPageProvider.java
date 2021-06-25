package com.colorworld.manbocash.tutorial.tutorials;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cleveroad.slidingtutorial.TutorialPageProvider;

public class CustomTutorialPageProvider implements TutorialPageProvider<Fragment> {

    private static final int ACTUAL_PAGES_COUNT = 5;

    @NonNull
    @Override
    public Fragment providePage(int position) {
        position %= ACTUAL_PAGES_COUNT;
        switch (position) {
            case 0:
                Fragment fr1 = new FirstCustomPageSupportFragment();
                return fr1;
            case 1:
                Fragment fr2 = new SecondCustomPageSupportFragment();
                return fr2;
            case 2:
                Fragment fr3 = new ThirdCustomPageSupportFragment();
                return fr3;
            case 3:
                Fragment fr4 = new FourthCustomPageSupportFragment();
                return fr4;
            case 4:
                Fragment fr5 = new FifthCustomPageSupportFragment();
                return fr5;
            default: {
                throw new IllegalArgumentException("Unknown position: " + position);
            }
        }
    }
}
