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
    private final static int INITIAL_SMALL_POSITION = 270;

    private boolean firstLoad = true;

    private int bigPointCount;
    private Bitmap canvasBackground;
    private boolean isBackgroundVisible;
    private int backgroundColor;
    private ClipShape backgroundShape;
    private boolean infinite;
    private float maxValue;

    private ItemCoordinate[] outerItems;
    private Paint bigPaint;
    private Paint singlePaint;
    private Paint singlePaintTransparent;
    private ItemCoordinate single;
    private ItemCoordinate singleFixPoint;

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
            if (infinite)
                calculateMoves();
        }

        if (isBackgroundVisible)
            canvas.drawBitmap(canvasBackground, 0, 0, null);

        if (infinite)
            drawInfiniteIndicator(canvas);
        else
            drawLoadingIndicator(canvas);
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

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = (float)maxValue;
    }

    public void setValue(float value) {
        calculateProgress(value);
    }

    public void setValue(int value) {
        calculateProgress((float)value);
    }

    private void calculateProgress(float value) {
        if (value <= maxValue && value > 0){
            float currentAngle = (360 / maxValue) * value;
            currentAngle += 270;
            single = getItemCoordinate(currentAngle, bigRadius, singlePointRadius);
            invalidate();
        }
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius, float pointRadius) {
        ItemCoordinate retValue = new ItemCoordinate();

        float x = (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
        float y = (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);

        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);
        retValue.setRadius(pointRadius);

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

        singlePaintTransparent = new Paint();
        singlePaintTransparent.setColor(smallPointColor);
        singlePaintTransparent.setAlpha(100);

        infinite = attributes.getBoolean(R.styleable.BusyIndicator_infinite, true);
        maxValue = attributes.getFloat(R.styleable.BusyIndicator_max_value, 100F);
    }

    private void drawInfiniteIndicator(Canvas canvas) {
        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), item.getRadius(), bigPaint);
        }

        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);

        invalidate();
    }

    private void drawLoadingIndicator(Canvas canvas) {
        RectF rect = new RectF();
        rect.set(0, 0, getHeight(), getWidth());
        canvas.drawArc(rect, 630, single.getAngle() - 270, true, singlePaintTransparent);

        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), singlePointRadius, bigPaint);
        }
        canvas.drawCircle(singleFixPoint.getX(), singleFixPoint.getY(), singlePointRadius, singlePaint);
        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);
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
            ItemCoordinate itemCoordinate = getItemCoordinate(angle, bigRadius, bigPointRadius);
            outerItems[i] = itemCoordinate;
        }

        singleRadius = (float) (bigRadius * 0.80);
        singlePointRadius = (float) (singleRadius * 0.05);

        if (infinite) {
            single = getItemCoordinate(INITIAL_SMALL_POSITION, singleRadius, singlePointRadius);
            singleFixPoint = getItemCoordinate(INITIAL_SMALL_POSITION, singleRadius, singlePointRadius);
        } else {
            single = getItemCoordinate(INITIAL_SMALL_POSITION, bigRadius, singlePointRadius);
            singleFixPoint = getItemCoordinate(INITIAL_SMALL_POSITION, bigRadius, singlePointRadius);
        }
    }

    private void initializeCanvas() {
        Bitmap cb = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(cb);
        cv.drawColor(backgroundColor);
        canvasBackground = getRoundedBitmap(cb, backgroundShape);
    }

    private void calculateMoves() {
        for (int i = 0; i < bigPointCount; i++) {
            float angle = outerItems[i].getAngle();
            angle += angleModifier;
            if (angle > 360)
                angle = angle - 360;

            ItemCoordinate itemCoordinate = getItemCoordinate(angle, bigRadius, bigPointRadius);
            outerItems[i] = itemCoordinate;
        }

        float singleAngle = single.getAngle();
        singleAngle -= angleModifier;
        if (singleAngle < 0)
            singleAngle = singleAngle + 360;

        single = getItemCoordinate(singleAngle, singleRadius, singlePointRadius);
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
