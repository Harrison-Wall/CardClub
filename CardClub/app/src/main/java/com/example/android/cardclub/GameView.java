package com.example.android.cardclub;

// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Stack;

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
    private CardStack activeStack = null;
    private int mTakenFrom;

    private int NUM_FOUNDATIONS = 5;

    //Class constructor
    public GameView(Context context, int ScreenX, int ScreenY)
    {
        super(context);

        Deck mDeck = new Deck(context); // Create the Deck of Cards
        mDeck.shuffleDeck();

        // Set up 7 Foundations
        foundations = new CardStack[NUM_FOUNDATIONS];
        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            foundations[i] = new CardStack(i, (ScreenX/30 + (200*i) ), ScreenY/4);
            foundations[i].setBackMap(context);
            for(int j = 0; j < i+1; j++)
            {
                foundations[i].addCard( mDeck.dealCard() );
            }
            //foundations[i].getTop().turnUp();
        }

        //Set up 4 Piles


        //Set up 1 Empty Pile

        //Set up 1 Pile with rest of deck

        // Set up paint. surface etc
        sHolder = getHolder();
        paint = new Paint();
    }

    @Override
    public void run()
    {
        draw();

        while (playing)
        {
            update();
            //draw();
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

            for( int i = 0; i < NUM_FOUNDATIONS; i++ )
            {
                drawCardStack( foundations[i] );
            }

            if(activeCard != null)
                drawCard(activeCard);

            if( activeStack != null )
                drawCardStack(activeStack);

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
                if(activeCard != null)
                {
                    for(int i = 0; i < NUM_FOUNDATIONS; i++)
                    {
                        // Check if on empty stack
                        if( (foundations[i].isEmpty()) ) // No Cards on stack
                        {
                            if( foundations[i].getDetectCollision().contains( (int)motionEvent.getX(), (int)motionEvent.getY() ) )
                            {
                                foundations[i].addCard( activeCard );
                                mTakenFrom = -1;
                                break; // Break For
                            }
                        }
                        else
                        {
                            if( foundations[i].getTop().getDetectCollision().contains( (int)motionEvent.getX(), (int)motionEvent.getY() ) ) // Check if on the top of a stack
                            {
                                if( isValidPlacement(foundations[i].getTop(), activeCard) )// If so -> check valid
                                {
                                    foundations[i].addCard( activeCard ); // If so -> add card to stack
                                    mTakenFrom = -1;
                                    break; // Break For
                                }
                            }
                        }

                    } // For

                    if( mTakenFrom >= 0 )
                    {
                        foundations[mTakenFrom].addCard(activeCard); // Put the card back
                    }
                    activeCard = null;
                }
                draw();
                break;
            case MotionEvent.ACTION_DOWN:
                for(int i = 0; i < NUM_FOUNDATIONS; i++)
                {
                    if( !foundations[i].isEmpty() ) // Make sure cardStack is not empty
                    {
                        for( int j = 0; j < foundations[i].getSize(); j++ ) // Find the card clicked on
                        {
                            if (foundations[i].getAt(j).getDetectCollision().contains((int) motionEvent.getX(), (int) motionEvent.getY()))
                            {
                                if( foundations[i].getAt(j) == foundations[i].getTop() && !foundations[i].getTop().isFaceUp() )
                                {
                                    foundations[i].getTop().turnUp(); // If it is the top Turn it Up
                                    break; // Break For
                                }
                                else if( foundations[i].getAt(j).isFaceUp()  )
                                {
                                    // Get the selected card for validation purposes
                                    activeCard = foundations[i].getAt(j);

                                    activeStack = new CardStack(-1, activeCard.getX(), activeCard.getY());
                                    activeStack.setBackMap( getContext() );

                                    mTakenFrom = i; // Where did we take the card from
                                }
                                else
                                {
                                    break; // Break For
                                }
                            }

                            if( activeCard != null && activeStack != null ) // Get all Cards Below it as a temp Stack
                            {
                                activeStack.addCard( foundations[i].removeAt(j) );
                            }
                        }
                        //break;
                    }
                }
                draw();
                break; // Break Switch
            case MotionEvent.ACTION_MOVE:
                if( activeStack != null  )
                {
                    activeStack.setLocation( (int)motionEvent.getX(), (int)motionEvent.getY() );
                }
                draw();
                break;
        }
        return true;
    }

    public void drawCard( Card pCard )
    {
        if( pCard.isFaceUp() )
            canvas.drawBitmap( pCard.getFaceMap(), pCard.getX(), pCard.getY(), paint );
        else
            canvas.drawBitmap( pCard.getBackMap(), pCard.getX(), pCard.getY(), paint );
    }

    public void drawCardStack( CardStack pCStack )
    {
        canvas.drawBitmap( pCStack.getStackMap(), pCStack.getX(), pCStack.getY(), paint);

        for( int k = 0; k < pCStack.getSize(); k++ )
        {
            drawCard( pCStack.getAt(k) );
        }
    }

    public boolean isValidPlacement( Card topCard, Card currCard )
    {
        boolean retVal = false;

        if( ( topCard.isRed() != currCard.isRed() ) && ( topCard.getValue() == currCard.getValue()+1 ) )
        {
            retVal = true;
        }

        return retVal;
    }
}
