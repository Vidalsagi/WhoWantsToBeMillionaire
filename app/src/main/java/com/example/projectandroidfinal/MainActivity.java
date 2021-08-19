package com.example.projectandroidfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    String difficult[] = {"EASY","MEDIUM","HARD"};
    EditText name;
    ImageView soundBottun;
    boolean isSound ;
    MediaPlayer backgroundMusic;

    /**
     * onResume()
     * start the menu music
     */
    @Override
    protected void onResume() {
        super.onResume();
        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_bg);
        backgroundMusic.start();
        backgroundMusic.setLooping(true);
        isSound = true;
    }

    /**
     * onPause()
     * stop the menu music
     */
    @Override
    protected void onPause() {
        super.onPause();
        isSound = false;
        backgroundMusic.stop();
        backgroundMusic.setLooping(false);
    }

    /**
     * onCreate()
     * @param savedInstanceState
     * create 4 buttons
     * buttonStart - start new activity and pass the player name to the difficult activity
     * highScoreButton - start the high score activity
     * soundButton - stop and resume the background sound
     * rulesButton - open the rules layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        soundBottun = (ImageView)findViewById(R.id.sound_button);
        name = (EditText)findViewById(R.id.name_text);
        Button buttonStart = (Button)findViewById(R.id.START_BUTTON);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundMusic.setLooping(false);
                backgroundMusic.setVolume(0,0);
                Intent intent = new Intent(getBaseContext(), Difficult_Activity.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("sound",isSound);
                startActivity(intent);
            }
        });

        soundBottun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSound == true){
                    isSound =false;
                    backgroundMusic.setVolume(0,0);
                    soundBottun.setImageDrawable(getResources().getDrawable(R.drawable.soundoff));
                }
                else{
                    isSound = true;
                    backgroundMusic.setVolume(1,1);
                    soundBottun.setImageDrawable(getResources().getDrawable(R.drawable.soundon));
                }

            }
        });


        Button highScore = (Button)findViewById(R.id.high_score_button);
        highScore.setText(getResources().getString(R.string.HIGH_SCORE_BUTTON));
        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), High_Score_Activity.class);
                startActivity(intent);
            }
        });

        Button rules = (Button)findViewById(R.id.game_rules_button);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                View view = layoutInflaterAndroid.inflate(R.layout.activity_game_rules, null);
                builder.setView(view);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                view.findViewById(R.id.return_to_main).setOnClickListener(l -> alertDialog.dismiss());

            }
        });

        rotationIcon();
    }

    /**
     * rotationIcon()
     * rotate and scale the main logo icon
     */
    void rotationIcon(){
        ImageView simpleImageView = (ImageView)findViewById(R.id.logo_hit);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(simpleImageView, "scaleX", 0.01f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(simpleImageView, "scaleY", 0.01f);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(simpleImageView, "scaleX", 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(simpleImageView, "scaleY", 1f);

        scaleUpX.setDuration(3000);
        scaleUpY.setDuration(3000);

        ObjectAnimator rotationDownX = ObjectAnimator.ofFloat(simpleImageView, "rotationX", 360);

        rotationDownX.setDuration(5000);
        rotationDownX.start();

        scaleDownX.setDuration(1);
        scaleDownY.setDuration(1);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.play(scaleUpX).after(scaleDownX);
        scaleDown.play(scaleUpY).with(scaleUpX);
        scaleDown.start();

    }
}