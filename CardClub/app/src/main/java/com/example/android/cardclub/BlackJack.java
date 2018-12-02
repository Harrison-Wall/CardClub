package com.example.android.cardclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        dealerCards.get(0).turnDown();

        // Set limits
        myparams =  dealerInitialCard.getLayoutParams();

        // Deal and Create a new ImageView Card
        dealerCards.add( mDeck.dealCard() );

        addDealerCardImage( dealerCards.get(1) );

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
        hasUserWon(); // is the current hand good?

        if( mDeck.getCardsDelt() < 52 )
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
        hasUserWon();
    }

    public void stay()
    {
        if( !dealerCards.get(0).isFaceUp() )
        {
            dealerCards.get(0).turnUp();
            dealerInitialCard.setImageResource( dealerCards.get(0).getFaceID() ); // Flip up dealers initial card
            dealerScore = calculateScore( dealerCards ); // update dealer score
            dealerScoreView.setText(""+dealerScore);
            hasDealerWon();
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

            hasDealerWon();
        }

        hasDealerWon();
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

    public void hasUserWon()
    {
        if( userScore > 21 ) // you Lose!
        {
            // Show Alert
           // finish();
            Toast meessage = Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT);
            meessage.show();
        }
        else if( userScore == 21 ) // you won!
        {
            // Show Alert
           // finish();
            Toast meessage = Toast.makeText(this, "You Won", Toast.LENGTH_SHORT);
            meessage.show();
        }

        // Else keep going!
    }

    public void hasDealerWon()
    {
        if( dealerScore > 21 ) // you won!
        {
            Toast meessage = Toast.makeText(this, "You Won", Toast.LENGTH_SHORT);
            meessage.show();
        }
        else if( dealerScore == 21 ) // you lose!
        {
            Toast meessage = Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT);
            meessage.show();
        }
        else if( dealerScore == userScore ) // Tie
        {
            Toast meessage = Toast.makeText(this, "Tie game", Toast.LENGTH_SHORT);
            meessage.show();
        }
        else if (dealerScore > userScore) // you lose!
        {
            Toast meessage = Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT);
            meessage.show();
        }
        else
        {
            Toast meessage = Toast.makeText(this, "You Won", Toast.LENGTH_SHORT);
            meessage.show();
        }
    }

    public void addUserCardImage(Card pCard)
    {
        /*// https://stackoverflow.com/questions/17481341/how-to-get-android-screen-size-programmatically-once-and-for-all
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        if( userCardGroup.getWidth() <= width ) // If it has gone past the screen go to another row
        {
            userCardGroup = (LinearLayout) findViewById( R.id.user_card_group_2 );
        }*/

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
}