package com.silverforge.controls.Model;

import com.silverforge.controls.ClipShape;

import lombok.Getter;
import lombok.Setter;

public class ConfigSettings {
    @Getter
    @Setter
    private boolean isBackgroundVisible;

    @Getter
    @Setter
    private ClipShape backgroundShape;

    @Getter
    @Setter
    private int backgroundColor;

    @Getter
    @Setter
    private int bigPointColor;

    @Getter
    @Setter
    private int smallPointColor;

    @Getter
    @Setter
    private int bigPointCount;

    @Getter
    @Setter
    private float maxValue;

    @Getter
    @Setter
    private boolean isPercentageVisible;

    @Getter
    @Setter
    private int percentageDecimalPlaces;

    @Getter
    @Setter
    private boolean isInfinite;
}
