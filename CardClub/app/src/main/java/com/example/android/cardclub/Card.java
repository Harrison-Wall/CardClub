package com.example.android.cardclub;

public class Card
{
    private int ID;                     //1-52
    private int iValue;                 //1-13
    private int iSuit;                  //0-3
    private boolean faceUp;
    private static int iCounter = 1;    //Used to keep track of Cards - My USE ONLY

    Card()
    {
        ID = iCounter;
        iValue = 1;
        iSuit = 0;
        faceUp = false;
        iCounter++;
    }

    Card(int pValue, int pSuit, boolean pFace)
    {
        ID = iCounter;
        iValue = pValue;
        iSuit = pSuit;
        faceUp = pFace;
        iCounter++;
    }

    /**
     * The ID is Used to keep track of Every Card.
     * @return ID (int)
     */
    public int getID()
    {
        return ID;
    }

    /**
     * Set the ID for a Card.
     * @param pID (int)
     */
    public void setID(int pID)
    {
        ID = pID;
    }

    /**
     * Get the card's suit.
     * @return iSuit (int)
     */
    public int getSuit()
    {
        return iSuit;
    }

    /**
     * Manually set the suit for individual card creation.
     * @param pSuit (int)
     */
    public void setSuit(int pSuit)
    {
        iSuit = pSuit;
    }

    /**
     * Get the card's face value.
     * @return iValue (int)
     */
    public int getValue()
    {
        return iValue;
    }

    /**
     * Set the card's face value.
     * @param pValue (int)
     */
    public void setValue(int pValue)
    {
        iValue = pValue;
    }

    /**
     * Checks if the card is face-up or face-down
     * @return faceUp (boolean)
     */
    public boolean isFaceUp()
    {
        return faceUp;
    }

    /**
     * Switches the value in faceUp
     */
    public void turnOver()
    {
        faceUp = !faceUp;
    }

    /**
     * Determine if the card is red or black.
     * @return bRed (boolean)
     */
    public boolean isRed()
    {
        return (iSuit == 1 || iSuit == 2);
    }

    /**
     * Resets the ID Counter to 1
     */
    public static void resetCounter()
    {
        iCounter = 1;
        return;
    }

}
