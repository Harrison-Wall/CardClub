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
    private int mCurrX, mCurrY, mOldX, mOldY;
    private int mresID;
    private Bitmap mFace;

    private static int iCounter = 1;    //Used to keep track of Cards - My USE ONLY

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

    // Specific card with some display data
    Card(int pValue, int pSuit, boolean pFace, int pX, int pY)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;

        mCurrX = pX;
        mCurrY = pY;
        detectCollision = new Rect(mCurrX, mCurrY, mFace.getWidth(), mFace.getHeight());

        iCounter++;
    }

    // specific card with full display data
    Card(int pValue, int pSuit, boolean pFace, int pX, int pY, int prID, Context context)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;

        // Set display
        mresID = prID;
        setFaceMap(context);
        mCurrX = pX;
        mCurrY = pY;
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

    public Bitmap getFaceMap()
    {
        return mFace;
    }

    public Rect getDetectCollision()
    {
        return detectCollision;
    }

    public int getResID() { return mresID; }

    //Method to update card
    public void update()
    {

        detectCollision.left = mCurrX;
        detectCollision.top = mCurrY;
        detectCollision.right = mCurrX + mFace.getWidth();
        detectCollision.bottom = mCurrY + mFace.getHeight();
    }

}
