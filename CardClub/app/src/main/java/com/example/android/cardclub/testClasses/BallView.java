package com.example.android.cardclub.testClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.android.cardclub.R;

/*
        // Add this to the activity's onCreate method
        int w=getWindowManager().getDefaultDisplay().getWidth()-25;
        int h=getWindowManager().getDefaultDisplay().getHeight()-25;

        SolitaireView gView = new SolitaireView(this, w, h);
        setContentView(gView);
 */

public class BallView extends SurfaceView implements SurfaceHolder.Callback
{
    private Bitmap bitmap ;
    private int x=20,y=20;int width,height;

    public BallView(Context context, int w, int h)
    {
        super(context);

        width = w;
        height = h;
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLUE);//To make background
        super.onDraw(canvas);

        BitmapFactory.Options myop = new BitmapFactory.Options();

        myop.inDensity = 450;

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c_2 , myop);

        canvas.drawBitmap(bitmap,x-(bitmap.getWidth()/8),y-(bitmap.getHeight()/8),null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        x=(int)event.getX();
        y=(int)event.getY();

        updateBall();

        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
    }

    private void updateBall()
    {
        Canvas canvas = null;

        try
        {
            canvas = getHolder().lockCanvas(null);
            synchronized (getHolder())
            {
                this.draw(canvas);
            }
        }
        finally
        {
            if (canvas != null)
            {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}