/*
 * Harrison Wall
 * 2018
 */


package com.example.android.cardclub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// Builds GameInfo list items for the main activity
public class GameInfoAdapter extends ArrayAdapter <GameInfo>
{
    public GameInfoAdapter(Context context, ArrayList<GameInfo> games)
    {
        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;

        if(listItemView == null) // If the view is not already in use
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false); // inflate
        }

        // get the info for the game at the current position
        GameInfo currentGame = getItem(position);

        TextView gameName = (TextView) listItemView.findViewById(R.id.game_name); // get the game name view from the list item

        gameName.setText( currentGame.getName() ); // set the name

        // TODO: seperate Colors, Images, Icons

        return listItemView;
    }
}
