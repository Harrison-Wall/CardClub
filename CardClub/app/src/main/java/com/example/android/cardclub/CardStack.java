package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Stack;

public class CardStack
{
    private int mID, mOffset, mX, mY;
    private Stack<Card> mCardStack;

    private Bitmap mStackMap;
    private Rect mDetectCollision;

    public CardStack()
    {
        mID = 0;
        mOffset = 0;
        mX = 0;
        mY = 0;
        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
    }

    public CardStack(int pID, int pX, int pY)
    {
        mID = pID;
        mOffset = 0;
        mX = pX;
        mY = pY;
        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
    }

    public void addCard(Card pCard)
    {
        pCard.setLocation(mX, (mY + mOffset));

        mCardStack.push(pCard);

        mOffset += 75;
    }

    public Card removeTop()
    {
        mOffset -= 75;
        return mCardStack.pop();
    }

    public void setBackMap(Context context)
    {
        BitmapFactory.Options myOps = new BitmapFactory.Options();
        myOps.inDensity = 2000;

        mStackMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_stack, myOps);

        mDetectCollision.left = mX;
        mDetectCollision.top = mY + mOffset;
        mDetectCollision.right = mX + mStackMap.getWidth();
        mDetectCollision.bottom = mY + mOffset + mStackMap.getHeight();
    }

    public Card removeAt(int index)
    {
        mOffset -= 75;
        return mCardStack.remove(index);
    }

    // Getters
    public Card getAt(int index) { return mCardStack.elementAt(index); }

    public Bitmap getStackMap() { return mStackMap; }

    public Rect getDetectCollision() { return mDetectCollision; }

    public boolean isEmpty() { return mCardStack.isEmpty(); }

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

    public void setLocation(int pX, int pY)
    {
        mX = pX;
        mY = pY;
        mOffset = 0;

        for( int i = 0; i < mCardStack.size(); i++ )
        {
            mCardStack.elementAt(i).setLocation(mX, mY+mOffset);
            mOffset += 75;
        }
    }

}
