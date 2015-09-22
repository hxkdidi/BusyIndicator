package com.silverforge.controls.painters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.silverforge.controls.model.ClipShape;

import lombok.Getter;

public class CanvasPainter {

    @Getter
    private Paint bigPaint;

    @Getter
    private Paint singlePaint;

    @Getter
    private Paint singlePaintTransparent;

    @Getter
    private Paint textPaint;

    public void initializePaints(int outerPointColor, int innerPointColor, float outerpointRadius, float innerpointRadius) {

        float strokeWidth = innerpointRadius * 2;

        bigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigPaint.setColor(outerPointColor);

        singlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        singlePaint.setColor(innerPointColor);

        singlePaintTransparent = new Paint();
        singlePaintTransparent.setAntiAlias(true);
        singlePaintTransparent.setStyle(Paint.Style.STROKE);
        singlePaintTransparent.setStrokeWidth(strokeWidth);
        singlePaintTransparent.setColor(innerPointColor);
        singlePaintTransparent.setAlpha(100);

        float textSize = (float) (outerpointRadius * 0.3);
        textPaint = new Paint();
        textPaint.setColor(innerPointColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(textSize);
    }


    public Bitmap getRoundedBitmap(Bitmap bitmap, ClipShape clipShape) {
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
}
