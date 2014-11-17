package com.example.lenovo.thejamroom.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.pojo.Song;

import java.util.ArrayList;



/**
 * Created by Avaneesh on 24/10/2014.
 */
public class SongsListAdapter extends ArrayAdapter<Song> {
    private final Activity context;
    ArrayList<Song> songs;
    private static LayoutInflater inflater=null;

    public SongsListAdapter(Activity context, ArrayList<Song> sngs){
        super(context, R.layout.songitem, sngs);
        this.context = context;
        this.songs = sngs;
    }

    static class ViewHolder{
        TextView songName;
        TextView songGenre;
        TextView songArtist;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        inflater = context.getLayoutInflater();
        View rowView = null;
        ViewHolder holder = new ViewHolder();
        rowView = inflater.inflate(R.layout.songitem, null, true);

        holder.songName = (TextView)rowView.findViewById(R.id.song_name);
        holder.songGenre = (TextView)rowView.findViewById(R.id.song_genre);
        holder.songArtist = (TextView)rowView.findViewById(R.id.song_artist);

        holder.songName.setText(songs.get(position).getName());
        holder.songGenre.setText(songs.get(position).getGenre());
        holder.songArtist.setText(songs.get(position).getArtist());

        return rowView;
    }


}
