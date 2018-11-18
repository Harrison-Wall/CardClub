package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Stack;

public class CardStack
{
    private int mID, mOffset, mX, mY, mOffAmount, mDensity;
    private Stack<Card> mCardStack;

    private Bitmap mStackMap;
    private Rect mDetectCollision;

    public CardStack()
    {
        mID = 0;
        mOffset = 0;
        mOffAmount = 75;
        mX = 0;
        mY = 0;
        mDensity = 2000;

        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
    }

    public CardStack(int pID, int pX, int pY, int pOffAmount, int pDensity, Context context)
    {
        mID = pID;
        mOffset = 0;
        mOffAmount = pOffAmount;
        mX = pX;
        mY = pY;
        mDensity = pDensity;

        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
        setBackMap(context, mDensity);
    }

    public void addCard(Card pCard)
    {
        pCard.setLocation(mX, (mY + mOffset));

        mCardStack.push(pCard);

        mOffset += mOffAmount;
    }

    public Card removeTop()
    {
        mOffset -= mOffAmount;
        return mCardStack.pop();
    }

    public void setBackMap(Context context, int density)
    {
        BitmapFactory.Options myOps = new BitmapFactory.Options();
        myOps.inDensity = density;

        mStackMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_stack, myOps);

        mDetectCollision.left = mX;
        mDetectCollision.top = mY + mOffset;
        mDetectCollision.right = mX + mStackMap.getWidth();
        mDetectCollision.bottom = mY + mOffset + mStackMap.getHeight();
    }

    public Card removeAt(int index)
    {
        mOffset -= mOffAmount;
        return mCardStack.remove(index);
    }

    public CardStack splitStack(Card pCard, Context context)
    {
        CardStack newStack = new CardStack();
        newStack.setBackMap(context, mDensity);

        for(int i = 0; i < mCardStack.size(); ++i)
        {
            if(mCardStack.get(i) == pCard)
            {
                newStack.setLocation(pCard.getX(), pCard.getY());

                for(; i < mCardStack.size();)
                {
                    newStack.addCard( this.removeAt(i) );
                }
            }
        }

        newStack.mID = this.mID;

        return newStack;
    }

    public void addStack(CardStack pStack)
    {
        if( pStack != null )
        {
            while( !pStack.isEmpty() )
            {
                this.addCard( pStack.removeAt(0) );
            }
        }
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

    public int getOffAmount() { return mOffAmount; }

    // Setters
    public void setOffset(int pOff) {mOffset = pOff;}

    public void setX(int pX) {mX = pX;}

    public void setY(int pY) {mY = pY;}

    public void setOffAmount( int pAmount ) { mOffAmount = pAmount; }

    public void setLocation(int pX, int pY)
    {
        mX = pX;
        mY = pY;
        mOffset = 0;

        for( int i = 0; i < mCardStack.size(); i++ )
        {
            mCardStack.elementAt(i).setLocation(mX, mY+mOffset);
            mOffset += mOffAmount;
        }
    }

}
