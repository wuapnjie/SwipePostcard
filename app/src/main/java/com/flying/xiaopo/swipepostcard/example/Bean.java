package com.flying.xiaopo.swipepostcard.example;

/**
 * Created by Flying SnowBean on 2016/1/26.
 */
public class Bean {
    private String text;
    private int resId;

    public Bean(int resId, String text) {
        this.resId = resId;
        this.text = text;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
