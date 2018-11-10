package com.example.android.cardclub;

public class Card
{
    private int mID;                     //1-52
    private int miValue;                 //1-13
    private int miSuit;                  //0-3
    private boolean mfaceUp;

    // Info for drawing
    private int mCurrX, mCurrY, mOldX, mOldY, mHieght, mWidth;

    private static int iCounter = 1;    //Used to keep track of Cards - My USE ONLY

    Card()
    {
        mID = iCounter;
        miValue = 1;
        miSuit = 0;
        mfaceUp = false;
        iCounter++;
    }

    Card(int pValue, int pSuit, boolean pFace)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;
        iCounter++;
    }

    Card(int pValue, int pSuit, boolean pFace, int pX, int pY, int pHeight, int pWidth)
    {
        mID = iCounter;
        miValue = pValue;
        miSuit = pSuit;
        mfaceUp = pFace;

        mCurrX = pX;
        mCurrY = pY;
        mHieght = pHeight;
        mWidth = pWidth;

        iCounter++;
    }


    /**
     * The ID is Used to keep track of Every Card.
     * @return ID (int)
     */
    public int getID()
    {
        return mID;
    }

    /**
     * Set the ID for a Card.
     * @param pID (int)
     */
    public void setID(int pID)
    {
        mID = pID;
    }

    /**
     * Get the card's suit.
     * @return iSuit (int)
     */
    public int getSuit()
    {
        return miSuit;
    }

    /**
     * Manually set the suit for individual card creation.
     * @param pSuit (int)
     */
    public void setSuit(int pSuit)
    {
        miSuit = pSuit;
    }

    /**
     * Get the card's face value.
     * @return iValue (int)
     */
    public int getValue()
    {
        return miValue;
    }

    /**
     * Set the card's face value.
     * @param pValue (int)
     */
    public void setValue(int pValue)
    {
        miValue = pValue;
    }

    /**
     * Checks if the card is face-up or face-down
     * @return faceUp (boolean)
     */
    public boolean isFaceUp()
    {
        return mfaceUp;
    }

    /**
     * Switches the value in faceUp
     */
    public void turnOver()
    {
        mfaceUp = !mfaceUp;
    }

    public void turnUp()
    {
        mfaceUp = true;
    }

    public void turnDown()
    {
        mfaceUp = false;
    }

    /**
     * Determine if the card is red or black.
     * @return bRed (boolean)
     */
    public boolean isRed()
    {
        return (miSuit == 1 || miSuit == 2);
    }

    /**
     * Resets the ID Counter to 1
     */
    public static void resetCounter()
    {
        iCounter = 1;
        return;
    }

    public int getCurrX()
    {
        return mCurrX;
    }

    public void setmCurrX(int pX)
    {
        mOldX = mCurrX;
        mCurrX = pX;
    }

    public int getCurrY()
    {
        return mCurrY;
    }

    public void setCurrY(int pY)
    {
        mOldY = mCurrY;
        mCurrY = pY;
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

    public void setHieght(int pHiehgt)
    {
        mHieght = pHiehgt;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public void setmWidth(int pWidth)
    {
        mWidth = pWidth;
    }

}
