package com.netcosports.rectangleview;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by mrgiua on 07/02/2015.
 */
public class ConvertHelper {
    public static int toPixels(int dp, DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
