package com.colorworld.manbocash.tutorial.tutorials;

import androidx.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageSupportFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.colorworld.manbocash.R;


public class FifthCustomPageSupportFragment extends PageSupportFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tutorial_fifth;
    }

    @NonNull
    @Override
    protected TransformItem[] getTransformItems() {
        return new TransformItem[]{
                TransformItem.create(R.id.fake_ivFirstBtn, Direction.LEFT_TO_RIGHT, 0.7f),
                TransformItem.create(R.id.fake_ivSecondBtn, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.fake_ivThirdBtn, Direction.LEFT_TO_RIGHT, 0.05f)
        };
    }





}
