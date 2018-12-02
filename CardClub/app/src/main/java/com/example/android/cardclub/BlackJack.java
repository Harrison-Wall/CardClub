package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BlackJack extends Activity
{
    private Deck mDeck;
    private int userScore, dealerScore;
    private ArrayList<Card> userCards, dealerCards;

    private ImageView dealerInitialCard, userInitialCard;
    private LinearLayout userCardGroup, dealerCardGroup;

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

        // Set up the card groups
        dealerCardGroup = (LinearLayout) findViewById( R.id.dealer_card_group );
        userCardGroup = (LinearLayout) findViewById( R.id.user_card_group );

        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userScore = 0;
        userCards = new ArrayList<Card>();

        dealerScore = 0;
        dealerCards = new ArrayList<Card>();

        //Set up textviews
        dealerScoreView = (TextView) findViewById( R.id.dealer_score_num );
        userScoreView = (TextView) findViewById( R.id.user_score_num );

        //--------------------------------------------------------------------------------------------

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

        sortCards( dealerCards );

        //--------------------------------------------------------------------------------------------

        // Set up user Cards
        userInitialCard = (ImageView) findViewById( R.id.user_card );
        userCards.add( mDeck.dealCard() );
        userInitialCard.setImageResource( userCards.get(0).getFaceID() ); // Users first Card

        hitMe(); // user has two Cards to start
    }

    public void hitMe()
    {
        if( mDeck.getCardsDelt() < 52 )
        {
            //add a new card to user
            Card tempCard = mDeck.dealCard();
            userCards.add( tempCard );

            ImageView tempView = new ImageView(this);
            tempView.setImageResource( tempCard.getFaceID() );
            userCardGroup.addView( tempView, myparams );

            sortCards( userCards );

            //update score
            userScore = calculateScore( userCards );
            userScoreView.setText( ""+userScore );
        }

        //check if lose/win
        hasUserWon();
    }

    public void stay()
    {
        //update dealer score

        // determin if dealer should keep drawing

        // determin winner
    }

    public int calculateScore( ArrayList<Card> pList )
    {
        int retVal = 0;
        int cardValue, listSize;

        listSize = pList.size() -1;

        //Loop through vector
        for( int i = listSize; i >= 0; i-- ) // Checks Highest value cards first (Aces assumed low to begin with0
        {
            cardValue = pList.get(i).getValue();

            if( cardValue > 10 ) // If K, J, Q
            {
                retVal += 10;
            }
            else if( cardValue == 1) // If it is an ace
            {
                if( retVal + 11 > 21 ) // If Aces high makes it too big
                {
                    retVal++;  // Aces low
                }
                else
                {
                    retVal += 11; // Aces high
                }
            }
            else
            {
                retVal += cardValue;
            }
        }

        return retVal;
    }

    public void sortCards( ArrayList<Card> pList )
    {
        // Intro to Algorithms: Insertion Sort
        Card tempCard;
        int i;

        if( pList.size() > 1 ) // Otherwise its already sorted
        {
            for( int j = 1; j < pList.size(); j++ )
            {
                 tempCard = pList.get(j);
                 i = j-1;

                 while( i >= 0 && pList.get(i).getValue() > tempCard.getValue() )
                 {
                     pList.set( i+1, pList.get(i) );
                     i--;
                 }

                 pList.set( i+1, tempCard );
            }
        }
    }

    public void hasUserWon()
    {
        if( userScore> 21 ) // you Lose!
        {
            // Show Alert
            finish();
        }
        else if( userScore == 21 ) // you won!
        {
            // Show Alert
            finish();
        }
    }
}