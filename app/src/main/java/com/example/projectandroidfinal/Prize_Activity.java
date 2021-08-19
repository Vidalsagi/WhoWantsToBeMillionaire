package com.example.projectandroidfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Prize_Activity extends AppCompatActivity {
    String prize;

    /**
     * onCreate()
     * load the animation that represent the prize amount
     * close the activity after 4 seconds with a handler
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_prize_);

        MediaPlayer backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_applause);
        backgroundMusic.start();

        prize = getIntent().getStringExtra("prize");
        ImageView imageView=(ImageView)findViewById(R.id.logo_hit);

        if(prize.equals("1")) imageView.setBackgroundResource(R.drawable.animation100);
        if(prize.equals("2")) imageView.setBackgroundResource(R.drawable.animation200);
        if(prize.equals("3")) imageView.setBackgroundResource(R.drawable.animation300);
        if(prize.equals("4")) imageView.setBackgroundResource(R.drawable.animation400);
        if(prize.equals("5")) imageView.setBackgroundResource(R.drawable.animation500);
        if(prize.equals("6")) imageView.setBackgroundResource(R.drawable.animation600);
        if(prize.equals("7")) imageView.setBackgroundResource(R.drawable.animation700);
        if(prize.equals("8")) imageView.setBackgroundResource(R.drawable.animation800);
        if(prize.equals("9")) imageView.setBackgroundResource(R.drawable.animation900);
        if(prize.equals("10")) imageView.setBackgroundResource(R.drawable.animation010);
        if(prize.equals("11")) imageView.setBackgroundResource(R.drawable.animation011);
        if(prize.equals("12")) imageView.setBackgroundResource(R.drawable.animation012);
        if(prize.equals("13")) imageView.setBackgroundResource(R.drawable.animation013);
        if(prize.equals("14")) imageView.setBackgroundResource(R.drawable.animation014);
        if(prize.equals("15")) imageView.setBackgroundResource(R.drawable.animation015);


        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();

            }
        }, 4000);


    }
}