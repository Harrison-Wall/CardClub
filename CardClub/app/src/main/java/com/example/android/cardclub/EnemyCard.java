package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class EnemyCard
{
    private Bitmap bmap;
    private int mSpeed = 1, x, y, maxX, maxY, minX, minY;

    private Rect DetectCollision;

    public EnemyCard(Context context, int pX, int pY)
    {
        maxX = pX;
        maxY = pY;
        bmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_back);

        minX = 0;
        minY = 0;

        // Enemy speed is random
        Random generator = new Random();
        mSpeed = generator.nextInt(6) + 10;

        // Enemy location is random
        x = pX;
        y = generator.nextInt(maxY) - bmap.getHeight();

        //Set up detection git box
        DetectCollision = new Rect(x, y, bmap.getHeight(), bmap.getWidth());

    }

    public void update(int pSpeed)
    {
        // Decrease coordinate so enemy moves
        x -= pSpeed;
        x -= mSpeed;

        if( x < minX - bmap.getHeight() ) // If it reashes the ledt edge
        {
            // Move it back to the right
            Random generator = new Random();
            mSpeed = generator.nextInt(6) + 10;

            // Enemy location is random
            x = maxX;
            y = generator.nextInt(maxY) - bmap.getHeight();
        }
    }

    public Bitmap getBmap()
    {
        return  bmap;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getSpeed()
    {
        return mSpeed;
    }
}
