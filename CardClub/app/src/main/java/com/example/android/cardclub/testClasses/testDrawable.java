package com.example.android.cardclub.testClasses;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class testDrawable extends Drawable
{
    private final Paint mRedPaint;

    testDrawable()
    {
        mRedPaint = new Paint();
        mRedPaint.setARGB(255, 255, 0, 0);
    }

    @Override
    public void draw(Canvas canvas)
    {
        // Get the drawable's bounds
        int width = getBounds().width();
        int height = getBounds().height();
        float radius = Math.min(width, height) / 2;

        // Draw a red circle in the center
        canvas.drawCircle(width/2, height/2, radius, mRedPaint);

    }

    @Override
    public void setAlpha(int alpha)
    {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {
        // This method is required
    }

    @Override
    public int getOpacity()
    {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }

}
