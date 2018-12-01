package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

public class BlackJack extends Activity
{
    private Deck mDeck;
    private int userScore, dealerScore, cardWidth, cardHeight;
    private Vector<Card> userCards, dealerCards;

    private ImageView dealerInitialCard, userInitialCard;
    private LinearLayout userCardGroup, dealerCardGroup;
    private int marginOffset;

    private TextView userScoreView, dealerScoreView;

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

        marginOffset = 8;

        // Set up the card groups
        dealerCardGroup = (LinearLayout) findViewById( R.id.dealer_card_group );
        userCardGroup = (LinearLayout) findViewById( R.id.user_card_group );

        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userScore = 0;
        userCards = new Vector<Card>();

        dealerScore = 0;
        dealerCards = new Vector<Card>();

        // Dealer and User both get two cards
        dealerInitialCard = (ImageView) findViewById( R.id.dealer_card );
        dealerCards.add( mDeck.dealCard() ); // Needs to remain faceDown until user hits stay

        // Set limits
        cardHeight = dealerInitialCard.getHeight();
        cardWidth = dealerInitialCard.getWidth();

        // Deal and Create a new ImageView Card
        dealerCards.add( mDeck.dealCard() );
        ImageView newCard = new ImageView(this);
        newCard.setImageResource( dealerCards.get(1).getFaceID() );
        newCard.setMinimumWidth( cardWidth );
        newCard.setMaxHeight( cardHeight );

        //Add the the Relative layout
        dealerCardGroup.addView( newCard );


        userInitialCard = (ImageView) findViewById( R.id.user_card );
        userCards.add( mDeck.dealCard() );

        userInitialCard.setImageResource( userCards.get(0).getFaceID() ); // Users first Card


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