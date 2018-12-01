package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;

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

        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userScore = 0;
        userCards = new Vector<Card>();

        dealerScore = 0;
        dealerCards = new Vector<Card>();
    }


}