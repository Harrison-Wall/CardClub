package com.example.android.cardclub.testClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

import com.example.android.cardclub.testClasses.testDrawable;

public class CustomDrawableView extends View
{
    private ShapeDrawable mDrawable;
    private testDrawable mtD;

    public CustomDrawableView(Context context)
    {
        super(context);

        int x = 100;
        int y = 100;
        int width = 300;
        int height = 500;

        mtD = new testDrawable();

        mtD.setBounds(x, y, x + width, y + height);

        mDrawable = new ShapeDrawable(new OvalShape());
        // If the color isn't set, the shape uses black as the default.
        mDrawable.getPaint().setColor(0xff74AC23);
        // If the bounds aren't set, the shape can't be drawn.
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    protected void onDraw(Canvas canvas)
    {
        //mDrawable.draw(canvas);
        mtD.draw(canvas);
    }
}