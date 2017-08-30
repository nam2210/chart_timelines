package com.hnam.tlchart;

/**
 * Created by nampham on 7/19/17.
 * Circle point is used for drawing
 * drawing circle
 */

public class CirclePoint extends Point {
    public CirclePoint(int hour, int minute, String description) {
        super(hour, minute, description);
    }

    private boolean isShowTimeText = true;

    public boolean isShowTimeText() {
        return isShowTimeText;
    }

    public void setShowTimeText(boolean showTimeText) {
        isShowTimeText = showTimeText;
    }

}
