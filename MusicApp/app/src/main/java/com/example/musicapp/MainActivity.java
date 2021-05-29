 package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST = 1;
    ArrayList<Song> arrayList;
    public static ArrayList<Song> arraySong = new ArrayList<>();
    ListView listView;
    SongAdapter adapter;

    Uri songuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST);
            }
        } else {
            doStuff();
        }
    }

    public void doStuff(){
        listView = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new SongAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = arraySong.get(position);
                Intent intent = new Intent(MainActivity.this, PlayMusic.class);

                intent.putExtra("path", song.getPath());
                intent.putExtra("title", song.getTitle());
                intent.putExtra("position", String.valueOf(position));
                startActivity(intent);

            }
        });
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(songuri, null, null, null, null);
        if(songcursor != null && songcursor.moveToFirst()){
            int songtitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songData = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String currentTitle = songcursor.getString(songtitle);
                String currentArtist = songcursor.getString(songArtist);
                String currentData= songcursor.getString(songData);
                if(currentData.contains("mp3") && (currentData.contains("Download"))){
                    arrayList.add(new Song(currentTitle, currentArtist));
                    arraySong.add(new Song(currentTitle, currentData));
                }


            } while (songcursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}