package com.example.android.cardclub;

// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable
{
    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

   // Stack of cards
    private CardStack[] foundations;

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    // Used for moving cards
    private Card activeCard;
    private int oldX, oldY, mTakenFrom;

    private int NUM_FOUNDATIONS = 2;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        int[] mResourceIDArray = new int[52];
        addResources(mResourceIDArray); // Add Bitmap resource IDs

        Deck mDeck = new Deck(context, mResourceIDArray); // Create the Deck of Cards
        mDeck.shuffleDeck();

        foundations = new CardStack[NUM_FOUNDATIONS];

        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            foundations[i] = new CardStack(i, ( (ScreenX/4) + (400*i) ), ScreenY/4);

            for(int j = 0; j < 5; j++)
            {
                foundations[i].addCard( mDeck.getCard(j) );
            }

            foundations[i].getTop().turnUp();
        }

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

            for(int i = 0; i < NUM_FOUNDATIONS; i++)
            {
                drawCardStack(foundations[i]);
            }

            if(activeCard != null)
                drawCard(activeCard);

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
                /*if(activeCard != null)
                {
                    for(int i = 0; i < NUM_FOUNDATIONS; i++)
                    {
                        if( foundations[i].getTop().getDetectCollision().contains( (int)motionEvent.getX(), (int)motionEvent.getY() ) ) // Check if on the top of a stack
                        {
                            if( isValidPlacement(foundations[i].getTop(), activeCard) )// If so -> check valid
                            {
                                foundations[i].addCard( activeCard ); // If so -> add card to stack
                                break; // Break For
                            }
                            else
                            {
                                activeCard.setCurrX(oldX);
                                activeCard.setCurrY(oldY);
                                break; // Break For
                            }

                        }

                        if( i == NUM_FOUNDATIONS-1 ) // If last chance
                        {
                            //else return card to stacks coordinates
                            activeCard.setCurrX(oldX);
                            activeCard.setCurrY(oldY);
                        }
                    } // For

                    activeCard.update();
                    foundations[mTakenFrom].addCard(activeCard); // Put the card back
                    activeCard = null;
                }*/
                break;
            case MotionEvent.ACTION_DOWN:
                for(int i = 0; i < NUM_FOUNDATIONS; i++)
                {
                    if( foundations[i].getTop().getDetectCollision().contains((int)motionEvent.getX(), (int)motionEvent.getY()) )
                    {
                        foundations[i].getTop().turnUp();

                        activeCard = foundations[i].removeTop();
                        mTakenFrom = i; // Where did we take the card from

                        //remember current stack's coordinates
                        oldX = activeCard.getCurrX();
                        oldY = activeCard.getCurrY();

                        break; // Break For
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if( activeCard != null )
                {
                    activeCard.setCurrX((int)motionEvent.getX());
                    activeCard.setCurrY((int)motionEvent.getY());
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

    public void drawCardStack(CardStack pCStack)
    {
        int size = pCStack.getSize();

        for( int i = 0; i < size; i++ )
        {
            drawCard(pCStack.getAt(i));
        }
    }

    public boolean isValidPlacement(Card topCard, Card currCard)
    {
        boolean retVal = false;

        if( (topCard.isRed() != currCard.isRed()) && (topCard.getValue() == currCard.getValue()+1) )
        {
            retVal = true;
        }

        return retVal;
    }

    // TODO: Move this to the Deck class
    public void addResources(int array[])
    {
        // Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/
        if( array.length >= 51 )
        {
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

}
