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
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.silverforge.library.R;

import java.util.HashMap;
import java.util.Map;

public class BusyIndicator extends Indicator {

    // region private members
    private final static int INITIAL_SMALL_POSITION = 270;

    private boolean firstLoad = true;

    private Bitmap canvasBackground;
    private float maxValue;
    private float currentValue;

    private Paint bigPaint;
    private Paint singlePaint;
    private Paint singlePaintTransparent;
    private Paint textPaint;

    private ItemCoordinate single;
    private ItemCoordinate singleFixPoint;

    private RectF rect = new RectF();

    private float layoutCenterX;
    private float layoutCenterY;

    private float textPosX;
    private float textPosY;

    private float bigRadius;

    private float singleRadius;
    private float singlePointRadius;

    private byte angleModifier = 1;

    private float arcAngle;

    protected ItemCoordinate[] outerItems;

    // endregion

    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        outerItems = new ItemCoordinate[configSettings.getBigPointCount()];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (firstLoad) {
            initializePoints();
            initializeCanvas();
            firstLoad = false;
        } else {
            if (configSettings.isInfinite())
                calculateInfiniteMoves();
        }

        if (configSettings.isBackgroundVisible())
            canvas.drawBitmap(canvasBackground, 0, 0, null);

        if (configSettings.isInfinite())
            drawInfiniteIndicator(canvas);
        else
            drawLoadingIndicator(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                angleModifier = 1;
                break;
            case MotionEvent.ACTION_DOWN:
                angleModifier = 3;
                break;
        }

        return true;
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

    float getArcAngle() {
        return arcAngle;
    }

    void setArcAngle(float aa) {
        arcAngle = aa;
    }

    private void calculateProgress(float value) {
        if (value > 0){
            if (value >= maxValue)
                value = maxValue;

            currentValue = value;

            float currentAngle = (360 / maxValue) * value;
            currentAngle += 270;
            recalculateItemCoordinate(currentAngle, bigRadius, single);

            float aa = (currentAngle - 270);
            LoaderAngleAnimation animation = new LoaderAngleAnimation(this, aa);
            animation.setDuration(300);
            this.startAnimation(animation);
        }
    }

    protected ItemCoordinate getItemCoordinate(float angleInDegrees, float radius, float pointRadius) {
        float x = getXCoordinate(angleInDegrees, radius);
        float y = getYCoordinate(angleInDegrees, radius);

        ItemCoordinate retValue = new ItemCoordinate();
        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);
        retValue.setRadius(pointRadius);

        return retValue;
    }

    protected void recalculateItemCoordinate(float angleInDegrees, float radius, ItemCoordinate itemCoordinate) {
        float x = getXCoordinate(angleInDegrees, radius);
        float y = getYCoordinate(angleInDegrees, radius);

        itemCoordinate.setX(x);
        itemCoordinate.setY(y);
        itemCoordinate.setAngle(angleInDegrees);
    }

    // region private methods

    private float getYCoordinate(float angleInDegrees, float radius) {
        return (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);
    }

    private float getXCoordinate(float angleInDegrees, float radius) {
        return (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
    }

    private void drawInfiniteIndicator(Canvas canvas) {
        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), item.getRadius(), bigPaint);
        }
        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);
        invalidate();
    }

    private void drawLoadingIndicator(Canvas canvas) {
        canvas.drawArc(rect, 630, arcAngle, false, singlePaintTransparent);

        for (ItemCoordinate item : outerItems) {
            canvas.drawCircle(item.getX(), item.getY(), singlePointRadius, bigPaint);
        }
        canvas.drawCircle(singleFixPoint.getX(), singleFixPoint.getY(), singlePointRadius, singlePaint);
        canvas.drawCircle(single.getX(), single.getY(), singlePointRadius, singlePaint);

        if (configSettings.isPercentageVisible()) {
            float v = (100 / maxValue) * currentValue;

            String formatString = decimalPlacesMap.get(configSettings.getPercentageDecimalPlaces());
            String text = String.format(formatString, v);
            canvas.drawText(text, textPosX, textPosY, textPaint);
        }
    }

    private void initializePoints() {
        int height = getHeight();
        int width = getWidth();
        bigRadius = height > width ? width /2 : height / 2;
        bigRadius = (float)(bigRadius * 0.75);
        float bigPointRadius = (float) (bigRadius * 0.15);

        layoutCenterX = getPaddingLeft() + width / 2;
        layoutCenterY = getPaddingTop() + height / 2;

        float slice = 360 / configSettings.getBigPointCount();
        for (int i = 0; i < configSettings.getBigPointCount(); i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(angle, bigRadius, bigPointRadius);
            outerItems[i] = itemCoordinate;
        }

        singleRadius = (float) (bigRadius * 0.80);
        singlePointRadius = (float) (singleRadius * 0.05);

        if (configSettings.isInfinite()) {
            single = getItemCoordinate(INITIAL_SMALL_POSITION, singleRadius, singlePointRadius);
            singleFixPoint = getItemCoordinate(INITIAL_SMALL_POSITION, singleRadius, singlePointRadius);
        } else {
            single = getItemCoordinate(INITIAL_SMALL_POSITION, bigRadius, singlePointRadius);
            singleFixPoint = getItemCoordinate(INITIAL_SMALL_POSITION, bigRadius, singlePointRadius);
        }

        bigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigPaint.setColor(configSettings.getBigPointColor());

        singlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        singlePaint.setColor(configSettings.getSmallPointColor());

        singlePaintTransparent = new Paint();
        singlePaintTransparent.setAntiAlias(true);
        singlePaintTransparent.setStyle(Paint.Style.STROKE);
        singlePaintTransparent.setStrokeWidth(singlePointRadius * 2);
        singlePaintTransparent.setColor(configSettings.getSmallPointColor());
        singlePaintTransparent.setAlpha(100);

        float textSize = (float) (bigRadius * 0.3);
        textPaint = new Paint();
        textPaint.setColor(configSettings.getSmallPointColor());
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(textSize);

        textPosX = layoutCenterX;
        textPosY = layoutCenterX - ((textPaint.descent() + textPaint.ascent()) / 2);

    }

    private void initializeCanvas() {
        Bitmap cb = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(cb);
        cv.drawColor(configSettings.getBackgroundColor());
        canvasBackground = getRoundedBitmap(cb, configSettings.getBackgroundShape());

        rect.set(layoutCenterX - bigRadius, layoutCenterY - bigRadius, layoutCenterX + bigRadius, layoutCenterY + bigRadius);
    }

    private void calculateInfiniteMoves() {
        for (int i = 0; i < configSettings.getBigPointCount(); i++) {
            float angle = outerItems[i].getAngle();
            angle += angleModifier;
            if (angle > 360)
                angle = angle - 360;

            recalculateItemCoordinate(angle, bigRadius, outerItems[i]);
        }

        float singleAngle = single.getAngle();
        singleAngle -= angleModifier;
        if (singleAngle < 0)
            singleAngle = singleAngle + 360;

        recalculateItemCoordinate(singleAngle, singleRadius, single);
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
