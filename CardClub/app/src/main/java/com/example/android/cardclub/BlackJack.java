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
    private ViewGroup.LayoutParams myparams;

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

        //Set up textviews
        dealerScoreView = (TextView) findViewById( R.id.dealer_score );
        userScoreView = (TextView) findViewById( R.id.user_score );

        // Dealer and User both get two cards
        dealerInitialCard = (ImageView) findViewById( R.id.dealer_card );
        dealerCards.add( mDeck.dealCard() ); // Needs to remain faceDown until user hits stay

        // Set limits
        myparams =  dealerInitialCard.getLayoutParams();

        // Deal and Create a new ImageView Card
        dealerCards.add( mDeck.dealCard() );
        ImageView newCard = new ImageView(this);
        newCard.setImageResource( dealerCards.get(1).getFaceID() );

        //Add the the Relative layout
        dealerCardGroup.addView( newCard, myparams );



        // Set up user Cards
        userInitialCard = (ImageView) findViewById( R.id.user_card );
        userCards.add( mDeck.dealCard() );
        userInitialCard.setImageResource( userCards.get(0).getFaceID() ); // Users first Card

        userCards.add( mDeck.dealCard() );
        ImageView tempCard = new ImageView(this);
        tempCard.setImageResource( userCards.get(1).getFaceID() ); // Users second Card

        userCardGroup.addView( tempCard, myparams );

        // Calculate user Score
        userScore = calculateScore( userCards );

        // Update Score Views
        userScoreView.setText( userScore );

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

        //Loop through vector

        //tally up card values

        //if >10 then just 10

        // if 1 then either 1 or 10

        return retVal;
    }

    public void updateScores()
    {

    }
}