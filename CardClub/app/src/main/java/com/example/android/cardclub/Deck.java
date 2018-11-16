package com.example.android.cardclub;

import android.content.Context;

import java.util.Random;
import java.util.ArrayList;

public class Deck
{
    private int deckSize = 52;
    private int cardsDelt;
    private ArrayList<Card> deckOfCards = new ArrayList<Card>();

    Deck( )
    {
        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false));
        }

        cardsDelt = 0;
    }

    /**
     * Custom sized Deck, good for using Jokers.
     * @param pDeckSize (int)
     */
    Deck(int pDeckSize)
    {
        deckSize = pDeckSize;

        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add( new Card( ((i%13)+1), (i/13), false) );
        }

        cardsDelt = 0;
    }

    // Deck with display data in the cards
    Deck(Context context, int resArray[])
    {
        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false, 0, 0, resArray[i], context ));
        }

        cardsDelt = 0;
    }


    public Card getCard (int pIndex)
    {
        return deckOfCards.get(pIndex);
    }

    public int getSize () {return deckSize;}

    public int getCardsDelt() {return cardsDelt;}

    public Card dealCard()
    {
        Card retVal;

        if( cardsDelt < deckSize )
        {
            retVal = getCard(cardsDelt);
            cardsDelt++;
        }
        else
        {
            retVal = null;
        }

        return retVal;
    }

    public void shuffleDeck()
    {

        Random rndgen = new Random(System.currentTimeMillis());
        int Index = 51;
        ArrayList<Card> tempDeck = new ArrayList<Card>();

        for( int k = 0; k < deckSize; k++ )
        {
            int iNum1 = rndgen.nextInt(Index+1);

            tempDeck.add( deckOfCards.get(iNum1) );

            deckOfCards.set(iNum1, deckOfCards.get(Index));

            Index--;
        }

        deckOfCards = tempDeck;

        return;

    }

    public void sortDeck()
    {
        Card.resetCounter();
        deckOfCards.clear();

        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add( new Card( ((i%13)+1), (i/13), false) );
        }
    }
}
