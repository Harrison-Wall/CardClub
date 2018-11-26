package com.example.android.cardclub;

import android.app.Activity;
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

    private Stack<Card> userStack;
    private Queue<Card> userQueue; // Change to Queue

    private Stack<Card> opponentStack;
    private Queue<Card> oppnentQueue; // Change to Queue

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_view);

        // Set up piles and stacks
        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userStack = new Stack<Card>();
        userQueue = new LinkedList<Card>();
        opponentStack = new Stack<Card>();
        oppnentQueue = new LinkedList<Card>();

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

    public void drawCards(View view)
    {
        if( !userQueue.isEmpty() && !oppnentQueue.isEmpty() )
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

            // TODO: Change to an Alert Dialogue

        }
        else if( oppnentQueue.isEmpty() )
        {
            // User Wins
            Toast mWinner = Toast.makeText(this, "Game Won.", Toast.LENGTH_LONG);
            mWinner.show();

            // TODO: Change to an Alert Dialogue
        }

        return;
    }

    public void updateUserCard(Card pCard )
    {
        ImageView userCard = (ImageView) findViewById( R.id.user_card );

        userCard.setImageResource( pCard.getFaceID() );

        return;
    }

    public void updateUserStackCount(int pCount)
    {
        TextView userStackCount = (TextView) findViewById( R.id.user_stack_count );
        userStackCount.setText( ""+pCount );

        return;
    }

    public void updateUserScoreCount(int pCount)
    {
        TextView userScoreCount = (TextView) findViewById( R.id.user_score_count );
        userScoreCount.setText( ""+pCount );

        return;
    }

    public void updateOpponentCard(Card pCard )
    {
        ImageView opponentCard = (ImageView) findViewById( R.id.ai_card );

        opponentCard.setImageResource( pCard.getFaceID() );

        return;
    }

    public void updateOpponentStackCount( int pCount )
    {
        TextView opponentStackCount = (TextView) findViewById( R.id.ai_stack_count );
        opponentStackCount.setText( ""+pCount );

        return;
    }

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
}