package com.hnam.tlchart;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by nampham on 7/17/17.
 */

public class TimeLineUtils {

    public static float density(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;

    }
}
