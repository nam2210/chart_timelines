package com.hnam.timelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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

import static com.hnam.timelinechart.TimeLineConstant.*;

/**
 * Created by nampham on 7/17/17.
 */

public class TimeLineChart extends View {
    private static final String TAG = TimeLineChart.class.getSimpleName();

    private int firstX;
    private int defaultD;
    private int defaultX;
    private int spacing;
    private float density;

    public TimeLineChart(Context context) {
        super(context);
        prepareHeight(context);
        prepareListener();

        //prepare data for testing
        //prepareData();
        //prepareTimelines();
    }

    public TimeLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        prepareHeight(context);
        prepareListener();

        //prepare data for testing
        //prepareData();
        //prepareTimelines();
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
        //set final dimensions
        setMeasuredDimension(width, heightSize);

        //prepare info
        prepareParameters(width);
        prepareDrawables();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // When the view is displayed when the callback
        // Positioning Drawable coordinates, then draw
        prepareDrawables();
    }


    int width = 0;
    int height = 0;

    Paint paint = new Paint();
    Paint paintText = new Paint();
    Paint paintTextSchedules = new Paint();
    Paint paintCircle = new Paint();
    Paint paintLine = new Paint();
    Paint paintDivider = new Paint();

    private void prepareHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        //newHeight = height - height of toolbar - height of padding
        density = TimeLineUtils.density(context);
        height = size.y - (int) (81 * density) - (int) (32 * density);
    }

    private void prepareParameters(int width) {
        firstX = (width * DEFAULT_FIRST_X) / DEFAULT_DEVICE_WIDTH;
        defaultD = (width * DEFAULT_D) / DEFAULT_DEVICE_WIDTH;
        defaultX = (width * DEFAULT_X) / DEFAULT_DEVICE_WIDTH;
        spacing = (width * DEFAULT_SPACING_16) / DEFAULT_DEVICE_WIDTH;
    }

    private void prepareDrawables() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        paintText.setColor(Color.BLACK);
        paintText.setTextSize(20);
        paintText.setAntiAlias(true);

        paintTextSchedules.setColor(Color.parseColor("#FF9800"));
        paintTextSchedules.setTextSize(22);
        paintTextSchedules.setAntiAlias(true);
        paintTextSchedules.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

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

        // draw chart
        drawChart(canvas);

        //draw chart for series model
        drawTimeLines(canvas);
    }


    private void drawChart(Canvas canvas){
        canvas.save();
        for (int i = 0; i < INTERVAL; i++) {
            String text = String.valueOf(i) + ":00";
            int xBaseline = 8 + getPaddingLeft();
            int yBaseline = (i * height / INTERVAL)
                    + getPaddingTop() * 2
                    + 20;
            canvas.drawText(text, xBaseline, yBaseline, paintText);
        }


        for (int i = 0; i < INTERVAL + 1; i++) {
            //draw vertical lines
            int verticalStartX = getPaddingLeft() + (int)(21 * density); //64
            int verticalStartY = (i * height / INTERVAL) + getPaddingTop() * 2;
            int verticalStopX = verticalStartX;
            int verticalStopY = height / INTERVAL;
            canvas.drawLine(verticalStartX, verticalStartY, verticalStopX, verticalStopY, paint);

            //draw horizontal line
            int hStartX = getPaddingLeft() + (int)(21 * density);//64
            int hStartY = (i * height / INTERVAL) + getPaddingTop() * 2;
            int hStopX = width;
            int hStopY = hStartY;
            canvas.drawLine(hStartX, hStartY, hStopX, hStopY, paintDivider);
        }
    }

    //draw time line
    Rect bounds = new Rect();
    private void drawTimeLines(Canvas canvas){
        // draw vertical lines
        for (int i = 0; i < timelines.size(); i++){
            int xPosition = firstX * (i + 1);
            List<Point> p = timelines.get(i);

            //draw lines
            canvas.restore();
            canvas.save();
            for (int j = 0; j < INTERVAL + 1; j++) {
                int startX = xPosition;
                int startY = j * height / INTERVAL + getPaddingTop() * 2;
                int stopY = height/ INTERVAL;
                canvas.drawLine(startX, startY, startX, stopY, paintLine);
            }

            // draw point
            canvas.restore();
            canvas.save();
            for (Point point : p){
                point.setCoordinates(xPosition, height);
                if (point instanceof CirclePoint) {
                    //draw circle
                    paintCircle.setColor(point.getColorId());
                    canvas.drawCircle(point.getX(), point.getY() + getPaddingTop() * 2, point.getRadius(), paintCircle);

                    //draw text
                    paintText.setColor(Color.BLACK);
                    paintText.getTextBounds(point.getTimeInText(), 0, point.getTimeInText().length(), bounds);
                    int height = bounds.height();
                    int xBaseline = point.getX() + (int) (DEFAULT_SPACING_6 * density);
                    int yBaseline = point.getY() + getPaddingTop() * 2 + height / 2;

                    canvas.drawText(point.getTimeInText(), xBaseline, yBaseline, paintText);
                } else if (point instanceof LinePoint) {
                    //draw line
                    int startX = point.getX() - 8;
                    int startY = point.getY() + getPaddingTop() * 2;
                    int stopX = point.getX() + 8;
                    int stopY = startY;
                    canvas.drawLine(startX, startY, stopX, stopY, paintLine);

                    //draw text
                    paintTextSchedules.getTextBounds(point.getTimeInText(), 0, point.getTimeInText().length(), bounds);
                    int height = bounds.height();
                    float width = paintText.measureText(point.getTimeInText());
                    int xBaseline = point.getX() - (int)width - (int)(DEFAULT_SPACING_8 * density); // 16
                    int yBaseline = point.getY() + getPaddingTop() * 2 + height / 2;
                    canvas.drawText(point.getTimeInText(), xBaseline, yBaseline, paintTextSchedules);
                }
            }
        }
    }


    List<List<Point>> timelines = new ArrayList<>();
    List<Point> points;
    private void prepareData() {
        points = new ArrayList<>();
        points.add(new CirclePoint(0, 0, ""));
        points.add(new CirclePoint(1, 30, ""));
        points.add(new CirclePoint(2, 59, ""));
        points.add(new LinePoint(3, 5, ""));
        points.add(new CirclePoint(3, 5, ""));
        points.add(new CirclePoint(4, 10, ""));
        points.add(new CirclePoint(4, 3, ""));
        points.add(new LinePoint(4, 30, ""));
        points.add(new CirclePoint(19, 0, ""));
        points.add(new CirclePoint(19, 45, ""));
        points.add(new LinePoint(23, 0, ""));
        points.add(new CirclePoint(23, 45, ""));
        points.add(new CirclePoint(23, 59, ""));


        for (Point point : points) {
            if (point instanceof CirclePoint) {
                if (point.getHour() <= 4) {
                    point.setColorId(Color.parseColor("#4CAF50"));
                } else if (point.getHour() > 4 && point.getHour() <= 19) {
                    point.setColorId(Color.parseColor("#f44336"));
                } else {
                    point.setColorId(Color.parseColor("#FF9800"));
                }
            } else if (point instanceof LinePoint) {
                point.setColorId(Color.parseColor("#FF9800"));
            }
        }
    }

    private void prepareTimelines(){
        List<Point> ps = new ArrayList<>();
        ps.add(new CirclePoint(0, 0, ""));
        ps.add(new CirclePoint(1, 30, ""));
        ps.add(new CirclePoint(2, 59, ""));
        ps.add(new LinePoint(3, 5, ""));
        ps.add(new CirclePoint(3, 5, ""));
        ps.add(new CirclePoint(4, 10, ""));
        ps.add(new CirclePoint(4, 3, ""));
        ps.add(new LinePoint(4, 30, ""));
        ps.add(new CirclePoint(19, 0, ""));
        ps.add(new CirclePoint(19, 45, ""));
        ps.add(new LinePoint(23, 0, ""));
        ps.add(new CirclePoint(23, 45, ""));
        ps.add(new CirclePoint(23, 59, ""));


        for (Point point : ps) {
            if (point instanceof CirclePoint) {
                if (point.getHour() <= 4) {
                    point.setColorId(Color.parseColor("#4CAF50"));
                } else if (point.getHour() > 4 && point.getHour() <= 19) {
                    point.setColorId(Color.parseColor("#f44336"));
                } else {
                    point.setColorId(Color.parseColor("#FF9800"));
                }
            } else if (point instanceof LinePoint) {
                point.setColorId(Color.parseColor("#FF9800"));
            }
        }
        timelines.add(ps);
        List<Point> p1 = new ArrayList<>(ps);
        List<Point> p2 = new ArrayList<>(ps);
        timelines.add(p1);
        timelines.add(p2);
    }

    public void addTimeLine(List<Point> timeline){
        this.timelines.add(timeline);
        postInvalidate();
    }

    public void addTimeLines(List<List<Point>> timelines){
        this.timelines.addAll(timelines);
        postInvalidate();
    }

    /** =============
     * handle gesture
     * ==============
     */
    GestureDetector gestureDetector;
    private void prepareListener() {
        gestureDetector = new GestureDetector(this.getContext(), new TapGesture());
    }

    float scale = 1f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    private class TapGesture extends GestureDetector.SimpleOnGestureListener {
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
                height = Math.round((scale * height) / previousScale);
                requestLayout();
            }
            return super.onSingleTapConfirmed(e);
        }
    }



}
