package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Card
{
    private int mID;                     //1-52
    private int miValue;                 //1-13
    private int miSuit;                  //0-3
    private boolean mfaceUp;

    // Info for drawing
    private int mCurrX, mCurrY, mOldX, mOldY, mHieght, mWidth;
    private int mresID;
    private Bitmap mFace;

    private static int iCounter = 1;    //Used to keep track of Cards - My USE ONLY

    //Controlling coordinates so that ship won't go outside the screen
    private int mMaxY, mMinY, mMaxX, mMinX;

    private Rect detectCollision;

    // Standard Card with no Display Data
    Card()
    {
        mID = iCounter;
        miValue = 1;
        miSuit = 0;
        mfaceUp = false;
        iCounter++;
    }

    // Specific Card with no display data
    Card(int pValue, int pSuit, boolean pFace)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;
        iCounter++;
    }

    // specific card with display data
    Card(int pValue, int pSuit, boolean pFace, int pX, int pY, int pHeight, int pWidth, Context context)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;

        // Set display
        mresID = R.drawable.blue_back;
        setFaceMap(context);

        mCurrX = pX;
        mCurrY = pY;
        mHieght = pHeight;
        mWidth = pWidth;
        mMaxY = pY - mFace.getHeight();
        mMaxX = 0;

        detectCollision = new Rect(mCurrX, mCurrY, mFace.getWidth(), mFace.getHeight());

        iCounter++;
    }

    // Setters

    public void setmCurrX(int pX)
    {
        mOldX = mCurrX;
        mCurrX = pX;
    }

    public void setCurrY(int pY)
    {
        mOldY = mCurrY;
        mCurrY = pY;
    }

    public void setResID(int pID) { mresID = pID; }

    public void setFaceMap(Context context) { mFace = BitmapFactory.decodeResource(context.getResources(), mresID); }

    public void turnUp() { mfaceUp = true; }

    public void turnDown() { mfaceUp = false; }

    public static void resetCounter() { iCounter = 1; }

    public void setSuit(int pSuit)
    {
        miSuit = pSuit;
    }

    public void setID(int pID)
    {
        mID = pID;
    }

    public void setValue(int pValue)
    {
        miValue = pValue;
    }

    public void setHieght(int pHiehgt) { mHieght = pHiehgt; }

    public void setWidth(int pWidth)
    {
        mWidth = pWidth;
    }

    // Getters

    public boolean isRed()
    {
        return (miSuit == 1 || miSuit == 2);
    }

    public boolean isFaceUp()
    {
        return mfaceUp;
    }

    public int getID()
    {
        return mID;
    }

    public int getSuit()
    {
        return miSuit;
    }

    public int getValue()
    {
        return miValue;
    }

    public int getCurrX()
    {
        return mCurrX;
    }

    public int getCurrY()
    {
        return mCurrY;
    }

    public int getOldX()
    {
        return mOldX;
    }

    public int getOldY()
    {
        return mOldY;
    }

    public int getHieght()
    {
        return mHieght;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public Bitmap getFaceMap()
    {
        return mFace;
    }

    public Rect getDetectCollision()
    {
        return detectCollision;
    }

    public int getResID() { return mresID; }


    /*********TEMPORARY WHILE LEARNING THIS SHIT*********/
    //Method to update coordinate of character
    public void update()
    {

        //but controlling it also so that it won't go off the screen
        if (mCurrY < mMinY)
        {
            mCurrY = mMinY;
        }

        if (mCurrY > mMaxY)
        {

            mCurrY = mMaxY;
        }

        detectCollision.left = mCurrX;
        detectCollision.top = mCurrY;
        detectCollision.right = mCurrX + mFace.getWidth();
        detectCollision.bottom = mCurrY + mFace.getHeight();
    }

}
