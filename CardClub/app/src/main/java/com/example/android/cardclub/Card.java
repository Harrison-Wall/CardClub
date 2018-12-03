/*
 * Harrison Wall
 * 2016-2018
 */

package com.example.android.cardclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Card class, represents a card from a standing deck
 */
public class Card
{
    private int mID;                     //1-52
    private int miValue;                 //1-13 Ace->King
    private int miSuit;                  //0-3  Clubs, Diamonds, Hearts, Spades
    private boolean mfaceUp;

    // Info for drawing
    private int mCurrX, mCurrY;
    private int mfaceID, mbackID;
    private Bitmap mFaceMap, mBackMap;
    private BitmapFactory.Options mOps;

    private static int iCounter = 1;    //Used to make the Card ID (ensure uniqueness)

    private Rect detectCollision;   // Rectangle for collision detection

    /**
     * Constructor, no arguments text based.
     */
    Card()
    {
        mID = iCounter;
        miValue = 1;
        miSuit = 0;
        mfaceUp = false;
        iCounter++;
    }

    /**
     * Constructor for making a specific card
     * @param pValue card face value 1-13 to A-K
     * @param  pSuit is which suit 0-3 for Clubs, Diamonds, Hearts, Spades
     * @param  pFace up (true) or down (false)
     */
    Card(int pValue, int pSuit, boolean pFace)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;
        iCounter++;
    }

    /**
     * Constructor for making a specific card with bitmap support
     * @param pValue card face value 1-13 to A-K
     * @param pSuit is which suit 0-3 for Clubs, Diamonds, Hearts, Spades
     * @param pFace up (true) or down (false)
     * @param pX horizontal location
     * @param pY vertical location
     * @param prID resource ID for the cards face
     * @param context where is will be drawn
     * @param pDensity the size of the card, higher is smaller
     */
    Card(int pValue, int pSuit, boolean pFace, int pX, int pY, int prID, Context context, int pDensity)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;

        // Set display
        mbackID = R.drawable.blue_back; // Could be made into a variable so user could change it
        mfaceID = prID;

        mOps = new BitmapFactory.Options();
        mOps.inDensity = pDensity;

        setFaceMap(context);
        setBackMap(context);

        mCurrX = pX;
        mCurrY = pY;
        detectCollision = new Rect(mCurrX, mCurrY, mFaceMap.getWidth(), mFaceMap.getHeight());

        iCounter++;
    }

    // Setters
    // Creates the front of the card's bitmap using the context, faceID, and density
    public void setFaceMap(Context context)
    {
        mFaceMap = BitmapFactory.decodeResource(context.getResources(), mfaceID, mOps);
    }

    // Creates the back of the card's bitmap using the context, faceID, and density
    public void setBackMap(Context context)
    {
        mBackMap = BitmapFactory.decodeResource(context.getResources(), mbackID, mOps);
    }

    // Sets the coordinate of the card and the collision rectangle
    public void setLocation(int pX, int pY)
    {
        mCurrX = pX;
        mCurrY = pY;

        update(); // update rectangle location
    }

    // Only updates the X coordinate
    public void setX(int pX)
    {
        mCurrX = pX;
        update();
    }

    // Only updates the Y coordinate
    public void setY(int pY)
    {
        mCurrY = pY;
        update();
    }

    public void turnUp() { mfaceUp = true; }

    public void turnDown() { mfaceUp = false; }

    public static void resetCounter() { iCounter = 1; }

    public void setSuit(int pSuit) { miSuit = pSuit; }

    public void setID(int pID) { mID = pID; }

    public void setValue(int pValue) { miValue = pValue; }

    // Getters

    /**
     * Checks if the card is Red or Black
     * @return true if Red (Diamond, Heart) false if Black (Club, Spade)
     */
    public boolean isRed()
    {
        return (miSuit == 1 || miSuit == 2);
    }

    public boolean isFaceUp() { return mfaceUp; }

    public int getID() { return mID; }

    public int getSuit() { return miSuit; }

    public int getValue() { return miValue; }

    public int getX() { return mCurrX; }

    public int getY() { return mCurrY; }

    public int getFaceID() { return mfaceID; }

    public int getBackID() { return  mbackID; }

    public Bitmap getFaceMap() { return mFaceMap; }

    public Bitmap getBackMap() { return mBackMap; }

    public Rect getDetectCollision() { return detectCollision; }

    // Updates the rectangles coordinates based on the cards
    public void update()
    {
        detectCollision.left = mCurrX;
        detectCollision.top = mCurrY;
        detectCollision.right = mCurrX + mFaceMap.getWidth();
        detectCollision.bottom = mCurrY + mFaceMap.getHeight();
    }

}
