package com.hnam.timelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nampham on 7/17/17.
 */

public class TimeLineChart extends View {
    private static final String TAG = TimeLineChart.class.getSimpleName();


    public TimeLineChart(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        height = size.y;
        height = size.y - (int) (81 * TimeLineUtils.density(context)) - (int) (32 * TimeLineUtils.density(context));
        Log.e(TAG, "height>>>> " + height);
        prepareData();
        prepareListener();
    }

    public TimeLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        height = size.y - (int) (81 * TimeLineUtils.density(context)) - (int) (32 * TimeLineUtils.density(context));
        Log.e(TAG, "height>>>> " + height);
        prepareData();
        prepareListener();
    }

    public TimeLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getPaddingLeft() + getPaddingRight() + getMinimumWidth();
        int h = height + (int) (32 * TimeLineUtils.density(getContext()));

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        width = widthSize;
        setMeasuredDimension(width, heightSize);
        Log.e(TAG, "onMeasure>>>>>>: " + width + " -- " + heightSize);
        initDrawables();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // When the view is displayed when the callback
        // Positioning Drawable coordinates, then draw
        initDrawables();
    }


    int width = 0;
    int height = 0;

    Paint paint = new Paint();
    Paint paintText = new Paint();
    Paint paintTextSchedules = new Paint();
    Paint paintCircle = new Paint();
    Paint paintLine = new Paint();
    Paint paintDivider = new Paint();


    //drawable line

    //drawable text

    private void initDrawables() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        paintText.setColor(Color.BLACK);
        paintText.setTextSize(20);
        paintText.setAntiAlias(true);

        paintTextSchedules.setColor(Color.parseColor("#FF9800"));
        paintTextSchedules.setTextSize(20);
        paintTextSchedules.setAntiAlias(true);

        paintCircle.setAntiAlias(true);

        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(2f);
        paintLine.setColor(Color.BLUE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeJoin(Paint.Join.ROUND);

        paintDivider.setAntiAlias(true);
        paintDivider.setStrokeWidth(2f);
        paintDivider.setColor(Color.parseColor("#d3d3d3"));
        paintDivider.setStyle(Paint.Style.STROKE);
        paintDivider.setStrokeJoin(Paint.Join.ROUND);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw>>>>>>");
        canvas.save();
        for (int i = 0; i < 24; i++) {
            canvas.drawText(String.valueOf(i)+":00", 8 + getPaddingLeft(), (i * height / 24) + getPaddingTop() * 2 + 20, paintText);
        }


        for (int i = 0; i < 25; i++) {
            canvas.drawLine(30 + getPaddingLeft() + 34,
                    (i*height / 24) + getPaddingTop() * 2,
                    30 + getPaddingLeft() + 34,  height / 24, paint);
            canvas.drawLine(30 + getPaddingLeft() + 34,
                    (i*height / 24) + getPaddingTop() * 2,
                    width,
                    (i*height / 24) + getPaddingTop() * 2, paintDivider);
        }

        //draw chart for series model
        canvas.restore();
        canvas.save();
        for (int i = 0; i < 25; i++) {
            canvas.drawLine(200, i * height/24 + getPaddingTop() * 2, 200, height/24, paintLine);
        }


        canvas.restore();
        canvas.save();
        Rect bounds = new Rect();
        for (Point point : points){
            point.setCoordinates(200, height);
            if (point instanceof CirclePoint) {
                paintCircle.setColor(point.getColorId());
                canvas.drawCircle(point.getX(), point.getY() + getPaddingTop() * 2, point.getRadius(), paintCircle);
                paintText.setColor(Color.BLACK);
                paintText.getTextBounds(point.getTimeInText(), 0, point.getTimeInText().length(), bounds);
                int height = bounds.height();
                canvas.drawText(point.getTimeInText(),
                        point.getX() + 16,
                        point.getY() + getPaddingTop() * 2 + height / 2, paintText);
            } else if (point instanceof LinePoint){
                int startX = point.getX() - 8;
                int startY = point.getY() + getPaddingTop() * 2;
                int stopX = point.getX() + 8;
                int stopY = startY;
                canvas.drawLine(startX, startY, stopX, stopY, paintLine);
                paintText.getTextBounds(point.getTimeInText(), 0, point.getTimeInText().length(), bounds);
                paintText.setColor(point.getColorId());
                int height = bounds.height();
                float width = paintText.measureText(point.getTimeInText());
                canvas.drawText(point.getTimeInText(),
                        point.getX() - width - 16,
                        point.getY() + getPaddingTop() * 2 + height / 2, paintText);
            }
        }


//
//        canvas.restore();
//        canvas.save();
//        for (int i = 0; i < 24; i++) {
//            canvas.drawText(String.valueOf(i), 300, i * height/12, paintText);
//        }
//
//        for (int i = 0; i < 25; i++) {
//            canvas.drawLine(334, i * height/12, 334, height/12, paint);
//        }
//
//        canvas.restore();
//        canvas.save();
//        for (int i = 0; i < 24; i++) {
//            canvas.drawText(String.valueOf(i), 400, i * height/12, paintText);
//        }
//
//        for (int i = 0; i < 25; i++) {
//            canvas.drawLine(434, i * height/12, 434, height/12, paint);
//        }

    }

    List<Point> points;
    private void prepareData(){
        points = new ArrayList<>();
        points.add(new CirclePoint(0,0, ""));
        points.add(new CirclePoint(1,30, ""));
        points.add(new CirclePoint(2,59, ""));
        points.add(new LinePoint(3,5, ""));
        points.add(new CirclePoint(3,5, ""));
        points.add(new CirclePoint(4,10, ""));
        points.add(new CirclePoint(4,3, ""));
        points.add(new LinePoint(4,30, ""));
        points.add(new CirclePoint(19, 0, ""));
        points.add(new CirclePoint(19, 45, ""));
        points.add(new LinePoint(23,0, ""));
        points.add(new CirclePoint(23, 45, ""));
        points.add(new CirclePoint(23, 59, ""));


        for (Point point : points){
            if (point instanceof CirclePoint) {
                if (point.getHour() <= 4) {
                    point.setColorId(Color.parseColor("#4CAF50"));
                } else if (point.getHour() > 4 && point.getHour() <= 19) {
                    point.setColorId(Color.parseColor("#f44336"));
                } else {
                    point.setColorId(Color.parseColor("#FF9800"));
                }
            } else if (point instanceof LinePoint){
                point.setColorId(Color.parseColor("#FF9800"));
            }
        }
    }

    GestureDetector gestureDetector;
    private void prepareListener(){
        gestureDetector = new GestureDetector(this.getContext(), new TapGesture());

    }

    float scale = 1f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }



    private class TapGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e(TAG, "double tap");
            if (scale <= 2) {
                scale *= 1.25;
                height *= 1.25;
                requestLayout();
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "single tap");
            if (scale > 1) {
                float previousScale = scale;
                scale /= 1.25;
                height = Math.round((scale * height)/previousScale);
                requestLayout();
            }
            return super.onSingleTapConfirmed(e);
        }
    }



}
