/*
 * Harrison Wall
 * 2018
 */

package com.example.android.cardclub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class War extends Activity
{
    private Deck mDeck;

    private Stack<Card> userStack; // Holds the users current cards in contention
    private Queue<Card> userQueue; // Holds the cards that were dealt to the user at the start

    private Stack<Card> opponentStack; // Holds the opponents current cards in contention
    private Queue<Card> oppnentQueue; // Holds the opponents that were dealt to the user at the start

    Bundle pSavedSate; // Used for restarting the game

    String winMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set up the layout
        pSavedSate = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_view);

        // Set up piles and stacks
        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userStack = new Stack<Card>();
        userQueue = new LinkedList<Card>();
        opponentStack = new Stack<Card>();
        oppnentQueue = new LinkedList<Card>();

        // Deck is divided between all player
        int halfDeck = mDeck.getSize()/2;
        for(int i = 0; i <halfDeck; i++)
        {
            userQueue.add( mDeck.dealCard() );
        }
        for(int i = 0; i <halfDeck; i++)
        {
            oppnentQueue.add( mDeck.dealCard() );
        }
    }

    // Called when user clicks the "draw" button
    public void drawCards(View view)
    {
        if( !userQueue.isEmpty() && !oppnentQueue.isEmpty() ) // While each queue has cards
        {
            // Take a card from the user
            userStack.push( userQueue.poll() );
            updateUserCard( userStack.peek() );

            // Take a card from the AI
            opponentStack.push( oppnentQueue.poll() );
            updateOpponentCard( opponentStack.peek() );

            //Compare to find the winner
            switch( compareCards( userStack.peek(), opponentStack.peek() ) )
            {
                case 1: // User wins Round
                    {
                        Toast mWinner = Toast.makeText(this, "Round Won", Toast.LENGTH_SHORT);
                        mWinner.show();

                        // Add Cards to User Pile
                        userQueue.addAll( userStack );
                        userQueue.addAll( opponentStack );
                        userStack.clear();
                        opponentStack.clear();

                        // Update textViews
                        updateUserScoreCount( userQueue.size() );
                        updateUserStackCount( userStack.size() );

                        updateOpponentScoreCount( oppnentQueue.size() );
                        updateOpponentStackCount( opponentStack.size() );
                    }
                    break;
                case 0: // Tie Round
                    {
                        Toast mWinner = Toast.makeText(this, "Round Tie", Toast.LENGTH_SHORT);
                        mWinner.show();

                        //Update Stack size
                        updateUserStackCount( userStack.size() );
                        updateOpponentStackCount( opponentStack.size() );
                    }
                    break;
                case -1: // AI wind Round
                    {
                        Toast mWinner = Toast.makeText(this, "Round Lost", Toast.LENGTH_SHORT);
                        mWinner.show();

                        // Add Cards to AI Pile
                        oppnentQueue.addAll( userStack );
                        oppnentQueue.addAll( opponentStack );
                        userStack.clear();
                        opponentStack.clear();

                        // Update textViews
                        updateUserScoreCount( userQueue.size() );
                        updateUserStackCount( userStack.size() );

                        updateOpponentScoreCount( oppnentQueue.size() );
                        updateOpponentStackCount( opponentStack.size() );
                    }
                    break;
                default: // Shouldn't Occur
                    break;
            }
        }
        else if( userQueue.isEmpty() )
        {
            // AI Wins
            Toast mWinner = Toast.makeText(this, "Game Lost.", Toast.LENGTH_LONG);
            mWinner.show();

            winMessage = "You Lose!";

            showAlert();
        }
        else if( oppnentQueue.isEmpty() )
        {
            // User Wins
            Toast mWinner = Toast.makeText(this, "Game Won.", Toast.LENGTH_LONG);
            mWinner.show();

            winMessage = "You Win!";

            showAlert();
        }

        return;
    }

    // Update the Image View for the users card
    public void updateUserCard(Card pCard )
    {
        ImageView userCard = (ImageView) findViewById( R.id.user_card );

        userCard.setImageResource( pCard.getFaceID() );

        return;
    }

    // Update the TextView for the users stackCount
    public void updateUserStackCount(int pCount)
    {
        TextView userStackCount = (TextView) findViewById( R.id.user_stack_count );
        userStackCount.setText( ""+pCount );

        return;
    }

    // Update the TextView for the users score
    public void updateUserScoreCount(int pCount)
    {
        TextView userScoreCount = (TextView) findViewById( R.id.user_score_count );
        userScoreCount.setText( ""+pCount );

        return;
    }

    // Update the Image View for the opponents card
    public void updateOpponentCard(Card pCard )
    {
        ImageView opponentCard = (ImageView) findViewById( R.id.ai_card );

        opponentCard.setImageResource( pCard.getFaceID() );

        return;
    }

    // Update the TextView for the opponents stackCount
    public void updateOpponentStackCount( int pCount )
    {
        TextView opponentStackCount = (TextView) findViewById( R.id.ai_stack_count );
        opponentStackCount.setText( ""+pCount );

        return;
    }

    // Update the TextView for the opponents score
    public void updateOpponentScoreCount( int pCount )
    {
        TextView opponentScoreCount = (TextView) findViewById( R.id.ai_score_count );
        opponentScoreCount.setText( ""+pCount );

        return;
    }

    // <0 is A won, 0 if tie, >0 if B won
    public int compareCards(Card pCardA, Card pCardB)
    {
        int retVal = 0;

        if( pCardA.getValue() > pCardB.getValue() )
        {
            retVal = -1;
        }
        else if ( pCardB.getValue() > pCardA.getValue() )
        {
            retVal = 1;
        }

        return retVal;
    }

    // End of Game Alert
    public void showAlert()
    {
        // Show and Alert Message https://www.tutorialspoint.com/android/android_alert_dialoges.html
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Game Over");
        alertBuilder.setMessage(winMessage);
        alertBuilder.setCancelable(false);

        alertBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onCreate(pSavedSate); // New Game
            }
        });

        alertBuilder.setNegativeButton("Home", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish(); // Back to main_activity
            }
        });

        AlertDialog myAlert = alertBuilder.create();
        myAlert.show();

        return;
    }
}