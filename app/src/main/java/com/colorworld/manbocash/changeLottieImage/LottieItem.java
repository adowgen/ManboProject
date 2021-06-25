package com.colorworld.manbocash.changeLottieImage;

public class LottieItem {

    private final int id;
    private final int image;
    private final boolean flip;
    private final float width;
    private final float height;
    private final String ccl;

    public String getCcl() {
        return ccl;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public boolean isFlip() {
        return flip;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public LottieItem(int id, int image, boolean flip, float width, float height, String ccl) {
        this.id = id;
        this.image = image;
        this.flip = flip;
        this.width = width;
        this.height = height;
        this.ccl = ccl;
    }






}
