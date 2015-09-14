package com.silverforge.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BusyIndicator extends View {
    private static final int POINT_COUNT = 12;

    private float layoutCenterX;
    private float layoutCenterY;
    private float baseRadius;
    private float pointRadius;
    private boolean firstLoad = true;

    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (firstLoad)
            init();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);

        canvas.drawARGB(100, 200, 200, 200);
        float slice = 360 / POINT_COUNT;
        for (int i = 0; i < POINT_COUNT; i++) {
            float angle = slice * i;

            ItemCoordinate itemCoordinate = getItemCoordinate(angle);
            canvas.drawCircle(itemCoordinate.getX(), itemCoordinate.getY(), pointRadius, paint);
        }

    }

    private void init() {
        int height = getHeight();
        int width = getWidth();
        baseRadius = height > width ? width /2 : height / 2;
        baseRadius = (float)(baseRadius * 0.75);
        pointRadius = (float) (baseRadius * 0.15);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float)(baseRadius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX;
        float y = (float)(baseRadius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY;

        retValue.setX(x);
        retValue.setY(y);

        return retValue;
    }
}
