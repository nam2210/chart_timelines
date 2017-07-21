package com.hnam.tlchart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nampham on 7/20/17.
 */

public class TimeLineReportView extends LinearLayout {

    private TimeLineTitle timeLineTitle;
    private TimeLineChart timeLineChart;

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
    }

    //set timeline title
    public void setTimeLineTitle(List<String> titles){
        if (titles == null){
            return;
        }
        for (int i = 0; i < titles.size(); i++){
            TextView tv = new TextView(getContext());
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams((int)(32*density), (int)(14 *density));
            tv.setLayoutParams(params);
            tv.setTextSize(10);
            tv.setMaxLines(1);
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
        timeLineChart.addTimeLines(listOfPoints);
    }

}
