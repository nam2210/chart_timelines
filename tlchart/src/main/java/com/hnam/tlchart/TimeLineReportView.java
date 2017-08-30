package com.hnam.tlchart;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nampham on 7/20/17.
 */

public class TimeLineReportView extends LinearLayout implements TimeLineTitle.OnTimeLineTitleListener{

    private TimeLineTitle timeLineTitle;
    private TimeLineChart timeLineChart;
    private ScrollView scrollView;

    public TimeLineReportView(Context context) {
        super(context);
    }

    public TimeLineReportView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private float density = 0;
    private void init(Context context){
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_time_line_report, this, true);

        density = TimeLineUtils.density(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        timeLineTitle = (TimeLineTitle) this.findViewById(R.id.timeline_title);
        timeLineChart = (TimeLineChart) this.findViewById(R.id.timeline_chart);
        timeLineTitle.setOnTimelineTitleListener(this);
        scrollView = (ScrollView) findViewById(R.id.timeline_scrollView);
        timeLineChart.setOnScaleListener(new TimeLineChart.OnScaleListener() {
            @Override
            public void onScaleBegin() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onScaleEnd() {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });
    }

    public void destroy(){
        Log.e("TimeLineReport","on destroy");
        timeLineTitle.setOnTimelineTitleListener(null);
    }

    private void clearView(){
        //remove old test and redraw it
        timeLineTitle.removeAllViews();
    }

    private static final int TEXT_HEIGHT = 16;
    private static final int TEXT_WIDTH = 48;
    private static final int TEXT_SIZE = 10;
    List<String> titles = new ArrayList<>();

    //set timeline title
    public void setTimeLineTitle(List<String> titles){
        if (titles == null){
            return;
        }
        clearView();

        this.titles.addAll(titles);
        for (int i = 0; i < titles.size(); i++){
            TextView tv = new TextView(getContext());
//            ViewGroup.LayoutParams params =
//                    new ViewGroup.LayoutParams((int)(TEXT_WIDTH*density), (int)(TEXT_HEIGHT *density));
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams((int)(TEXT_WIDTH*density), ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            tv.setTextSize(TEXT_SIZE);
            tv.setMaxLines(2);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setText(titles.get(i));
            timeLineTitle.addView(tv);
        }
    }

    //set timeline points
    public void setTimeLinePoints(List<List<Point>> listOfPoints){
        if (listOfPoints == null){
            return;
        }
        timeLineChart.setTimeLines(listOfPoints);
    }

    @Override
    public void onTitleClick(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Medication name")
                .setMessage(msg)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     *
     * @param medName
     * @param point
     */

}
