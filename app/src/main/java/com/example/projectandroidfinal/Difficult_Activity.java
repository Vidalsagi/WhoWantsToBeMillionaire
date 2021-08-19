package com.example.projectandroidfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class Difficult_Activity extends AppCompatActivity {
    ImageView easy,hard,medium,people,geo,programming;
    Button button;
    String name;
    int diff = 1;
    int category = 1;
    boolean isSound;

    /**
     * onCreate()
     * @param savedInstanceState
     * create 3 difficult buttons for easy,medium and hard
     * create 3 category buttons for persons,geography and programming
     * pass to Questions activity the difficult and the category and start it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_difficult_);


        name = getIntent().getStringExtra("name");
        isSound = getIntent().getBooleanExtra("sound",true);
        easy = (ImageView)findViewById(R.id.easy);
        hard = (ImageView)findViewById(R.id.hard);
        medium = (ImageView)findViewById(R.id.medium);
        people = (ImageView)findViewById(R.id.people);
        programming = (ImageView)findViewById(R.id.programming);
        geo = (ImageView)findViewById(R.id.geo);
        button = (Button)findViewById(R.id.startQuestionsButton);

        MediaPlayer backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_bg);
        backgroundMusic.setVolume(0,0);

        set_diff();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Question_Activity.class);
                intent.putExtra("difficult", String.valueOf(diff));
                intent.putExtra("category", String.valueOf(category));
                intent.putExtra("name",name);
                intent.putExtra("sound",isSound);
                startActivity(intent);
            }
        });


        easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                diff = 1;
                set_diff();
            }});

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff = 3;
                set_diff();
            }});

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff = 2;
                set_diff();
            }});

        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 1;
                set_diff();
            }});

        programming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 2;
                set_diff();
            }});

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 3;
                set_diff();
            }});

    }

    /**
     * set the green and red borders to the selected buttons
     */
    void set_diff(){
        easy.setPadding(0,0,0,0);
        medium.setPadding(0,0,0,0);
        hard.setPadding(0,0,0,0);
        people.setPadding(0,0,0,0);
        geo.setPadding(0,0,0,0);
        programming.setPadding(0,0,0,0);

        if (diff == 1) easy.setPadding(15,15,15,15);
        if (diff == 2) medium.setPadding(15,15,15,15);
        if (diff == 3) hard.setPadding(15,15,15,15);
        if (category == 1) people.setPadding(15,15,15,15);
        if (category == 2) programming.setPadding(15,15,15,15);
        if (category == 3) geo.setPadding(15,15,15,15);
    }



}