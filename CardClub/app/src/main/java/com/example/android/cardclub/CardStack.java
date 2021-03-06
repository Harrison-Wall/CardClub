/*
 * Harrison Wall
 * 2018
 */

package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Stack;

// Cardstack holds the card stack and display data for a stack of cards
public class CardStack
{
    private Stack<Card> mCardStack;

    // location and display variables
    private int mID, mOffset, mX, mY, mOffAmount, mDensity;
    private Bitmap mStackMap;
    private Rect mDetectCollision;

    // Default constructor
    public CardStack()
    {
        mID = 0;
        mOffset = 0;
        mOffAmount = 75;
        mX = 0;
        mY = 0;
        mDensity = 0;

        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
    }

    // Full constructor
    public CardStack(int pID, int pX, int pY, int pOffAmount, int pDensity, Context context)
    {
        mID = pID;

        // Offsets for displaying cards on the stack
        // Adding cards should result in them displaying lower on the stack
        mOffset = 0;
        mOffAmount = pOffAmount;

        mX = pX;
        mY = pY;
        mDensity = pDensity;

        mCardStack = new Stack<Card>( );
        mDetectCollision = new Rect();
        setBackMap(context, mDensity); // Set the bitmap for the stack
    }

    public void addCard(Card pCard)
    {
        // Each card added displaying a little further down than the last
        pCard.setLocation(mX, (mY + mOffset));
        mOffset += mOffAmount;

        mCardStack.push(pCard);
    }

    public Card removeTop()
    {
        // Need to adjust offset for next card being added
        mOffset -= mOffAmount;
        return mCardStack.pop();
    }

    // Set the display bitmap for the stack itself, not the cards
    public void setBackMap(Context context, int density)
    {
        BitmapFactory.Options myOps = new BitmapFactory.Options();
        myOps.inDensity = density;

        mStackMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_stack, myOps);

        // Collision for thae stack should follow its own image. not one of the cards
        mDetectCollision.left = mX;
        mDetectCollision.top = mY + mOffset;
        mDetectCollision.right = mX + mStackMap.getWidth();
        mDetectCollision.bottom = mY + mOffset + mStackMap.getHeight();
    }

    public Card removeAt(int index)
    {
        // Need to adjust offset for next card being added
        mOffset -= mOffAmount;
        return mCardStack.remove(index);
    }

    // Takes all cards above a chosen one, and puts them into a cardstack
    public CardStack splitStack(Card pCard, Context context)
    {
        // Set up the new cardStack
        CardStack newStack = new CardStack();
        newStack.setBackMap(context, mDensity);

        for(int i = 0; i < mCardStack.size(); ++i) // For each card currently in the stack
        {
            if(mCardStack.get(i) == pCard) // Find the one chosen
            {
                newStack.setLocation(pCard.getX(), pCard.getY()); // New stack should move with the card

                for(; i < mCardStack.size();) // Get all cards on top of the selected card
                {
                    newStack.addCard( this.removeAt(i) );
                }
            }
        }

        newStack.mID = this.mID; // IDs are used to put the stack back if needed

        return newStack;
    }

    // Add the contents of a CardStack to the current one
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

    public void clearStack() { mCardStack.clear(); }

    // Getters
    public Card getAt(int index) { return mCardStack.elementAt(index); }

    public Bitmap getStackMap() { return mStackMap; } // get the bitmap image

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

    // Adjusts the location for the stack and each card on it
    public void setLocation(int pX, int pY)
    {
        mX = pX;
        mY = pY;
        mOffset = 0; // Reset so the cards can be moved correctly

        // Adjust for each card on the stack
        for( int i = 0; i < mCardStack.size(); i++ )
        {
            mCardStack.elementAt(i).setLocation(mX, mY+mOffset);
            mOffset += mOffAmount;
        }
    }

}
