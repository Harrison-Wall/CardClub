/*
 * Harrison Wall
 * 2018
 */

package com.example.android.cardclub;

// Contains and ID and Name for a game
public class GameInfo
{
    private int ID; // Should be Unique, Used to start correct activity
    private String name;

    public GameInfo()
    {
        ID = -1; // Considered Invalid (Will default)
        name = "Game Name";
    }

    public GameInfo(int gNum, String gName)
    {
        ID = gNum;
        name = gName;
    }

    // getters and setters
    public int getID()
    {
        return ID;
    }

    public String getName()
    {
        return name;
    }

    public void setID(int gNum) { ID = gNum; }

    public void setName(String gName) { name = gName; }
}
