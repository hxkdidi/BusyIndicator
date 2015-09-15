package com.silverforge.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.silverforge.library.R;

public class BusyIndicator extends View {

    // region private members

    private boolean firstLoad = true;

    private int bigPointCount;
    private Bitmap canvasBackground;
    private boolean isBackgroundVisible;
    private int backgroundColor;
    private ClipShape backgroundShape;

    private ItemCoordinate[] outerItems;
    private Paint bigPaint;
    private Paint singlePaint;
    private ItemCoordinate single;

    private float layoutCenterX;
    private float layoutCenterY;

    private float bigRadius;
    private float bigPointRadius;

    private float singleRadius;
    private float singlePointRadius;

    private byte angleModifier = 1;

    // endregion

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

        readAttributeValues(attributes);
        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (firstLoad) {
            initializePoints();
            initializeCanvas();
            firstLoad = false;
        } else {
            calculateMoves();
        }

        if (isBackgroundVisible)
            canvas.drawBitmap(canvasBackground, 0, 0, null);

        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);
        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), item.getRadius(), bigPaint);
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
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

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
        float y = (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);

        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);
        retValue.setRadius(bigPointRadius);

        return retValue;
    }

    // region private methods

    private void readAttributeValues(TypedArray attributes) {
        isBackgroundVisible = attributes.getBoolean(R.styleable.BusyIndicator_background_is_visible, false);
        backgroundColor = attributes.getColor(R.styleable.BusyIndicator_background_color, Color.argb(100, 200, 200, 200));

        int clipShapeIndex = attributes.getInteger(R.styleable.BusyIndicator_background_shape, ClipShape.ROUNDED_RECTANGLE.getIndex());
        backgroundShape = ClipShape.values()[clipShapeIndex];

        bigPointCount = attributes.getInteger(R.styleable.BusyIndicator_bigpoint_count, Color.BLACK);
        if (bigPointCount < 4)
            bigPointCount = 4;
        if (bigPointCount > 20)
            bigPointCount = 20;

        outerItems = new ItemCoordinate[bigPointCount];

        int smallPointColor = attributes.getColor(R.styleable.BusyIndicator_smallpoint_color, Color.BLACK);
        int bigPointColor = attributes.getColor(R.styleable.BusyIndicator_bigpoint_color, Color.DKGRAY);

        bigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigPaint.setColor(bigPointColor);

        singlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        singlePaint.setColor(smallPointColor);
    }

    private void initializePoints() {
        int height = getHeight();
        int width = getWidth();
        bigRadius = height > width ? width /2 : height / 2;
        bigRadius = (float)(bigRadius * 0.75);
        bigPointRadius = (float) (bigRadius * 0.15);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;

        float slice = 360 / bigPointCount;
        for (int i = 0; i < bigPointCount; i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(angle, bigRadius);
            outerItems[i] = itemCoordinate;
        }

        singleRadius = (float) (bigRadius * 0.80);
        singlePointRadius = (float) (singleRadius * 0.05);
        single = getItemCoordinate(270, singleRadius);
    }

    private void initializeCanvas() {
        Bitmap cb = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(cb);
        cv.drawColor(backgroundColor);
        canvasBackground = getRoundedBitmap(cb, backgroundShape);
    }

    private void calculateMoves() {
        float singleAngle = single.getAngle();
        singleAngle -= angleModifier;
        if (singleAngle < 0)
            singleAngle = singleAngle + 360;

        single = getItemCoordinate(singleAngle, singleRadius);

        for (int i = 0; i < bigPointCount; i++) {
            float angle = outerItems[i].getAngle();
            angle += angleModifier;
            if (angle > 360)
                angle = angle - 360;

            ItemCoordinate itemCoordinate = getItemCoordinate(angle, bigRadius);
            outerItems[i] = itemCoordinate;
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap, ClipShape clipShape) {
        int zeroIntValue = 0;

        Bitmap resultBitmap;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float r;

        int size;
        if (originalWidth > originalHeight) {
            size = originalHeight;
            resultBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            r = originalHeight / 2;
        } else {
            size = originalWidth;
            resultBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            r = originalWidth / 2;
        }

        Canvas canvas = new Canvas(resultBitmap);

        final Paint paint = new Paint();
        final Rect rect = new Rect(zeroIntValue,
                zeroIntValue, originalWidth, originalHeight);

        paint.setAntiAlias(true);
        canvas.drawARGB(zeroIntValue, zeroIntValue,
                zeroIntValue, zeroIntValue);

        switch (clipShape) {
            case ROUNDED_RECTANGLE:
                RectF rectF = new RectF(rect);
                float cornerRadius = 32.0f;
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
                break;
            case CIRCLE:
                canvas.drawCircle(r, r, r, paint);
                break;
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return resultBitmap;
    }

    // endregion
}
