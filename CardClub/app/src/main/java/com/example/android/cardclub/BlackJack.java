package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Vector;

public class BlackJack extends Activity
{
    private Deck mDeck;
    private int userScore, dealerScore;
    private Vector<Card> userCards, dealerCards;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_view);

        Button hitBtn = (Button) findViewById( R.id.hit_btn ); // OnClick for "hit"
        hitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hitMe();
            }
        });

        Button stayBtn = (Button) findViewById( R.id.stay_btn ); // OnClick for "stay"
        stayBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stay();
            }
        });

        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userScore = 0;
        userCards = new Vector<Card>();

        dealerScore = 0;
        dealerCards = new Vector<Card>();

        // Dealer and User both get two cards

    }

    public void hitMe()
    {

    }

    public void stay()
    {

    }

    public int calculateScore(Vector<Card> pVect )
    {
        int retVal = -1; // Default to invalid

        return retVal;
    }

    public void updateScores()
    {

    }
}