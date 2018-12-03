/*
 * Harrison Wall
 * 2016-2018
 */

package com.example.android.cardclub;

import android.content.Context;
import java.util.Random;
import java.util.ArrayList;

/**
 * Collection of cards, uses an arrayList as a base.
 */
public class Deck
{
    private int deckSize = 52, cardsDelt;
    private int[] resourceIDs; // the IDs of the card faces for bitmaps
    private ArrayList<Card> deckOfCards = new ArrayList<Card>();

    /**
     * Standard Constructor for using text-based cards
     */
    Deck( )
    {
        for ( int i = 0; i < deckSize; i++ )
        {
            // Card( Value (1-13->A-K), Suit (0-3), faceUp or faceDown
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
            // Card( Value (1-13->A-K), Suit (0-3), faceUp or faceDown
            deckOfCards.add( new Card( ((i%13)+1), (i/13), false) );
        }

        cardsDelt = 0;
    }

    /**
     * Standard Sized deck with bitmap based cards
     * @param context is passed to the cards so their bitmaps can be initialized
     */
    Deck(Context context)
    {
        // Fill array with all the resource IDs for easy assignment
        // due to this the order of creation/assignment matters
        resourceIDs = new int[deckSize];
        addResources(resourceIDs);


        for ( int i = 0; i < deckSize; i++ )
        {
            // Card( Value (1-13->A-K), Suit (0-3), faceUp or faceDown, coordinates, face bitmap image, context. size
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false, 0, 0, resourceIDs[i], context, 2000 ));
        }

        cardsDelt = 0;
    }

    /**
     * Standard Sized deck with bitmap based cards
     * @param context is passed to the cards so their bitmaps can be initialized
     * @param pDensity is passed to the cards, determines their size
     */
    Deck(Context context, int pDensity)
    {
        resourceIDs = new int[deckSize];
        addResources(resourceIDs);

        for ( int i = 0; i < deckSize; i++ )
        {
            // Card( Value (1-13->A-K), Suit (0-3), faceUp or faceDown, coordinates, face bitmap image, context. size
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false, 0, 0, resourceIDs[i], context, pDensity ));
        }

        cardsDelt = 0;
    }

    // gives the next card in the arrayList
    public Card dealCard()
    {
        Card retVal;

        if( cardsDelt < deckSize ) // Make sure there are still cards to give
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

    // Randomize the deck
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

    // Reorder the deck
    public void sortDeck()
    {
        Card.resetCounter();
        deckOfCards.clear();

        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add( new Card( ((i%13)+1), (i/13), false) );
        }

        cardsDelt = 0;
    }

    // Fills given array with the cards image IDs
    public void addResources(int array[])
    {
        // Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/
        if( array.length >= 51 ) // Array has to be big enough
        {
            // Clubs
            array[0] = R.drawable.c_a;
            array[1] = R.drawable.c_2;
            array[2] = R.drawable.c_3;
            array[3] = R.drawable.c_4;
            array[4] = R.drawable.c_5;
            array[5] = R.drawable.c_6;
            array[6] = R.drawable.c_7;
            array[7] = R.drawable.c_8;
            array[8] = R.drawable.c_9;
            array[9] = R.drawable.c_10;
            array[10] = R.drawable.c_j;
            array[11] = R.drawable.c_q;
            array[12] = R.drawable.c_k;

            // Diamonds
            array[13] = R.drawable.d_a;
            array[14] = R.drawable.d_2;
            array[15] = R.drawable.d_3;
            array[16] = R.drawable.d_4;
            array[17] = R.drawable.d_5;
            array[18] = R.drawable.d_6;
            array[19] = R.drawable.d_7;
            array[20] = R.drawable.d_8;
            array[21] = R.drawable.d_9;
            array[22] = R.drawable.d_10;
            array[23] = R.drawable.d_j;
            array[24] = R.drawable.d_q;
            array[25] = R.drawable.d_k;

            // Hearts
            array[26] = R.drawable.h_a;
            array[27] = R.drawable.h_2;
            array[28] = R.drawable.h_3;
            array[29] = R.drawable.h_4;
            array[30] = R.drawable.h_5;
            array[31] = R.drawable.h_6;
            array[32] = R.drawable.h_7;
            array[33] = R.drawable.h_8;
            array[34] = R.drawable.h_9;
            array[35] = R.drawable.h_10;
            array[36] = R.drawable.h_j;
            array[37] = R.drawable.h_q;
            array[38] = R.drawable.h_k;

            // Spades
            array[39] = R.drawable.s_a;
            array[40] = R.drawable.s_2;
            array[41] = R.drawable.s_3;
            array[42] = R.drawable.s_4;
            array[43] = R.drawable.s_5;
            array[44] = R.drawable.s_6;
            array[45] = R.drawable.s_7;
            array[46] = R.drawable.s_8;
            array[47] = R.drawable.s_9;
            array[48] = R.drawable.s_10;
            array[49] = R.drawable.s_j;
            array[50] = R.drawable.s_q;
            array[51] = R.drawable.s_k;
        }
    }

    public Card getCard (int pIndex) { return deckOfCards.get(pIndex); }

    public int getSize () {return deckSize;}

    public int getCardsDelt() {return cardsDelt;}
}
