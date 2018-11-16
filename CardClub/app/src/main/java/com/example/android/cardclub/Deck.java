package com.example.android.cardclub;

import android.content.Context;

import java.util.Random;
import java.util.ArrayList;

public class Deck
{
    private int deckSize = 52, cardsDelt;
    private int[] resourceIDs;
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
    Deck(Context context)
    {
        resourceIDs = new int[deckSize];
        addResources(resourceIDs);

        for ( int i = 0; i < deckSize; i++ )
        {
            deckOfCards.add(new Card( ((i%13)+1), (i/13), false, 0, 0, resourceIDs[i], context ));
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

    public void addResources(int array[])
    {
        // Card Images from: http://acbl.mybigcommerce.com/52-playing-cards/
        if( array.length >= 51 )
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
}
