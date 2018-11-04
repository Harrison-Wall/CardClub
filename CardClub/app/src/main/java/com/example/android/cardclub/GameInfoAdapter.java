package com.example.android.cardclub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GameInfoAdapter extends ArrayAdapter <GameInfo>
{

    public GameInfoAdapter(Context context, ArrayList<GameInfo> games, ViewGroup parent)
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

        // get the info the the game at the current position
        GameInfo currentGame = getItem(position);

        





        return super.getView(position, convertView, parent);
    }
}
