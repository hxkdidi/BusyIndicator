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
    private ItemCoordinate[] items = new ItemCoordinate[POINT_COUNT];
    private Paint paint;

    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (firstLoad) {
            initializePoints();
            canvas.drawARGB(100, 200, 200, 200);
            firstLoad = false;
        } else {
            for (int i = 0; i < POINT_COUNT; i++) {
                float angle = items[i].getAngle();
                angle += 1;
                if (angle > 360)
                    angle = angle - 360;

                ItemCoordinate itemCoordinate = getItemCoordinate(angle);
                items[i] = itemCoordinate;
            }
        }


        for (ItemCoordinate item : items) {
            canvas.drawCircle(item.getX(), item.getY(), pointRadius, paint);
        }

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
            ItemCoordinate itemCoordinate = getItemCoordinate(angle);
            items[i] = itemCoordinate;
        }
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float) ((baseRadius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
        float y = (float) ((baseRadius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);

        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);

        return retValue;
    }
}
