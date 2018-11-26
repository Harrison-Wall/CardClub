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
    private int userCardCount = 0, opponentCardCount = 0;

    private Stack<Card> userStack;
    private Queue<Card> userQueue; // Change to Queue

    private Stack<Card> opponentStack;
    private Queue<Card> oppnentQuque; // Change to Queue

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
        oppnentQuque = new LinkedList<Card>();

        int halfDeck = mDeck.getSize()/2;

        for(int i = 0; i <halfDeck; i++)
        {
            userQueue.add( mDeck.dealCard() );
            userCardCount++;
        }

        for(int i = 0; i <halfDeck; i++)
        {
            oppnentQuque.add( mDeck.dealCard() );
            opponentCardCount++;
        }
    }

    public void drawCards(View view)
    {
        if( !userQueue.isEmpty() )
        {
            userStack.push( userQueue.poll() );
            userCardCount--;

            updateUserCard( userStack.peek() );
            updateUserScoreCount( userCardCount );
            updateUserStackCount( userStack.size() );
        }

        if( !oppnentQuque.isEmpty() )
        {
            opponentStack.push( oppnentQuque.poll() );
            opponentCardCount--;

            updateOpponentCard( opponentStack.peek() );
            updateOpponentScoreCount( opponentCardCount );
            updateOpponentStackCount( opponentStack.size() );
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
}