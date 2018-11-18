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

    // Used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder sHolder;

    // Used for moving cards
    private Card activeCard;
    private CardStack activeStack = null;
    private int mTakenFrom;

    private int NUM_FOUNDATIONS = 7;
    private CardStack[] foundations;

    private int NUM_PILES = 4;
    private CardStack[] piles;

    private CardStack tap, tapRunOff;

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
            foundations[i] = new CardStack(0, (ScreenX/50 + (200*i) ), ScreenY/4, 75, 2000, context); // ID: 0 == Normal Stack
            for(int j = 0; j < i+1; j++)
            {
                foundations[i].addCard( mDeck.dealCard() );
            }
            foundations[i].getTop().turnUp();
        }

        //Set up 4 Piles
        piles = new CardStack[NUM_PILES];
        for( int i = 0; i < NUM_PILES; i++ )
        {
            piles[i] = new CardStack(1, 200*i, 200, 0, 2000, context); // ID: 0 = Pile Stack
        }

        //Set up 1 Empty Pile
        tapRunOff = new CardStack(2, ScreenX-500, 200, 0, 2000, context);

        //Set up 1 Pile with rest of deck
        tap = new CardStack(3, ScreenX-300, 200, 0, 2000, context);
        while( mDeck.getCardsDelt() < mDeck.getSize() )
        {
            tap.addCard( mDeck.dealCard() );
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
            update();
            draw();
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

            for(int i = 0; i < NUM_PILES; i++)
            {
                drawCardStack( piles[i] );
            }

            drawCardStack( tap );
            drawCardStack( tapRunOff );

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
                if(activeStack != null)
                {
                    for(int i = 0; i < NUM_FOUNDATIONS; i++)
                    {
                        // Check if on empty stack
                        if( (foundations[i].isEmpty()) ) // No Cards on stack
                        {
                            if( foundations[i].getDetectCollision().contains( (int)motionEvent.getX(), (int)motionEvent.getY() ) && activeStack.getAt(0).getValue() == 13 )
                            {
                                foundations[i].addStack( activeStack );
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
                                    foundations[i].addStack( activeStack ); // If so -> add card to stack
                                    mTakenFrom = -1;
                                    break; // Break For
                                }
                            }
                        }
                    }

                    if( mTakenFrom >= 0 )
                    {
                        foundations[mTakenFrom].addStack(activeStack); // Put the card(s) back
                    }

                    activeCard = null;
                    activeStack = null;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                // Get tap location
                int clickX = (int) motionEvent.getX();
                int clickY = (int) motionEvent.getY();

                // Check all CardStacks
                if( !tapClicked(clickX, clickY) ) // Check the tap
                {
                    if( !runOffClicked(clickX, clickY) ) // Check tap runOff
                    {
                        if( !stacksClicked(clickX, clickY, NUM_PILES, piles) ) // Check the 4 piles
                        {
                            stacksClicked(clickX, clickY, NUM_FOUNDATIONS, foundations); // Check the foundations
                        }
                    }
                }
                break; // Break Switch
            case MotionEvent.ACTION_MOVE:
                if( activeStack != null  )
                {
                    activeStack.setLocation( (int)motionEvent.getX(), (int)motionEvent.getY() );
                }
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

    public boolean tapClicked(int pX, int pY)
    {
        boolean retVal = false;

        // Check the tap
        if( tap.getDetectCollision().contains(pX, pY ) ) // Get all the Cards from the tap runOff
        {
            if( tap.isEmpty() )
            {
                while (! tapRunOff.isEmpty())
                {
                    tapRunOff.getTop().turnDown();
                    tap.addCard( tapRunOff.removeTop() );
                }

            }
            else
            {
                tapRunOff.addCard( tap.removeTop() );
                tapRunOff.getTop().turnUp();
            }

            retVal = true;
        }

        return retVal;
    }

    public boolean runOffClicked(int pX, int pY)
    {
        boolean retVal = false;

        if( tapRunOff.getDetectCollision().contains(pX, pY) )
        {
            if( !tapRunOff.isEmpty() )
            {
                activeCard = tapRunOff.getTop();
                activeStack = tapRunOff.splitStack( activeCard, getContext() );
                mTakenFrom = -1;
            }

            retVal = true;
        }

        return retVal;
    }

    public boolean stacksClicked(int pX, int pY, int arraySize, CardStack[] stacks)
    {
        boolean retVal = false;

        for(int i = 0; i < arraySize; i++)
        {
            if( !stacks[i].isEmpty() ) // Make sure cardStack is not empty
            {
                for( int j = 0; j < stacks[i].getSize(); j++ ) // Find the card clicked on
                {
                    if (stacks[i].getAt(j).getDetectCollision().contains(pX, pY))
                    {
                        retVal = true;

                        if( stacks[i].getAt(j) == stacks[i].getTop() && !stacks[i].getTop().isFaceUp() )
                        {
                            stacks[i].getTop().turnUp(); // If it is the top Turn it Up
                            break; // Break For
                        }
                        else if( stacks[i].getAt(j).isFaceUp()  )
                        {
                            // Get the selected card for validation purposes
                            activeCard  = stacks[i].getAt(j);
                            activeStack = stacks[i].splitStack(activeCard, getContext()); //Get any Cards on top of the selected card

                            mTakenFrom = i; // Where did we take the card from
                            break;
                        }
                    }
                }
            }
        }

        return retVal;
    }





}
