/*
 * Harrison Wall
 * 2018
 * Help with views from : https://www.simplifiedcoding.net/android-game-development-tutorial-1/#Building-Game-View
 */

package com.example.android.cardclub;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

public class Solitaire extends Activity
{
    private SolitaireView solitaireView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        // Get display metrics for configuring proper size and spacing of cards
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        //Initializing game view object
        solitaireView = new SolitaireView(this, size.x, size.y, dm);

        //adding it to ContentView
        setContentView(solitaireView);
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause()
    {
        super.onPause();
        solitaireView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume()
    {
        super.onResume();
        solitaireView.resume();
    }
}