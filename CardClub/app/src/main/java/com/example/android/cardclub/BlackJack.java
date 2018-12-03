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

    private ImageView dealerInitialCard, userInitialCard; // Initial Cards in the Layout
    private LinearLayout userCardGroup, dealerCardGroup;  // The ViewGroup containing the cards

    private TextView userScoreView, dealerScoreView;      // TextViews holding the scores
    private ViewGroup.LayoutParams myparams;              // Used to size cards properly

    Bundle mSavedState;     // used to start a new game
    String gameOverMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set up the Layout
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

        // Set up decks, scores, and arrayLists
        mDeck = new Deck(this);
        mDeck.shuffleDeck();

        userScore = 0;
        userCards = new ArrayList<Card>();

        dealerScore = 0;
        dealerCards = new ArrayList<Card>();

        //Set up TextViews
        dealerScoreView = (TextView) findViewById( R.id.dealer_score_num );
        userScoreView = (TextView) findViewById( R.id.user_score_num );

        //----------------Set up the Dealer's Cards---------------------------------------------------

        // Dealers first card
        dealerInitialCard = (ImageView) findViewById( R.id.dealer_card );
        dealerCards.add( mDeck.dealCard() ); // Needs to remain faceDown until user hits stay

        // Set parameter for card size, etc
        myparams =  dealerInitialCard.getLayoutParams();

        // Deal and Create a new ImageView Card for the dealer
        dealerCards.add( mDeck.dealCard() );
        addDealerCardImage( dealerCards.get(1) );

        //--------------------------------------------------------------------------------------------

        // Set up users first Card
        userInitialCard = (ImageView) findViewById( R.id.user_card );
        userCards.add( mDeck.dealCard() );
        userInitialCard.setImageResource( userCards.get(0).getFaceID() );

        hitMe(); // user has two Cards to start, same as hitting once
    }

    // Draw once for the user each time
    public void hitMe()
    {
        if(  hasUserWon() )// is the current hand good?
        {
            showAlert(); // End Game, show options
            return;
        }

        if( mDeck.getCardsDelt() < 52 && userScore < 21 ) // Can't draw if out of Cards, or user has lost
        {
            //add a new card to user
            Card tempCard = mDeck.dealCard();
            userCards.add( tempCard );
            addUserCardImage(tempCard);

            sortCards( userCards ); // Sorts for more accurate counting

            //update user score
            userScore = calculateScore( userCards );
            userScoreView.setText( ""+userScore );
        }

        //check if lose/win
       if(  hasUserWon() )
       {
           showAlert(); // End Game, show options
           return;
       }
    }

    // Dealer draws until game is over
    public void stay()
    {
        // Flip up dealers initial card
        dealerInitialCard.setImageResource( dealerCards.get(0).getFaceID() );

        sortCards(dealerCards); // Can now safely sort them

        dealerScore = calculateScore( dealerCards ); // update dealer score
        dealerScoreView.setText(""+dealerScore);

        if(  hasDealerWon() ) // Do we need to draw?
        {
            showAlert(); // End Game, show options
            return;
        }

        while( mDeck.getCardsDelt() < 52 && dealerScore < 16 ) // Dealer does not draw if score is > 16
        {
            // Give the dealer a card
            Card tempCard = mDeck.dealCard();
            dealerCards.add( tempCard );
            addDealerCardImage(tempCard);

            sortCards(dealerCards);

            // update dealer score
            dealerScore = calculateScore( dealerCards );
            dealerScoreView.setText(""+dealerScore);

            if(  hasDealerWon() ) // Do we need to keep drawing
            {
                showAlert(); // End Game, show options
                return;
            }
        }
    }

    // Sum the up the cards
    public int calculateScore( ArrayList<Card> pList )
    {
        int retVal = 0;
        int cardValue, listSize;

        // -1 since we loop from top ( ArrayList is sorted in ascending order ), leaving any aces for last
        listSize = pList.size() -1;

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
                    retVal += cardValue; // Ace value is 1
                }
            }
        }

        return retVal;
    }

    // Sort the ArrayList in ascending order using insertion sort
    public void sortCards( ArrayList<Card> pList )
    {
        Card key;
        int i;

        if( pList.size() > 1 ) // Otherwise its already sorted
        {
            for( int j = 1; j < pList.size(); j++ ) // Start at 1 since we need atleast two cards (0, and 1)
            {
                key = pList.get(j);
                 i = j-1;

                 while( i >= 0 && pList.get(i).getValue() > key.getValue() ) // Find the keys place in the order
                 {
                     pList.set( i+1, pList.get(i) );
                     i--;
                 }

                 pList.set( i+1, key ); // Set they keys place
            }
        }
    }

    // Check if the user has won (only done when user clicks on "hit")
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

        // Cases not involving 21 should be solved by the Dealer drawing

        return retVal;
    }

    // Check for win condition, done when the user click "stay"
    public boolean hasDealerWon()
    {
        boolean retVal = false;

        if( dealerScore > 21) // Dealer Bust
        {
            gameOverMessage = "You Won! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( dealerScore == userScore  && dealerScore >= 16) // Tie Score, but only if the dealer isn't drawing anymore
        {
            gameOverMessage = "Tie Game! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( dealerScore == 21 ) // Dealer got 21
        {
            gameOverMessage = "You Lose! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if(dealerScore > userScore ) // Dealer has higher score
        {
            gameOverMessage = "You Lose! " + userScore + " to " + dealerScore;
            retVal = true;
        }
        else if( userScore > dealerScore && dealerScore >= 16) // User has higher score, but only if the dealer isn't drawing anymore
        {
            gameOverMessage = "You Won! " + userScore + " to " + dealerScore;
            retVal = true;
        }

        return retVal;
    }

    // Creates and adds an ImageView of the given card to the users hand
    public void addUserCardImage(Card pCard)
    {
        ImageView tempView = new ImageView(this);
        tempView.setImageResource( pCard.getFaceID() );
        userCardGroup.addView( tempView, myparams );
    }

    // Creates and adds an ImageView of the given card to the dealers hand
    public void addDealerCardImage(Card pCard)
    {
        ImageView tempView = new ImageView(this);
        tempView.setImageResource( pCard.getFaceID() );
        dealerCardGroup.addView( tempView, myparams );
    }

    // End of Game box for user
    public void showAlert()
    {
        // Show and Alert Message https://www.tutorialspoint.com/android/android_alert_dialoges.html
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Game Over");
        alertBuilder.setMessage(gameOverMessage);
        alertBuilder.setCancelable(false);

        // New Game, recreate using savedSate
        alertBuilder.setPositiveButton("New Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Got a lot of weird bugs where alert box would not go away
                onCreate( mSavedState );
            }
        });

        // Back to home
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