package com.silverforge.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BusyIndicator extends View {
    private float layoutCenterX;
    private float layoutCenterY;
    private float baseRadius;


    public BusyIndicator(Context context) {
        super(context);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        baseRadius = height > width ? width /2 : height / 2;
        baseRadius = (float)(baseRadius * 0.75);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);

//        canvas.drawARGB(255, 200, 200, 200);
//        canvas.drawCircle(layoutCenterX, layoutCenterY, 20, paint);

//        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint1.setColor(Color.BLUE);
//
//        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint2.setColor(Color.BLACK);
//
//        ItemCoordinate itemCoordinate1 = getItemCoordinate(90);
//        ItemCoordinate itemCoordinate2 = getItemCoordinate(180);
//
//        canvas.drawCircle(itemCoordinate1.getX(), itemCoordinate1.getY(), 20, paint1);
//        canvas.drawCircle(itemCoordinate2.getX(), itemCoordinate2.getY(), 20, paint2);

//        double slice = 2 * Math.PI / 10;
        float slice = 360 / 10;
        for (int i = 0; i < 10; i++) {
            float angle = slice * i;

            ItemCoordinate itemCoordinate = getItemCoordinate(angle);
            canvas.drawCircle(itemCoordinate.getX(), itemCoordinate.getY(), 20, paint);
        }

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
