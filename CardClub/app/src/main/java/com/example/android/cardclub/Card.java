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
    private Bitmap mFace, mBack;

    private static int iCounter = 1;    //Used to keep track of Cards - My USE ONLY

    /*
    * TEMP DELETE LATER
    * */
    private int speed = 0;

    //boolean variable to track the ship is boosting or not
    private boolean boosting;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    private Rect detectCollision;

    Card()
    {
        mID = iCounter;
        miValue = 1;
        miSuit = 0;
        mfaceUp = false;
        iCounter++;

        // TEMP DELETE LATER
        //setting the boosting value to false initially
        boosting = false;
        speed = 1;

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

    /**TEMP Delete Later**/
    Card(Context context, int pX, int pY)
    {
        mCurrY = 50;
        mCurrY = 75;
        setFaceMap(context, R.drawable.c_2);

        // Calculate Max Y
        maxY = pY - mFace.getHeight();

        minY = 0;

        boosting = false;

        detectCollision = new Rect(mCurrX, mCurrY, mFace.getWidth(), mFace.getHeight());
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

    public Bitmap getFaceMap()
    {
        return mFace;
    }

    public void setFaceMap(Context context, int resID)
    {
        mFace = BitmapFactory.decodeResource(context.getResources(), resID);
    }

    public void setBackMap(Context context, int resID)
    {
        mBack = BitmapFactory.decodeResource(context.getResources(), resID);
    }

    public Bitmap getBackMap()
    {
        return mBack;
    }


    /*********TEMPORARY WHILE LEARNING THIS SHIT*********/
    //Method to update coordinate of character
    public void update()
    {
        //if the ship is boosting
        if (boosting) {
            //speeding up the ship
            speed += 2;
        } else {
            //slowing down if not boosting
            speed -= 5;
        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        // Move card down
        mCurrY -= speed + GRAVITY;

        //but controlling it also so that it won't go off the screen
        if (mCurrY < minY)
        {
            mCurrY = minY;
        }

        if (mCurrY > maxY)
        {

            mCurrY = maxY;
        }

        detectCollision.left = mCurrX;
        detectCollision.top = mCurrY;
        detectCollision.right = mCurrX + mFace.getWidth();
        detectCollision.bottom = mCurrY + mFace.getHeight();
    }

    // get current speed
    public int getSpeed()
    {
        return speed;
    }

    //setting boosting true
    public void setBoosting() {
        boosting = true;
    }

    //setting boosting false
    public void stopBoosting() {
        boosting = false;
    }

    public Rect getDetectCollision()
    {
        return detectCollision;
    }

}
