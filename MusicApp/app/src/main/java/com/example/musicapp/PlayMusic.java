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
        //gán tổng thời gian của bài hát cho seekbar
        seekBar.setMax(mediaPlayer.getDuration());
    }
    public void setTimePlay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                timePlay.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());//seekbar chạy theo thời gian bài hát

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

                handler.postDelayed(this, 500); //chạy lại hàm run, 500 là thời gian cập nhật lại 0.5 giây,
                // cứ mỗi 0.5 giây sẽ cập nhật lại thời gian đang phát bài hát

            }
        }, 100); //100 là tham số delay, khi click tới đâu, delay tới đó (trong khoảng 1/10 giây) rồi chạy luôn
    }
    public void clickButton (){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){ //đang phát nhạc -> dừng -> đổi nút pause thành play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else{ //đang ngừng -> phát -> đổi nút play thành pause
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
                //khi kéo thanh seekbar liên tục thì sẽ cập nhật giá trị liên tục

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //khi vừa chạm vào vị trí nào thì sẽ lấy mốc thời gian ở vị trí đó

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //khi kéo thanh seekbar và thả ra tại vị trí nào đó thì sẽ lấy giá trị tại vị trí đó
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
}