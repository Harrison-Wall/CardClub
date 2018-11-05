package com.example.android.cardclub;

import java.util.Random;
import java.util.ArrayList;

public class Deck
{
    private int deckSize = 52;
    private ArrayList<Card> deckOfCards = new ArrayList<Card>();

    Deck( )
    {
        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false));
        }
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
    }

    /**
     * Returns a Card at a specified location.
     * @param pIndex (int)
     * @return Card
     */
    public Card getCard (int pIndex)
    {
        return deckOfCards.get(pIndex);
    }

    /**
     * Randomly Shuffles the Deck
     */
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

    /**
     * Puts the deck into order by ID
     */
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
