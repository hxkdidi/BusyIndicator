package com.silverforge.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.silverforge.library.R;

public class BusyIndicator extends View {
    private int pointCount;

    private boolean firstLoad = true;
    private ItemCoordinate[] outerItems;
    private Paint outerPaint;
    private Paint singlePaint;
    private ItemCoordinate single;

    private float layoutCenterX;
    private float layoutCenterY;

    private float baseRadius;
    private float basePointRadius;

    private float singleRadius;
    private float singlePointRadius;

    private byte angleModifier = 1;

    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes
            = context
                .getTheme()
                .obtainStyledAttributes(attrs,
                                        R.styleable.BusyIndicator,
                                        defStyleAttr,
                                        0);
        int bigPointColor = attributes.getColor(R.styleable.BusyIndicator_bigpoint_color, Color.DKGRAY);
        int smallPointColor = attributes.getColor(R.styleable.BusyIndicator_smallpoint_color, Color.BLACK);
        int bigPointCount = attributes.getColor(R.styleable.BusyIndicator_bigpoint_count, Color.BLACK);
        if (bigPointCount < 4)
            bigPointCount = 4;
        if (bigPointCount > 20)
            bigPointCount = 20;

        pointCount= bigPointCount;
        outerItems = new ItemCoordinate[pointCount];

        outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setColor(bigPointColor);

        singlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        singlePaint.setColor(smallPointColor);

        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawARGB(100, 200, 200, 200);

        if (firstLoad) {
            initializePoints();
            firstLoad = false;
        } else {
            calculateMoves();
        }

        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);
        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), item.getRadius(), outerPaint);
        }

        invalidate();
    }

    private void calculateMoves() {
        float singleAngle = single.getAngle();
        singleAngle -= angleModifier;
        if (singleAngle < 0)
            singleAngle = singleAngle + 360;

        single = getItemCoordinate(singleAngle, singleRadius);

        for (int i = 0; i < pointCount; i++) {
            float angle = outerItems[i].getAngle();
            angle += angleModifier;
            if (angle > 360)
                angle = angle - 360;

            ItemCoordinate itemCoordinate = getItemCoordinate(angle, baseRadius, single);
            outerItems[i] = itemCoordinate;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (angleModifier > 1)
                    angleModifier = 1;
                else
                    angleModifier = 2;
                break;
        }

        return super.onTouchEvent(event);
    }

    private void initializePoints() {
        int height = getHeight();
        int width = getWidth();
        baseRadius = height > width ? width /2 : height / 2;
        baseRadius = (float)(baseRadius * 0.75);
        basePointRadius = (float) (baseRadius * 0.15);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;

        float slice = 360 / pointCount;
        for (int i = 0; i < pointCount; i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(angle, baseRadius);
            outerItems[i] = itemCoordinate;
        }

        singleRadius = (float) (baseRadius * 0.80);
        singlePointRadius = (float) (singleRadius * 0.05);
        single = getItemCoordinate(270, singleRadius);
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius) {
        return getItemCoordinate(angleInDegrees, radius, null);
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius, ItemCoordinate affectedItem) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
        float y = (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);

        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);
        retValue.setRadius(basePointRadius);

//        if (affectedItem != null) {
//            float abs = Math.abs(affectedItem.getAngle() - retValue.getAngle());
//            retValue.setRadius((float) (basePointRadius + (abs * 0.20)));
//        }

        return retValue;
    }
}
