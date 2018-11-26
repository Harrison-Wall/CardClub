package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class War extends Activity
{
    private Deck mDeck;
    private int userCardCount = 0, opponentCardCount = 0;

    private Stack<Card> userStack;
    private Stack<Card> userPile;
    private Stack<Card> opponentStack;
    private Stack<Card> oppnentPile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_view);

        // Set up piles and stacks
        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userStack = new Stack<Card>();
        userPile = new Stack<Card>();
        opponentStack = new Stack<Card>();
        oppnentPile = new Stack<Card>();

        int halfDeck = mDeck.getSize()/2;

        for(int i = 0; i <halfDeck; i++)
        {
            userPile.push( mDeck.dealCard() );
            userCardCount++;
        }

        for(int i = 0; i <halfDeck; i++)
        {
            oppnentPile.push( mDeck.dealCard() );
            opponentCardCount++;
        }
    }

    public void drawCards(View view)
    {
        if( !userPile.empty() )
        {
            userStack.push( userPile.pop() );
            userCardCount--;

            updateUserCard( userStack.peek() );
            updateUserScoreCount( userCardCount );
            updateUserStackCount( userStack.size() );
        }
        else
        {
            return;
        }

        if( !oppnentPile.empty() )
        {
            opponentStack.push( oppnentPile.pop() );
            opponentCardCount--;

            updateOpponentCard( opponentStack.peek() );
            updateOpponentScoreCount( opponentCardCount );
            updateOpponentStackCount( opponentStack.size() );
        }
        else
        {
            return;
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