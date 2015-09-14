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
    private ItemCoordinate[] outerItems = new ItemCoordinate[POINT_COUNT];
    private Paint outerPaint;
    private Paint singlePaint;
    private ItemCoordinate single;
    private float singleRadius;
    private float singlePointRadius;

    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(Color.DKGRAY);

        singlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        singlePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(100, 200, 200, 200);

        if (firstLoad) {
            initializePoints();
            firstLoad = false;
        } else {
            for (int i = 0; i < POINT_COUNT; i++) {
                float angle = outerItems[i].getAngle();
                angle += 1;
                if (angle > 360)
                    angle = angle - 360;

                ItemCoordinate itemCoordinate = getItemCoordinate(angle, baseRadius);
                outerItems[i] = itemCoordinate;
            }

            float singleAngle = single.getAngle();
            singleAngle -= 1;
            if (singleAngle < 0)
                singleAngle = singleAngle + 360;

            single = getItemCoordinate(singleAngle, singleRadius);
        }

        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), pointRadius, outerPaint);
        }

        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);

        invalidate();
    }

    private void initializePoints() {
        int height = getHeight();
        int width = getWidth();
        baseRadius = height > width ? width /2 : height / 2;
        baseRadius = (float)(baseRadius * 0.75);
        pointRadius = (float) (baseRadius * 0.15);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;

        float slice = 360 / POINT_COUNT;
        for (int i = 0; i < POINT_COUNT; i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(angle, baseRadius);
            outerItems[i] = itemCoordinate;
        }

        singleRadius = (float) (baseRadius * 0.80);
        singlePointRadius = (float) (singleRadius * 0.05);
        single = getItemCoordinate(270, singleRadius);
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
        float y = (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);

        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);

        return retValue;
    }
}
