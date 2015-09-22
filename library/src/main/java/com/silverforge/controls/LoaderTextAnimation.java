package com.silverforge.controls;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LoaderTextAnimation extends Animation {
    private BusyIndicator busyIndicator;

    public LoaderTextAnimation(BusyIndicator busyIndicator) {
        this.busyIndicator = busyIndicator;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        int newAlpha = (int) (127 * interpolatedTime);
        busyIndicator.setTextAlpha(newAlpha - 127);
        busyIndicator.setTextAlpha(127 + newAlpha);

        busyIndicator.requestLayout();
    }
}
