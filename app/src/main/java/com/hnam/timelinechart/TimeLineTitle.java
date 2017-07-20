package com.hnam.timelinechart;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import static com.hnam.timelinechart.TimeLineConstant.*;

/**
 * Created by nampham on 7/19/17.
 */

public class TimeLineTitle extends ViewGroup implements View.OnClickListener{
    private static final String TAG = TimeLineTitle.class.getSimpleName();
    int deviceWidth;
    int firstX = 0;

    public TimeLineTitle(Context context) {
        super(context);
        init(context);

    }

    public TimeLineTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        android.graphics.Point deviceDisplay = new android.graphics.Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
        firstX = (deviceWidth * DEFAULT_FIRST_X) / DEFAULT_DEVICE_WIDTH;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curLeftPos;

        //get the available size of view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;
        curLeftPos = firstX + childLeft;

        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            int vcWidth = child.getMeasuredWidth();
            int vcHeight = child.getMeasuredHeight();
            child.layout((i+1)*curLeftPos - vcWidth/2, childTop, (i+1)*curLeftPos + vcWidth/2, childTop + vcHeight);
            child.setOnClickListener(this);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = getMeasuredWidth() + getPaddingLeft() + getPaddingTop();
        int maxHeight = 0;
        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);

            //measure child and calculate max height for parent view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Report our final dimensions.
        // Width and height to determine the final view
        int finalWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int finalHeight = resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);
        setMeasuredDimension(finalWidth, finalHeight);
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG,"click here>>>>>");
    }
}
