package com.hnam.timelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * Created by nampham on 7/19/17.
 */

public class TimeLineTextView extends View{
    private static final String TAG = TimeLineTextView.class.getSimpleName();
    private String text = "";
    private GestureDetector gestureDetector;
    public TimeLineTextView(Context context, String text) {
        super(context);
        this.text = text;

        gestureDetector = new GestureDetector(context, new TapGesture());
    }

    public TimeLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new TapGesture());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"onDraw.....");
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLUE);
        paintText.setTextSize(32);
        paintText.setStyle(Paint.Style.FILL);


        Rect rectText = new Rect();

        paintText.getTextBounds(text, 0, text.length(), rectText);
        canvas.drawText(text, 512, 50, paintText);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private class TapGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG,"on click view....");
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG,"on click view....");
            return super.onSingleTapUp(e);
        }
    }

}
