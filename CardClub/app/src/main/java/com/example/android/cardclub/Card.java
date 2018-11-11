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
    private int mfaceID, mbackID;
    private Bitmap mFaceMap, mBackMap;

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
        mbackID = R.drawable.blue_back;

        mCurrX = pX;
        mCurrY = pY;
        detectCollision = new Rect(mCurrX, mCurrY, mFaceMap.getWidth(), mFaceMap.getHeight());

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
        mbackID = R.drawable.blue_back;
        mfaceID = prID;

        setFaceMap(context);
        setBackMap(context);

        mCurrX = pX;
        mCurrY = pY;
        detectCollision = new Rect(mCurrX, mCurrY, mFaceMap.getWidth(), mFaceMap.getHeight());

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


    public void setFaceMap(Context context) { mFaceMap = BitmapFactory.decodeResource(context.getResources(), mfaceID); }

    public void setBackMap(Context context) { mBackMap = BitmapFactory.decodeResource(context.getResources(), mbackID); }

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

    public Bitmap getFaceMap() { return mFaceMap; }

    public Bitmap getBackMap() { return mBackMap; }

    public Rect getDetectCollision()
    {
        return detectCollision;
    }

    //Method to update card
    public void update()
    {

        detectCollision.left = mCurrX;
        detectCollision.top = mCurrY;
        detectCollision.right = mCurrX + mFaceMap.getWidth();
        detectCollision.bottom = mCurrY + mFaceMap.getHeight();
    }

}
