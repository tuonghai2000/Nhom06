package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    // Song list và layout
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    // Constructor
    public SongAdapter(Context c, ArrayList<Song> theSongs) {
        songs = theSongs;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Ánh xạ đến layout mỗi bài
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);

        TextView songView = (TextView) songLayout.findViewById(R.id.tv_music_name);
        TextView artistView = (TextView) songLayout.findViewById(R.id.tv_music_subtitle);
        ImageView imageView = (ImageView) songLayout.findViewById(R.id.iv_music_list);

        // Lấy bài hát hiện
        Song currentSong = songs.get(position);

        // Lấy tên tiêu đề và tác
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getPath());

        // Cài đặt tag cho mỗi bài là vị trí của mỗi
        songLayout.setTag(position);
        return songLayout;
    }
}
