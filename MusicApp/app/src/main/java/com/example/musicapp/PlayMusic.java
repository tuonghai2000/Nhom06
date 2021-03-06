package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlayMusic extends AppCompatActivity {
    TextView nameSong, timePlay, timeTotal;
    SeekBar seekBar;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    ImageView imageView_cd;
    Button btn;
    MediaPlayer mediaPlayer;
    String title = "";
    String path = "";
    ArrayList<Song> songArrayList = MainActivity.arraySong;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent intent = this.getIntent();
        title = getIntent().getStringExtra("title");
        path = getIntent().getStringExtra("path");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        Anhxa();
        setMediaPlayer(title,path);
        clickButton();
        setSeekBar();


    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }
    public void Anhxa(){
        timePlay = findViewById(R.id.timeplay);
        timeTotal = findViewById(R.id.timetotal);
        nameSong = findViewById(R.id.namesong);
        seekBar = findViewById(R.id.seekbar);
        btnNext = findViewById(R.id.btnnext);
        btnPrev = findViewById(R.id.btnprev);
        btnPlay = findViewById(R.id.btnplay);
    }
    public void setMediaPlayer(String title, String path){
        mediaPlayer = MediaPlayer.create(PlayMusic.this, Uri.parse(path));
        nameSong.setText(title);
        mediaPlayer.start();
        mediaPlayer.seekTo(seekBar.getProgress());
        setTimePlay();
        setTimeTotal();
    }
    public void setTimeTotal(){
        SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
        timeTotal.setText(formatTime.format(mediaPlayer.getDuration()));
        //g??n t???ng th???i gian c???a b??i h??t cho seekbar
        seekBar.setMax(mediaPlayer.getDuration());
    }
    public void setTimePlay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                timePlay.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());//seekbar ch???y theo th???i gian b??i h??t

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position ++;
                        if(position > songArrayList.size() -1){
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        setMediaPlayer(songArrayList.get(position).getTitle(), songArrayList.get(position).getPath());
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                        setTimeTotal();
                        setTimePlay();
                    }
                });

                handler.postDelayed(this, 500); //ch???y l???i h??m run, 500 l?? th???i gian c???p nh???t l???i 0.5 gi??y,
                // c??? m???i 0.5 gi??y s??? c???p nh???t l???i th???i gian ??ang ph??t b??i h??t

            }
        }, 100); //100 l?? tham s??? delay, khi click t???i ????u, delay t???i ???? (trong kho???ng 1/10 gi??y) r???i ch???y lu??n
    }
    public void clickButton (){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){ //??ang ph??t nh???c -> d???ng -> ?????i n??t pause th??nh play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else{ //??ang ng???ng -> ph??t -> ?????i n??t play th??nh pause
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                setTimePlay();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 1){
                    position ++;
                    if(position > songArrayList.size() -1){
                        position = 0;
                    }
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    setMediaPlayer(songArrayList.get(position).getTitle(), songArrayList.get(position).getPath());
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                setTimePlay();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 1){
                    position --;
                    if(position < 0){
                        position = songArrayList.size() - 1;
                    }
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    setMediaPlayer(songArrayList.get(position).getTitle(), songArrayList.get(position).getPath());
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                setTimePlay();
            }
        });
    }
    public void setSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //khi k??o thanh seekbar li??n t???c th?? s??? c???p nh???t gi?? tr??? li??n t???c

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //khi v???a ch???m v??o v??? tr?? n??o th?? s??? l???y m???c th???i gian ??? v??? tr?? ????

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //khi k??o thanh seekbar v?? th??? ra t???i v??? tr?? n??o ???? th?? s??? l???y gi?? tr??? t???i v??? tr?? ????
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
}