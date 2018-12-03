/*
 * Harrison Wall
 * 2018
 */

package com.example.android.cardclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

// Displays the games list, and moves user between them
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of games
        final ArrayList<GameInfo> games = new ArrayList<GameInfo>();
        games.add(new GameInfo(0, "Black Jack"));
        games.add(new GameInfo(1, "Solitaire"));
        games.add(new GameInfo(2, "War"));

        //set the adapter
        GameInfoAdapter myAdapter = new GameInfoAdapter(this, games);

        //get the list view
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(myAdapter);

        //set on item click listner to start up the game's activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
                {
                    // Get the list item the user clicked on
                    GameInfo currGame = games.get(position);

                    // Determine which activity to run, and start it
                    Intent gameIntent = getGameIntent(currGame.getID());
                    startActivity(gameIntent);
                }
            }
        );

    }

    // Build the intent, based on the game selected
    public Intent getGameIntent(int gameNum)
    {
        Intent gameIntent = new Intent();

        switch(gameNum)
        {
            case 0:
                gameIntent = new Intent(MainActivity.this, BlackJack.class); // BlackJack
                break;
            case 1:
                gameIntent = new Intent(MainActivity.this, Solitaire.class); // Solitaire
                break;
            case 2:
                gameIntent = new Intent(MainActivity.this, War.class); // War
                break;
        }

        return gameIntent;
    }
}
