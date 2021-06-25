package com.colorworld.manbocash.tutorial.tutorials;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageOptions;
import com.cleveroad.slidingtutorial.TransformItem;
import com.cleveroad.slidingtutorial.TutorialPageOptionsProvider;
import com.colorworld.manbocash.R;

public class CustomTutorialOptionsProvider implements TutorialPageOptionsProvider {

    private static final int ACTUAL_PAGES_COUNT = 5;

    @NonNull
    @Override
    public PageOptions provide(int position) {
        @LayoutRes int pageLayoutResId;
        TransformItem[] tutorialItems;

        position %= ACTUAL_PAGES_COUNT;
        switch (position) {
            case 0: {
                pageLayoutResId = R.layout.fragment_tutorial_first;
                tutorialItems = new TransformItem[]{
                        TransformItem.create(R.id.ivFirstImage, Direction.LEFT_TO_RIGHT, 0.2f),
                        TransformItem.create(R.id.ivSecondImage, Direction.RIGHT_TO_LEFT, 0.06f)
//                        TransformItem.create(R.id.ivThirdImage, Direction.LEFT_TO_RIGHT, 0.08f),
//                        TransformItem.create(R.id.ivFourthImage, Direction.RIGHT_TO_LEFT, 0.1f),
//                        TransformItem.create(R.id.ivFifthImage, Direction.RIGHT_TO_LEFT, 0.03f),
//                        TransformItem.create(R.id.ivSixthImage, Direction.RIGHT_TO_LEFT, 0.09f),
//                        TransformItem.create(R.id.ivSeventhImage, Direction.RIGHT_TO_LEFT, 0.14f),
//                        TransformItem.create(R.id.ivEighthImage, Direction.RIGHT_TO_LEFT, 0.07f)
                };
                break;
            }
            case 1: {
                pageLayoutResId = R.layout.fragment_tutorial_second;
                tutorialItems = new TransformItem[]{
                        TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f),
                        TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f),
                        TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f)
//                        TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f),
//                        TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f),
//                        TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f),
//                        TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f),
//                        TransformItem.create(R.id.ivEighthImage, Direction.LEFT_TO_RIGHT, 0.07f)
                };
                break;
            }
            case 2: {
                pageLayoutResId = R.layout.fragment_tutorial_third;
                tutorialItems = new TransformItem[]{
                        TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f),
                        TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f),
                        TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f),
                        TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f),
                        TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f)
//                        TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f),
//                        TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f)
                };
                break;
            }
            case 3: {
                pageLayoutResId = R.layout.fragment_tutorial_fourth;
                tutorialItems = new TransformItem[]{
                        TransformItem.create(R.id.ivFirstImage, Direction.RIGHT_TO_LEFT, 0.2f),
                        TransformItem.create(R.id.ivSecondImage, Direction.LEFT_TO_RIGHT, 0.06f),
                        TransformItem.create(R.id.ivThirdImage, Direction.RIGHT_TO_LEFT, 0.08f),
//                            TransformItem.create(R.id.ivFourthImage, Direction.LEFT_TO_RIGHT, 0.1f),
//                            TransformItem.create(R.id.ivFifthImage, Direction.LEFT_TO_RIGHT, 0.03f),
//                            TransformItem.create(R.id.ivSixthImage, Direction.LEFT_TO_RIGHT, 0.09f),
//                            TransformItem.create(R.id.ivSeventhImage, Direction.LEFT_TO_RIGHT, 0.14f)
                };
                break;
            }
            case 4: {
                pageLayoutResId = R.layout.fragment_tutorial_fifth;
                tutorialItems = new TransformItem[]{
                        TransformItem.create(R.id.fake_ivFirstBtn, Direction.LEFT_TO_RIGHT, 0.7f),
                        TransformItem.create(R.id.fake_ivSecondBtn, Direction.LEFT_TO_RIGHT, 0.2f),
                        TransformItem.create(R.id.fake_ivThirdBtn, Direction.LEFT_TO_RIGHT, 0.05f)

                };
                break;
            }

            default: {
                throw new IllegalArgumentException("Unknown position: " + position);
            }
        }

        return PageOptions.create(pageLayoutResId, position, tutorialItems);
    }



}
