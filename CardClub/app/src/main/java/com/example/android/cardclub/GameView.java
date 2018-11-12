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
    //private Card c1, c2;

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        // Set up the card
        //c1 = new Card(1, 1, false, ScreenX/4, ScreenY/4, R.drawable.c_2 , context);
        //c2 = new Card(1, 1, false, ScreenX/4+500, ScreenY/4, R.drawable.h_a , context);

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
        //c1.update();
        //c2.update();
    }

    private void draw()
    {
        //checking if surface is valid
        if (sHolder.getSurface().isValid())
        {
            //locking the canvas
            canvas = sHolder.lockCanvas();
            canvas.drawColor(Color.rgb(25,200,50));

            if( activeCard == null )
            {
                /*if( c1.isFaceUp() )
                    canvas.drawBitmap(c1.getFaceMap(), c1.getCurrX(), c1.getCurrY(), paint);
                else
                    canvas.drawBitmap(c1.getBackMap(), c1.getCurrX(), c1.getCurrY(), paint);

                if( c2.isFaceUp() )
                    canvas.drawBitmap(c2.getFaceMap(), c2.getCurrX(), c2.getCurrY(), paint);
                else
                    canvas.drawBitmap(c2.getBackMap(), c2.getCurrX(), c2.getCurrY(), paint);*/
            }
            else
            {
                canvas.drawBitmap(activeCard.getFaceMap(), activeCard.getCurrX(), activeCard.getCurrY(), paint);

                /*if( activeCard.getID() == c1.getID() )
                {
                    if( c2.isFaceUp() )
                        canvas.drawBitmap(c2.getFaceMap(), c2.getCurrX(), c2.getCurrY(), paint);
                    else
                        canvas.drawBitmap(c2.getBackMap(), c2.getCurrX(), c2.getCurrY(), paint);
                }
                else
                {
                    if( c1.isFaceUp() )
                        canvas.drawBitmap(c1.getFaceMap(), c1.getCurrX(), c1.getCurrY(), paint);
                    else
                        canvas.drawBitmap(c1.getBackMap(), c1.getCurrX(), c1.getCurrY(), paint);
                }*/
            }

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
    private Card activeCard;
    int x, y;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                activeCard = null;
                draw();
                break;
            case MotionEvent.ACTION_DOWN:
                userCollisionDetection = new Point((int)motionEvent.getX(), (int)motionEvent.getY() );
                /*if( c1.getDetectCollision().contains(userCollisionDetection.x, userCollisionDetection.y) ) // If the user touches a card
                {
                    c1.turnUp();
                    activeCard = c1;
                }
                else if( c2.getDetectCollision().contains(userCollisionDetection.x, userCollisionDetection.y) ) // If the user touches a card
                {
                    c2.turnUp();
                    activeCard = c2;
                }*/

                break;
            case MotionEvent.ACTION_MOVE:
                // When user drags across screen
                x = (int)motionEvent.getX();
                y = (int)motionEvent.getY();

                if( activeCard != null )
                {
                    activeCard.setCurrX(x);
                    activeCard.setCurrY(y);

                    draw();
                }

                break;
        }

        return true;
    }

}
