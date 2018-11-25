package com.example.android.cardclub;

// Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SolitaireView extends SurfaceView implements Runnable
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
    private Deck mDeck;
    private Card activeCard;
    private CardStack activeStack = null;
    private int[] mTakenFrom = new int[2];

    // The Various stacks needed for solitaire
    private int NUM_FOUNDATIONS = 7;
    private CardStack[] foundations;

    private int NUM_PILES = 4;
    private CardStack[] piles;

    private CardStack tap, tapRunOff;

    public SolitaireView(Context context, int pScreenX, int pScreenY)
    {
        super(context);

        // Calculate spacing / layout
        int density = (int)Math.sqrt(pScreenX*pScreenY); // 2000
        int offAmount = pScreenY/25;                     // 100
        int topY = pScreenY/12;                          // 200
        int bottomY = pScreenY/4;                        // 640
        int xOffset = pScreenX/50;                       // 50

        foundations = new CardStack[NUM_FOUNDATIONS];
        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            foundations[i] = new CardStack(0, (xOffset + (200*i) ), bottomY, offAmount, density, context); // ID: 0 == Normal Stack
        }

        //Set up 4 Piles
        piles = new CardStack[NUM_PILES];
        for( int i = 0; i < NUM_PILES; i++ )
        {
            piles[i] = new CardStack(1, xOffset + (200*i), topY, 0, density, context); // ID: 0 = Pile Stack
        }

        //Set up 1 Empty Pile
        tapRunOff = new CardStack(2, pScreenX-500, topY, 0, density, context);

        //Set up 1 Pile with rest of deck
        tap = new CardStack(3, pScreenX-300, topY, 0, density, context);

        fillBoard(context);

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





    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                if(activeStack != null)
                {
                    int clickX = (int) motionEvent.getX();
                    int clickY = (int) motionEvent.getY();

                    if( !placeCards( clickX, clickY, NUM_PILES, piles) ) // Check the piles
                    {
                        placeCards( clickX,  clickY,  NUM_FOUNDATIONS,  foundations); // Check the Foundations
                    }

                    // Put Card(s) Back if needed
                    if( mTakenFrom[0] >= 0 )
                    {
                        switch( mTakenFrom[1] )
                        {
                            case 0:
                                foundations[ mTakenFrom[0] ].addStack(activeStack); // Put the card(s) back
                                break;
                            case 1:
                                piles[ mTakenFrom[0] ].addStack(activeStack);
                                break;
                            case 2:
                                tapRunOff.addStack(activeStack);
                                break;
                        }

                        // No Longer Valid
                        mTakenFrom[0] = -1;
                        mTakenFrom[1] = -1;
                    }

                    activeCard = null;
                    activeStack = null;

                    if( hasWon() ) // If all Piles are Full
                    {
                        showAlert();
                    }
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
                        if( !stacksClicked(clickX, clickY, NUM_PILES, piles) ) // Check the piles
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


    public void fillBoard(Context context)
    {
        mDeck = new Deck(context); // Create the Deck of Cards
        mDeck.shuffleDeck();

        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            for(int j = 0; j < i+1; j++)
            {
                foundations[i].addCard( mDeck.dealCard() );
            }
            foundations[i].getTop().turnUp();
        }

        while( mDeck.getCardsDelt() < mDeck.getSize() )
        {
            tap.addCard( mDeck.dealCard() );
        }

        return;
    }

    public void clearBoard()
    {
        for(int i = 0; i < NUM_PILES; i++)
        {
            piles[i].clearStack();
        }

        for(int i = 0; i < NUM_FOUNDATIONS; i++)
        {
            foundations[i].clearStack();
        }

        tap.clearStack();
        tapRunOff.clearStack();

        mDeck.sortDeck();

        return;
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


    public void showAlert()
    {
        // Show and Alert Message https://www.tutorialspoint.com/android/android_alert_dialoges.html
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setMessage("You Won!");

        alertBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearBoard();
                fillBoard( getContext() );
            }
        });

        AlertDialog myAlert = alertBuilder.create();
        myAlert.show();

        return;
    }

    public boolean isValidPlacement( Card topCard, Card currCard, int pID )
    {
        boolean retVal = false;

        if( pID == 0 ) // Foundations
        {
            if( ( topCard.isRed() != currCard.isRed() ) && ( topCard.getValue() == currCard.getValue()+1 ) )
            {
                retVal = true;
            }
        }
        else if( pID == 1 ) // Piles
        {
            if( ( topCard.getSuit() == currCard.getSuit() ) && ( topCard.getValue() == currCard.getValue()-1 ) )
            {
                retVal = true;
            }
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
                while ( !tapRunOff.isEmpty() )
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
                mTakenFrom[0] = 1;
                mTakenFrom[1] = tapRunOff.getID(); // Taken from the runOff
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
                // If it is a pile, take it from the top
                if( stacks[i].getID() == 1 )
                {
                    if( stacks[i].getDetectCollision().contains( pX, pY ) )
                    {
                        activeCard  = stacks[i].getTop();
                        activeStack = stacks[i].splitStack(activeCard, getContext()); //Get any Cards on top of the selected card

                        mTakenFrom[0] = i; // Where did we take the card from
                        mTakenFrom[1] = stacks[i].getID();
                        break;
                    }
                }
                else
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

                                mTakenFrom[0] = i; // Where did we take the card from
                                mTakenFrom[1] = stacks[i].getID();
                                break;
                            }
                        }
                    }
                }
            }
        }

        return retVal;
    }



    public boolean placeCards(int pX, int pY, int arraySize, CardStack[] stacks)
    {
        boolean retVal = false;

        // Check the Foundations
        for(int i = 0; i < arraySize; i++)
        {
            // Check if on empty stack
            if( (stacks[i].isEmpty()) ) // No Cards on stack
            {
                if( stacks[i].getDetectCollision().contains( pX, pY ) ) // If you are touching thr stack
                {
                    if( stacks[i].getID() == 0 && activeStack.getAt(0).getValue() == 13 ) // If it is a foundation and active card is a king
                    {
                        retVal = true;

                        stacks[i].addStack( activeStack );
                        mTakenFrom[0] = -1;
                        mTakenFrom[1] = -1;
                        break; // Break For
                    }
                    else if( stacks[i].getID() == 1 && activeStack.getAt(0).getValue() == 1 ) // If it is a Pile and active card is an Ace
                    {
                        if( activeStack.getSize() == 1 ) // Can only add one card at a time to a pile
                        {
                            retVal = true;

                            stacks[i].addStack( activeStack );
                            mTakenFrom[0] = -1;
                            mTakenFrom[1] = -1;
                            break; // Break For
                        }
                    }

                }
            }
            else // Cards are on the stack
            {
                if( stacks[i].getTop().getDetectCollision().contains( pX, pY ) ) // Check if on the top of a stack
                {
                    if( isValidPlacement(stacks[i].getTop(), activeCard, stacks[i].getID() ) )// If so -> check valid
                    {
                        if( stacks[i].getID() == 0 || activeStack.getSize() == 1 ) // If it is a pile only one card can go on it
                        {
                            retVal = true;

                            stacks[i].addStack( activeStack ); // If so -> add card to stack
                            mTakenFrom[0] = -1;
                            mTakenFrom[1] = -1;
                            break; // Break For
                        }
                    }
                }
            }
        }

        return retVal;
    }




    public boolean hasWon()
    {
        boolean retVal = true;

        for( int i = 0; i < NUM_PILES; i++ )
        {
            if( piles[i].getSize() != 13 )
            {
                retVal = false;
                break;
            }
        }

        return retVal;
    }


}
