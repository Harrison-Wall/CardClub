package com.example.android.cardclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of games

        //get the list view

        //set the adapter

        //set on item click listner to start up the game's activity

            // have to do this through intents (call function)
    }


    public void startGameIntent(int gameNum)
    {
        Intent gameIntent = new Intent();

        switch(gameNum)
        {
            case 0:
                break;
            default:
                gameIntent = new Intent(MainActivity.this, MainActivity.class); // Stay Here
                break;
        }

        startActivity(gameIntent);
    }
}
