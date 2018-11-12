package com.example.android.cardclub;

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

    private int[] mRedourceIDArray; // Resource IDs of Card images
    private Deck mDeck;             // Array list of Cards
    private CardStack mCStack;      // Stack of cards

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    // Used for moving cards
    private Card activeCard;
    int x, y;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        mRedourceIDArray = new int[52];
        addReources(mRedourceIDArray); // Add Bitmap resource IDs

        mDeck = new Deck(context, mRedourceIDArray); // Create the Deck of Cards
        mDeck.shuffleDeck();

        mCStack = new CardStack(0,ScreenX/4,ScreenY/4); // Create a Stack of Cards

        mCStack.addCard( mDeck.getCard(0) );
        mCStack.addCard( mDeck.getCard(1) );
        mCStack.addCard( mDeck.getCard(3) );
        mCStack.addCard( mDeck.getCard(4) );

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
        //updating card position + hit box
        if(activeCard != null)
            activeCard.update();
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
                for( int i = 0; i < mCStack.getSize(); i++ )
                {
                    drawCard(mCStack.getAt(i));
                }
            }
            else
            {
                for( int i = 0; i < mCStack.getSize(); i++ )
                {
                    if( activeCard.getID() == mCStack.getAt(i).getID() )
                        drawCard(activeCard);
                    else
                        drawCard(mCStack.getAt(i));
                }
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
                Point userCollisionDetection = new Point((int)motionEvent.getX(), (int)motionEvent.getY() );

                if( mCStack.getTop().getDetectCollision().contains(userCollisionDetection.x, userCollisionDetection.y) )
                {
                    mCStack.getTop().turnUp();

                    activeCard = mCStack.getTop();
                }
                draw();
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

    public void drawCard(Card pCard)
    {
        if( pCard.isFaceUp() )
            canvas.drawBitmap(pCard.getFaceMap(), pCard.getCurrX(), pCard.getCurrY(), paint);
        else
            canvas.drawBitmap(pCard.getBackMap(), pCard.getCurrX(), pCard.getCurrY(), paint);
    }

    public void addReources(int array[])
    {
        // Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/

        // Clubs
        array[0] = R.drawable.c_a;
        array[1] = R.drawable.c_2;
        array[2] = R.drawable.c_3;
        array[3] = R.drawable.c_4;
        array[4] = R.drawable.c_5;
        array[5] = R.drawable.c_6;
        array[6] = R.drawable.c_7;
        array[7] = R.drawable.c_8;
        array[8] = R.drawable.c_9;
        array[9] = R.drawable.c_10;
        array[10] = R.drawable.c_j;
        array[11] = R.drawable.c_q;
        array[12] = R.drawable.c_k;

        // Diamonds
        array[13] = R.drawable.d_a;
        array[14] = R.drawable.d_2;
        array[15] = R.drawable.d_3;
        array[16] = R.drawable.d_4;
        array[17] = R.drawable.d_5;
        array[18] = R.drawable.d_6;
        array[19] = R.drawable.d_7;
        array[20] = R.drawable.d_8;
        array[21] = R.drawable.d_9;
        array[22] = R.drawable.d_10;
        array[23] = R.drawable.d_j;
        array[24] = R.drawable.d_q;
        array[25] = R.drawable.d_k;

        // Hearts
        array[26] = R.drawable.h_a;
        array[27] = R.drawable.h_2;
        array[28] = R.drawable.h_3;
        array[29] = R.drawable.h_4;
        array[30] = R.drawable.h_5;
        array[31] = R.drawable.h_6;
        array[32] = R.drawable.h_7;
        array[33] = R.drawable.h_8;
        array[34] = R.drawable.h_9;
        array[35] = R.drawable.h_10;
        array[36] = R.drawable.h_j;
        array[37] = R.drawable.h_q;
        array[38] = R.drawable.h_k;

        // Spades
        array[39] = R.drawable.s_a;
        array[40] = R.drawable.s_2;
        array[41] = R.drawable.s_3;
        array[42] = R.drawable.s_4;
        array[43] = R.drawable.s_5;
        array[44] = R.drawable.s_6;
        array[45] = R.drawable.s_7;
        array[46] = R.drawable.s_8;
        array[47] = R.drawable.s_9;
        array[48] = R.drawable.s_10;
        array[49] = R.drawable.s_j;
        array[50] = R.drawable.s_q;
        array[51] = R.drawable.s_k;

    }

}
