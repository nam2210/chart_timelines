package com.hnam.timelinechart;

import android.graphics.Color;

/**
 * Created by nampham on 7/18/17.
 * point object is represent time point in 24h
 */

public abstract class Point {
    public static final int MAX_OFFSET = 1440;

    private int x;
    private int y;
    private int colorId;
    private int radius;

    private int hour;
    private int minute;
    private String description;

    public Point(int hour, int minute, String description){
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.radius = 12;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setCoordinates(int x, int heightOfTimeline){
        this.x = x;
        this.y = (heightOfTimeline * (hour * 60 + minute)) / MAX_OFFSET;
    }

    public int getRadius() {
        return radius;
    }

    public String getTimeInText(){
        String result = String.valueOf(hour) + ":";
        if (minute <= 9){
            result += "0" + String.valueOf(minute);
        } else {
            result += String.valueOf(minute);
        }
        return result;
    }


}
