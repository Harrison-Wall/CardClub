package com.example.android.cardclub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    Bundle mSavedState;
    String gameOverMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mSavedState = savedInstanceState;

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
        dealerCards.get(0).turnDown();

        // Set limits
        myparams =  dealerInitialCard.getLayoutParams();

        // Deal and Create a new ImageView Card
        dealerCards.add( mDeck.dealCard() );

        addDealerCardImage( dealerCards.get(1) );

        //--------------------------------------------------------------------------------------------

        // Set up user Cards
        userInitialCard = (ImageView) findViewById( R.id.user_card );
        userCards.add( mDeck.dealCard() );
        userInitialCard.setImageResource( userCards.get(0).getFaceID() ); // Users first Card

        hitMe(); // user has two Cards to start
    }

    public void hitMe()
    {
        if(  hasUserWon() )// is the current hand good?
        {
            showAlert();
            return;
        }

        if( mDeck.getCardsDelt() < 52 && userScore < 21 )
        {
            //add a new card to user
            Card tempCard = mDeck.dealCard();
            userCards.add( tempCard );

            addUserCardImage(tempCard);

            sortCards( userCards );

            //update score
            userScore = calculateScore( userCards );
            userScoreView.setText( ""+userScore );
        }

        //check if lose/win
       if(  hasUserWon() )
       {
           showAlert();
           return;
       }
    }

    public void stay()
    {
        if( !dealerCards.get(0).isFaceUp() )
        {
            dealerCards.get(0).turnUp();
            dealerInitialCard.setImageResource( dealerCards.get(0).getFaceID() ); // Flip up dealers initial card

            sortCards(dealerCards); // Can now safely sort them

            dealerScore = calculateScore( dealerCards ); // update dealer score
            dealerScoreView.setText(""+dealerScore);
        }

        if(  hasDealerWon() )
        {
            showAlert();
            return;
        }

        // determine if dealer should keep drawing
        while( mDeck.getCardsDelt() < 52 && dealerScore < 16 ) // Dealer does not draw if score is > 16
        {
            Card tempCard = mDeck.dealCard();
            dealerCards.add( tempCard );

            addDealerCardImage(tempCard);

            sortCards(dealerCards);

            dealerScore = calculateScore( dealerCards ); // update dealer score
            dealerScoreView.setText(""+dealerScore);

            if(  hasDealerWon() )
            {
                showAlert();
                return;
            }
        }
    }

    public int calculateScore( ArrayList<Card> pList )
    {
        int retVal = 0;
        int cardValue, listSize;

        listSize = pList.size() -1;

        //Loop through vector
        for( int i = listSize; i >= 0; i-- ) // Checks Highest value cards first (Aces assumed low to begin with)
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

        // If it is >21 check if making all aces low will fix it
        if( retVal > 21 )
        {
            retVal = 0;

            for( int i = listSize; i >= 0; i-- ) // Checks Highest value cards first (Aces assumed low to begin with)
            {
                cardValue = pList.get(i).getValue();

                if( cardValue > 10 ) // If K, J, Q
                {
                    retVal += 10;
                }
                else
                {
                    retVal += cardValue;
                }
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

    public boolean hasUserWon()
    {
        boolean retVal = false;

        if( userScore > 21 ) // you Lose!
        {
            gameOverMessage = "You Lose! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( userScore == 21 ) // you won!
        {
            gameOverMessage = "You Won! " + userScore + " to " + dealerScore;
            retVal = true;
        }

        return retVal;
    }

    public boolean hasDealerWon()
    {
        boolean retVal = false;

        if( dealerScore > 21) // you won!
        {
            gameOverMessage = "You Won! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( dealerScore == 21 ) // you lose!
        {
            gameOverMessage = "You Lose! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if(dealerScore > userScore ) // you lose!
        {
            gameOverMessage = "You Lose! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( dealerScore == userScore  && dealerScore >= 16) // Tie
        {
            gameOverMessage = "Tie Game! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( userScore > dealerScore && dealerScore >= 16)
        {
            gameOverMessage = "You Won! " + userScore + " to " + dealerScore;
            retVal = true;
        }

        return retVal;
    }

    public void addUserCardImage(Card pCard)
    {
        ImageView tempView = new ImageView(this);
        tempView.setImageResource( pCard.getFaceID() );
        userCardGroup.addView( tempView, myparams );
    }

    public void addDealerCardImage(Card pCard)
    {
        ImageView tempView = new ImageView(this);
        tempView.setImageResource( pCard.getFaceID() );
        dealerCardGroup.addView( tempView, myparams );
    }

    public void showAlert()
    {
        // Show and Alert Message https://www.tutorialspoint.com/android/android_alert_dialoges.html
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Game Over");
        alertBuilder.setMessage(gameOverMessage);

        alertBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Got a lot of weird bugs where alert box would not go away
                onCreate( mSavedState );
            }
        });

        alertBuilder.setNegativeButton("Home", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        AlertDialog myAlert = alertBuilder.create();
        myAlert.show();

        return;
    }
}