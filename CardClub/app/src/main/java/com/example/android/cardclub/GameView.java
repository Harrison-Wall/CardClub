package com.example.android.cardclub;

// Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/
// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements Runnable
{
    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    // Card to draw
    private Card c1;

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        // Set up the card
        c1 = new Card(1, 1, false, ScreenX/4, ScreenY/4, R.drawable.c_2 , context);

        // Set up paint. surface etc
        sHolder = getHolder();
        paint = new Paint();
    }

    @Override
    public void run()
    {
        while (playing)
        {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }

    private void update()
    {
        //updating player position
        c1.update();

        //Detect collisions
        /*if(Rect.intersects(c1.getDetectCollision(), enemies[0].getDetectCollision() ) )
        {
            enemies[0].setX(-200);
        }
        */
    }

    private void draw()
    {
        //checking if surface is valid
        if (sHolder.getSurface().isValid())
        {
            //locking the canvas
            canvas = sHolder.lockCanvas();
            canvas.drawColor(Color.rgb(25,200,50));

            if( c1.isFaceUp() )
                canvas.drawBitmap(c1.getFaceMap(), c1.getCurrX(), c1.getCurrY(), paint);
            else
                canvas.drawBitmap(c1.getBackMap(), c1.getCurrX(), c1.getCurrY(), paint);


            //Unlocking the canvas
            sHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control()
    {
        try
        {
            gameThread.sleep(17);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        //setting the variable to false
        playing = false;

        try
        {
            //stopping the thread
            gameThread.join();
        }
        catch (InterruptedException e)
        {

        }
    }

    public void resume()
    {
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    private Point userCollisionDetection;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                userCollisionDetection = new Point((int)motionEvent.getX(), (int)motionEvent.getY() );

                if( c1.getDetectCollision().contains(userCollisionDetection.x, userCollisionDetection.y) ) // If the user touches a card
                {
                    c1.turnUp();
                }

                draw();

                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                break;
            case MotionEvent.ACTION_MOVE:
                // When user drags across screen
                break;
        }



        return true;
    }

}
