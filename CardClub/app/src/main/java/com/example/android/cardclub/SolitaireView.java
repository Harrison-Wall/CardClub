/*
 * Harrison Wall
 * 2018
 * Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/
 */

package com.example.android.cardclub;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private Card activeCard;
    private CardStack activeStack = null;
    private int[] mTakenFrom = new int[2]; // used to determine where cards were taken from, and what kind of stack it was

    private Deck mDeck; // The deck

    // The Various stacks needed for solitaire
    private int NUM_FOUNDATIONS = 7;
    private CardStack[] foundations;

    private int NUM_PILES = 4;
    private CardStack[] piles;

    private CardStack tap, tapRunOff;

    private int density; // How large to make the cards

    public SolitaireView(Context context, int pScreenX, int pScreenY, DisplayMetrics dm)
    {
        super(context);

        density =  (int) dm.density;

        // Calculate spacing / layout
        density *= (1660000 / pScreenY); // 16600000 Was just the number I got by adjusting the density to get it to fit, then calculating back from that using the screen dimensions
        int offAmount      = pScreenY/25;
        int topY           = pScreenY/12;
        int bottomY        = pScreenY/4;
        int xOffset        = pScreenX/50;
        int cardGap        = pScreenX/7;
        int tapRunOffGap   = pScreenX/3;
        int tapXGap        = pScreenX/5;

        // Create the foundations
        foundations = new CardStack[NUM_FOUNDATIONS];
        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            foundations[i] = new CardStack(0, (xOffset + (cardGap*i) ), bottomY, offAmount, density, context); // ID: 0 == Normal Stack
        }

        //Set up 4 Piles
        piles = new CardStack[NUM_PILES];
        for( int i = 0; i < NUM_PILES; i++ )
        {
            piles[i] = new CardStack(1, xOffset + (cardGap*i), topY, 0, density, context); // ID: 0 = Pile Stack
        }

        //Set up 1 Empty Pile
        tapRunOff = new CardStack(2, pScreenX-tapRunOffGap, topY, 0, density, context);

        //Set up 1 Pile with rest of deck
        tap = new CardStack(3, pScreenX-tapXGap, topY, 0, density, context);

        // Add the cards where they are needed ( May change depending on game set up)
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

    // Update the active cards hitbox / location
    private void update()
    {

        if(activeCard != null)
            activeCard.update();
    }

    // Delays the frame rate
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

    // User leaves the app
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
            Log.d("Pause()", "Exception in gameThread.join()");
        }
    }

    // app has focus again
    public void resume()
    {
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // draw cards and background
    private void draw()
    {
        //checking if surface is valid
        if (sHolder.getSurface().isValid())
        {
            //locking the canvas
            canvas = sHolder.lockCanvas();

            // background
            canvas.drawColor(Color.rgb(25,200,50));

            // Draw the foundations
            for( int i = 0; i < NUM_FOUNDATIONS; i++ )
            {
                drawCardStack( foundations[i] );
            }

            // draw the piles
            for(int i = 0; i < NUM_PILES; i++)
            {
                drawCardStack( piles[i] );
            }

            // draw rest of the stacks
            drawCardStack( tap );
            drawCardStack( tapRunOff );

            // Only draw if the card/stack is in use
            if(activeCard != null)
                drawCard(activeCard);

            if( activeStack != null )
                drawCardStack(activeStack);

            //Unlocking the canvas
            sHolder.unlockCanvasAndPost(canvas);
        }
    }

    // If the game is being played
    public boolean isRunning()
    {
        return playing;
    }


    // User touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            // user lifts finger off the screen
            case MotionEvent.ACTION_UP:
                if(activeStack != null)
                {
                    // Get location of lift
                    int clickX = (int) motionEvent.getX();
                    int clickY = (int) motionEvent.getY();

                    // Add cards if lifted on a stack
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
                                foundations[ mTakenFrom[0] ].addStack(activeStack);
                                break;
                            case 1:
                                piles[ mTakenFrom[0] ].addStack(activeStack);
                                break;
                            case 2:
                                tapRunOff.addStack(activeStack);
                                break;
                        }

                        // No Longer needed
                        mTakenFrom[0] = -1;
                        mTakenFrom[1] = -1;
                    }

                    // No longer needed
                    activeCard = null;
                    activeStack = null;

                    if( hasWon() ) // If all Piles are Full
                    {
                        showAlert(); // Game Over Screen
                    }
                }
                break;

                // If the user touched the screen
            case MotionEvent.ACTION_DOWN:
                // Get tap location
                int clickX = (int) motionEvent.getX();
                int clickY = (int) motionEvent.getY();

                // Check all CardStacks for touch
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

                // User has finger down, is moving
            case MotionEvent.ACTION_MOVE:
                if( activeStack != null  )
                {
                    // While he is holding a cardStack, move its position to the touch
                    activeStack.setLocation( (int)motionEvent.getX(), (int)motionEvent.getY() );
                }
                break;
        }
        return true;
    }

    // Deal out cards to the various cardStacks
    public void fillBoard(Context context)
    {
        mDeck = new Deck(context, density); // Create the Deck of Cards
        mDeck.shuffleDeck();

        // Deal increasing to foundations
        for( int i = 0; i < NUM_FOUNDATIONS; i++ )
        {
            for(int j = 0; j < i+1; j++) // Each successive foundation gets another card than the last
            {
                foundations[i].addCard( mDeck.dealCard() );
            }
            foundations[i].getTop().turnUp();
        }

        // Tap gets the rest
        while( mDeck.getCardsDelt() < mDeck.getSize() )
        {
            tap.addCard( mDeck.dealCard() );
        }

        return;
    }

    // Remove all cards from the board
    public void clearBoard()
    {
        // Clear piles
        for(int i = 0; i < NUM_PILES; i++)
        {
            piles[i].clearStack();
        }

        // clear foundations
        for(int i = 0; i < NUM_FOUNDATIONS; i++)
        {
            foundations[i].clearStack();
        }

        // Clear tap and runOff
        tap.clearStack();
        tapRunOff.clearStack();

        // Put deck back in order
        mDeck.sortDeck();

        return;
    }

    // Draw a cards bitmap to the canvas
    public void drawCard( Card pCard )
    {
        // Can draw either the cardFace or cardBack when appropriate
        if( pCard.isFaceUp() )
            canvas.drawBitmap( pCard.getFaceMap(), pCard.getX(), pCard.getY(), paint );
        else
            canvas.drawBitmap( pCard.getBackMap(), pCard.getX(), pCard.getY(), paint );
    }

    // Draw a cardStack of cards to the canvas
    public void drawCardStack( CardStack pCStack )
    {
        // Draw the stacks bitmap (used to tell where a cardStack space is if no cards)
        canvas.drawBitmap( pCStack.getStackMap(), pCStack.getX(), pCStack.getY(), paint);

        // Draw all cards in the cardStack
        for( int k = 0; k < pCStack.getSize(); k++ )
        {
            drawCard( pCStack.getAt(k) );
        }
    }

    // Game Over pop-up
    public void showAlert()
    {
        // Show and Alert Message https://www.tutorialspoint.com/android/android_alert_dialoges.html
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Game Over");
        alertBuilder.setMessage("You Won!");
        alertBuilder.setCancelable(false);

        // New Game should remove all cards, reshuffle and reDeal
        alertBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearBoard();
                fillBoard( getContext() );
            }
        });

        // Could not get a button back to the Home screen to work properly

        // Make and Display alert
        AlertDialog myAlert = alertBuilder.create();
        myAlert.show();

        return;
    }

    // Compares two cards depending on the stackID pID, returns true if curr can be placed on top
    public boolean isValidPlacement( Card topCard, Card currCard, int pID )
    {
        boolean retVal = false;

        if( pID == 0 ) // Foundations
        {
            // Have to be different colors, top one less than curr
            if( ( topCard.isRed() != currCard.isRed() ) && ( topCard.getValue() == currCard.getValue()+1 ) )
            {
                retVal = true;
            }
        }
        else if( pID == 1 ) // Piles
        {
            // Have to be same suit, top one more than curr
            if( ( topCard.getSuit() == currCard.getSuit() ) && ( topCard.getValue() == currCard.getValue()-1 ) )
            {
                retVal = true;
            }
        }

        return retVal;
    }

    // true if pX and pY are in the tap's collision detection
    public boolean tapClicked(int pX, int pY)
    {
        boolean retVal = false;

        // Check collision
        if( tap.getDetectCollision().contains(pX, pY ) )
        {
            // Get all the Cards from the tap runOff if empty
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
                // Remove card from tap, add to tapRunOff
                tapRunOff.addCard( tap.removeTop() );
                tapRunOff.getTop().turnUp();
            }

            retVal = true;
        }

        return retVal;
    }

    // true if pX and pY are in the tap runOff's collision detection
    public boolean runOffClicked(int pX, int pY)
    {
        boolean retVal = false;

        // Check collision
        if( tapRunOff.getDetectCollision().contains(pX, pY) )
        {
            // Remove top from runOff
            if( !tapRunOff.isEmpty() )
            {
                activeCard = tapRunOff.getTop();
                activeStack = tapRunOff.splitStack( activeCard, getContext() );

                // Used if needs to be put back
                mTakenFrom[0] = 1;
                mTakenFrom[1] = tapRunOff.getID(); // Taken from the runOff
            }

            retVal = true;
        }

        return retVal;
    }

    // true if pX and pY are in one of the cardStacks collision detection
    public boolean stacksClicked(int pX, int pY, int arraySize, CardStack[] stacks)
    {
        boolean retVal = false;

        // Check each cardStack in the array
        for(int i = 0; i < arraySize; i++)
        {
            if( !stacks[i].isEmpty() ) // Make sure cardStack is not empty
            {
                // If it is a pile
                if( stacks[i].getID() == 1 )
                {
                    // Check collision
                    if( stacks[i].getDetectCollision().contains( pX, pY ) )
                    {
                        // take it from the top
                        activeCard  = stacks[i].getTop();
                        activeStack = stacks[i].splitStack(activeCard, getContext()); //Get any Cards on top of the selected card

                        mTakenFrom[0] = i; // Where did we take the card from
                        mTakenFrom[1] = stacks[i].getID();
                        break;
                    }
                }
                else
                {
                    // If it is a foundation
                    for( int j = 0; j < stacks[i].getSize(); j++ ) // Find the card clicked on
                    {
                        // Check collision
                        if (stacks[i].getAt(j).getDetectCollision().contains(pX, pY))
                        {
                            retVal = true;

                            // If its the top and faceDown
                            if( stacks[i].getAt(j) == stacks[i].getTop() && !stacks[i].getTop().isFaceUp() )
                            {
                                stacks[i].getTop().turnUp(); // Turn it Over
                                break; // Break, do not take any cards
                            }
                            else if( stacks[i].getAt(j).isFaceUp()  ) // Already face up
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

    // checks each cardStack in the array for collision, it so adds active cards to that stack
    public boolean placeCards(int pX, int pY, int arraySize, CardStack[] stacks)
    {
        boolean retVal = false;

        // Check the cardStacks
        for(int i = 0; i < arraySize; i++)
        {
            // Check if on empty stack
            if( (stacks[i].isEmpty()) ) // No Cards on stack
            {
                if( stacks[i].getDetectCollision().contains( pX, pY ) ) // If you are touching the stack
                {
                    if( stacks[i].getID() == 0 && activeStack.getAt(0).getValue() == 13 ) // If it is a foundation and active card is a king
                    {
                        retVal = true;

                        // Add king to empty foundation
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

                            // add ace to empty pile
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
                        // If it is a foundation OR only once card is being added
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

    // Return true if all Piles are full
    public boolean hasWon()
    {
        boolean retVal = true;

        for( int i = 0; i < NUM_PILES; i++ )
        {
            if( piles[i].getSize() != 13 ) // Standard Deck so each Pile would have 13 Max
            {
                retVal = false;
                break;
            }
        }

        return retVal;
    }
}
