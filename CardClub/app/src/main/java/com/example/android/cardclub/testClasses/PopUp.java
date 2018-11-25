package com.example.android.cardclub.testClasses;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.example.android.cardclub.R;

public class PopUp extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.popup_window);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        getWindow().setLayout((int)(width*.4), (int)(height*.4));
    }

}
