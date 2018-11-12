package com.example.android.cardclub;

import java.util.Stack;

public class CardStack
{
    private int mID, mOffset, mX, mY;
    private Stack<Card> mCardStack;

    public CardStack()
    {
        mID = 0;
        mOffset = 50;
        mX = 0;
        mY = 0;
        mCardStack = new Stack<Card>( );
    }

    public CardStack(int pID, int pX, int pY)
    {
        mID = pID;
        mOffset = 50;
        mX = pX;
        mY = pY;
        mCardStack = new Stack<Card>( );
    }

    public void addCard(Card pCard)
    {
        pCard.setCurrX( mX );
        pCard.setCurrY( mY + mOffset); // Next Card should be below the previous
        mCardStack.push(pCard);

        mOffset += 50;
    }

    public Card removeTop()
    {
        mOffset -= 50;
        return mCardStack.pop();
    }


    // Getters
    public boolean isEmpty() { return mCardStack.isEmpty(); }

    public Card getAt(int index) { return mCardStack.elementAt(index); }

    public Card getTop() { return mCardStack.peek(); }

    public int getSize() { return mCardStack.size(); }

    public int getID() { return mID; }

    public int getOffset() {return  mOffset;}

    public int getX() { return mX; }

    public int getY() {return  mY;}

    // Setters
    public void setOffset(int pOff) {mOffset = pOff;}

    public void setX(int pX) {mX = pX;}

    public void setY(int pY) {mY = pY;}

}
