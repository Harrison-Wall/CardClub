package com.example.android.cardclub;

// Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/
// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.android.cardclub.testClasses.EnemyCard;

public class GameView extends SurfaceView implements Runnable
{
    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    // Card to draw
    private Card c1;

    // Enemy Card
    //private EnemyCard[] enemies;

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        // Set up the card
        c1 = new Card(1, 1, false, ScreenX/2, ScreenY/2, 100, 50, context);

        /*enemies = new EnemyCard[3];
        enemies[0] = new EnemyCard(context, ScreenX, ScreenY);
        enemies[1] = new EnemyCard(context, ScreenX, ScreenY);
        enemies[2] = new EnemyCard(context, ScreenX, ScreenY);*/

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

        //update enemy position
        //enemies[0].update((c1.getSpeed()));
        //enemies[1].update((c1.getSpeed()));
        //enemies[2].update((c1.getSpeed()));

        //Detect collisions
        /*if(Rect.intersects(c1.getDetectCollision(), enemies[0].getDetectCollision() ) )
        {
            enemies[0].setX(-200);
        }
        if(Rect.intersects(c1.getDetectCollision(), enemies[1].getDetectCollision() ) )
        {
            enemies[1].setX(-200);
        }
        if(Rect.intersects(c1.getDetectCollision(), enemies[2].getDetectCollision() ) )
        {
            enemies[2].setX(-200);
        }*/
    }

    private void draw()
    {
        //checking if surface is valid
        if (sHolder.getSurface().isValid())
        {
            //locking the canvas
            canvas = sHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLUE);
            //Drawing the player
            canvas.drawBitmap(c1.getFaceMap(), c1.getCurrX(), c1.getCurrY(), paint);

            //Draw enemies
            //canvas.drawBitmap(enemies[0].getBmap(), enemies[0].getX(), enemies[0].getY(), paint);
            //canvas.drawBitmap(enemies[1].getBmap(), enemies[1].getX(), enemies[1].getY(), paint);
            //canvas.drawBitmap(enemies[2].getBmap(), enemies[2].getX(), enemies[2].getY(), paint);

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



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //c1.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //c1.setBoosting();
                break;
        }
        return true;
    }

}
