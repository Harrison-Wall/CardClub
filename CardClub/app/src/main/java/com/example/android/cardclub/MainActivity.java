package com.example.android.cardclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of games
        final ArrayList<GameInfo> games = new ArrayList<GameInfo>();
        games.add(new GameInfo(0, "Solitaire"));

        // TODO: Add more games

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
                    // Get the {@link Word} object at the given position the user clicked on
                    GameInfo currGame = games.get(position);

                    Intent gameIntent = getGameIntent(currGame.getID());

                    startActivity(gameIntent);
                }
            }
        );

    }

    public Intent getGameIntent(int gameNum)
    {
        Intent gameIntent = new Intent();

        switch(gameNum)
        {
            case 0:
                gameIntent = new Intent(MainActivity.this, Solitaire.class); // Solitaire
                break;
        }

        return gameIntent;
    }
}
