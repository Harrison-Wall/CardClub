package com.example.android.cardclub;

// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
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

        Deck mDeck = new Deck(context); // Create the Deck of Cards
        mDeck.shuffleDeck();

        foundations = new CardStack[NUM_FOUNDATIONS];

        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            foundations[i] = new CardStack(i, ( (ScreenX/4) + (350*i) ), ScreenY/4);

            for(int j = 0; j < 1; j++)
            {
                foundations[i].addCard( mDeck.dealCard() );
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

            for( int i = 0; i < NUM_FOUNDATIONS; i++ )
            {
                drawCardStack( foundations[i] );
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
                if(activeCard != null)
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
                }
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

    public void drawCard( Card pCard )
    {
        if( pCard.isFaceUp() )
            canvas.drawBitmap( pCard.getFaceMap(), pCard.getCurrX(), pCard.getCurrY(), paint );
        else
            canvas.drawBitmap( pCard.getBackMap(), pCard.getCurrX(), pCard.getCurrY(), paint );
    }

    public void drawCardStack( CardStack pCStack )
    {
        int size = pCStack.getSize();

        for( int k = 0; k < size; k++ )
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
