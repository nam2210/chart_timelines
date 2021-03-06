package com.hnam.timelinechart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hnam.tlchart.CirclePoint;
import com.hnam.tlchart.LinePoint;
import com.hnam.tlchart.Point;
import com.hnam.tlchart.TimeLineReportView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private TimeLineReportView reportView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        reportView = (TimeLineReportView) findViewById(R.id.timeline_report);
        prepareTimeLinesTitle();
        prepareTimelinesChart();
    }

    private void prepareTimeLinesTitle(){
        List<String> titles = new ArrayList<>();
        titles.add("abcdefghijklmsdfsdfdfdfd");
        titles.add("abcdefghijklmDFJSDLFJSA");
        titles.add("qwertyeruotrtrertert");
        titles.add("asdfdsffgfwererlkflg");
        titles.add("asdfdsffgfwererlkfla");
        reportView.setTimeLineTitle(titles);
    }

    private void prepareTimelinesChart(){
        List<List<Point>> timelines = new ArrayList<>();
        List<Point> ps = new ArrayList<>();
        ps.add(new CirclePoint(0, 0, "Overdose"));
        ps.add(new CirclePoint(1, 30, "on time"));
        ps.add(new CirclePoint(2, 59, "on time"));
        ps.add(new LinePoint(3, 5, "wrong time"));
        ps.add(new CirclePoint(3, 5, "wrong time"));
        ps.add(new CirclePoint(4, 10, "on time"));
        ps.add(new CirclePoint(4, 3, "wrong time"));
        ps.add(new LinePoint(4, 30, "on time"));
        ps.add(new CirclePoint(19, 0, "overdose"));
        ps.add(new CirclePoint(19, 45, "overdose"));
        ps.add(new LinePoint(23, 0, "wrong time"));
        ps.add(new CirclePoint(23, 45, "on time"));
        ps.add(new CirclePoint(23, 59, "wrong time"));


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
        List<Point> p3 = new ArrayList<>(ps);
        List<Point> p4 = new ArrayList<>(ps);
        timelines.add(p1);
        timelines.add(p2);
        timelines.add(p3);
        timelines.add(p4);
        reportView.setTimeLinePoints(timelines);
    }
}
