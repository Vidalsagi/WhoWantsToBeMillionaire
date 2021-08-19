package com.example.projectandroidfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Question_Activity extends AppCompatActivity {
    int numberOfQuestions = 0, timerTime = 30, globalTimer  = 0;
    int prize[] = {0,100,200,300,500,1000,2000,4000,8000,16000,32000,64000,125000,250000,500000,1000000};
    int checkFifty[] = {0, 0};

    boolean stopTimer = false, isChart = false, isSound;

    Integer cards[] = {1,2,3,4};
    Integer questionsOrder[] = new Integer[30];

    String name, difficult, category;
    String questionMatrix[][];
    String diff[] = {"easy","medium","hard"};

    Button button[] =  new Button[4];
    Button helpWheels[] =  new Button[3];
    TextView text, timer, globalTimerText, money, helpWheelText;
    PieChart pieChart;
    PieData pieData;
    List<PieEntry> pieEntryList = new ArrayList<>();
    MediaPlayer backgroundMusic,timeMusic;
    Handler mHandler;
    SharedPreferences sp;

    /**
     * onPause()
     * stop and exit the counting thread
     */
    @Override
    protected void onPause() {
        super.onPause();
        timerTime = -1;
    }

    /**
     * onDestroy()
     * stop back ground music
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundMusic.stop();
    }

    /**
     * onResume()
     * start the 30 sec back counting each time the game getting a new question
     */
    @Override
    protected void onResume() {
        super.onResume();
        int time = 3000;
        if (numberOfQuestions == 0) time = 6000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        },time);
    }

    /**
     * onCreate()
     * @param savedInstanceState
     * start the background music
     * hide the ActionBar
     * create the question order array
     * getting the difficult,category and name from the previous Activity
     * create the question matrix with creatematrix() function
     * call the help Wheels by functions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_start_game);
        backgroundMusic.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_question_);

        mHandler=new Handler();

        for(int i=0;i<30;i++)questionsOrder[i]=i;
        Collections.shuffle(Arrays.asList(questionsOrder));

        difficult = getIntent().getStringExtra("difficult");
        category = getIntent().getStringExtra("category");
        name = getIntent().getStringExtra("name");
        isSound = getIntent().getBooleanExtra("sound",true);

        text = (TextView)findViewById(R.id.question_text);
        timer = (TextView)findViewById(R.id.timer);
        globalTimerText = (TextView)findViewById(R.id.globalTimer);
        money = (TextView)findViewById(R.id.money);

        questionMatrix = creatematrix();
        text.setText(questionMatrix[0][0]);

        button[0] = (Button)findViewById(R.id.answer_1);
        button[1] = (Button)findViewById(R.id.answer_2);
        button[2] = (Button)findViewById(R.id.answer_3);
        button[3] = (Button)findViewById(R.id.answer_4);

        helpWheels[0] = (Button)findViewById(R.id.fiftyfify);
        helpWheels[1] = (Button)findViewById(R.id.phone_button);
        helpWheels[2] = (Button)findViewById(R.id.crowd_button);

        helpWheelText = (TextView)findViewById(R.id.cellphone);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setNoDataText("");

        onFiftyfifty();
        cellphoneWheel();
        crowdWheel();
        checkIfCorrect();

    }

    /**
     *checkIfCorrect()
     *set for each button onClick method
     * when a answer button is clicked:
     * 1.set all the answer buttons to enabaled false
     * 2.set all the answer buttons to text color white
     * 3.check if the button is the correct answer by comparing with the original right answer
     * start a new handler of the animation delay
     * start a second handler for the correct or incorrect answer
     * if the chosen answer is correct,start the prize activity and load a new question
     * if the chosen answer is incorrect,close the activity ,run alert dialog,save the score in the high score list,and close the activity
     */
    void checkIfCorrect(){
        for(int i=0; i<4; i++){
            int temp = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                int isCorrect;
                @Override
                public void onClick(View v) {
                    MediaPlayer bg = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_final_answer);
                    bg.start();
                    stopTimer = true;
                    button[0].setEnabled(false);
                    button[1].setEnabled(false);
                    button[2].setEnabled(false);
                    button[3].setEnabled(false);
                    button[0].setTextColor(Color.WHITE);
                    button[1].setTextColor(Color.WHITE);
                    button[2].setTextColor(Color.WHITE);
                    button[3].setTextColor(Color.WHITE);
                    if(button[temp].getText() == questionMatrix[questionsOrder[numberOfQuestions]][1]) isCorrect = 1;
                    else isCorrect = 0;
                    button[temp].setBackground(getDrawable(R.drawable.shape_blink_button));
                    Animation animation = AnimationUtils.loadAnimation(Question_Activity.this,R.anim.bottun_animation);
                    button[temp].startAnimation(animation);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button[temp].setBackground(getDrawable(R.drawable.shape_incorrect_button));
                            button[temp].clearAnimation();
                                if(isCorrect == 1){
                                    button[temp].setBackground(getDrawable(R.drawable.shape_check_button));
                                    MediaPlayer bg = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_correct_answer);
                                    bg.start();
                                }
                                if(isCorrect == 0){
                                    MediaPlayer bg = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_wrong_answer);
                                    bg.start();
                                    for(int i=0;i<4;i++)
                                        if(button[i].getText()== questionMatrix[questionsOrder[numberOfQuestions]][1])
                                            button[i].setBackground(getDrawable(R.drawable.shape_check_button));
                                }
                            Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(isCorrect == 1){
                                            numberOfQuestions++;
                                            button[0].setBackground(getDrawable(R.drawable.shape_reguler_button));
                                            button[1].setBackground(getDrawable(R.drawable.shape_reguler_button));
                                            button[2].setBackground(getDrawable(R.drawable.shape_reguler_button));
                                            button[3].setBackground(getDrawable(R.drawable.shape_reguler_button));
                                            Intent intent = new Intent(getBaseContext(), Prize_Activity.class);
                                            intent.putExtra("prize", String.valueOf(numberOfQuestions));
                                            startActivity(intent);
                                            loadNewQuestion();
                                        }
                                        else endGame();
                                    }
                                },3000);
                        }
                    },3000);
                }
            });
        }
        loadNewQuestion();
    }

    /**
     * loadNewQuestion()
     * each time the loadNewQuestion function is called,the function reset all the previos actions of the last question,
     * like question and answer text and enable back the answers buttons
     * the function shuffle the arrayList of answer so each time the correct answer wont be on the same place
     * we used handler to delay the appearance of the correct answer after retuning from the prize activity
     */
    void loadNewQuestion(){

        checkFifty[0] = 0;
        checkFifty[1] = 0;
        stopTimer = true;
        text.setText("");
        button[0].setText(""); button[1].setText(""); button[2].setText(""); button[3].setText(""); money.setText("");
        button[0].setEnabled(false); button[1].setEnabled(false); button[2].setEnabled(false); button[3].setEnabled(false);

        if(numberOfQuestions == 15){
            timerTime = -1;
            endGame();
            return;
        }


        if(numberOfQuestions==0){
            helpWheels[0].setEnabled(false);
            helpWheels[1].setEnabled(false);
            helpWheels[2].setEnabled(false);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopTimer=false;
                timerTime = 31;
                Collections.shuffle(Arrays.asList(cards));

                text.setText(questionMatrix[questionsOrder[numberOfQuestions]][0]);
                button[0].setText(questionMatrix[questionsOrder[numberOfQuestions]][cards[0]]);
                button[1].setText(questionMatrix[questionsOrder[numberOfQuestions]][cards[1]]);
                button[2].setText(questionMatrix[questionsOrder[numberOfQuestions]][cards[2]]);
                button[3].setText(questionMatrix[questionsOrder[numberOfQuestions]][cards[3]]);

                button[0].setEnabled(true);
                button[1].setEnabled(true);
                button[2].setEnabled(true);
                button[3].setEnabled(true);

                helpWheelText.setText("");
                helpWheelText.setBackgroundColor(0);
                text.setText(questionMatrix[questionsOrder[numberOfQuestions]][0]);
                money.setText(String.valueOf(prize[numberOfQuestions]));
                clearChart();

                if(numberOfQuestions==0){
                    helpWheels[0].setEnabled(true);
                    helpWheels[1].setEnabled(true);
                    helpWheels[2].setEnabled(true);
                }
            }
        },6000);
    }


    /**
     * onFiftyfifty()
     * choose 2 incorrect answers and disable them from the answers buttons
     */
    public void onFiftyfifty() {
        helpWheels[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChart();
                helpWheels[0].setEnabled(false);
                int count = 2;
                for (int i = 0; i < 4; i++) {
                    if (button[i].getText() != questionMatrix[questionsOrder[numberOfQuestions]][1] && count != 0) {
                        button[i].setEnabled(false);
                        button[i].setBackground(getDrawable(R.drawable.shape_black_button));
                        checkFifty[--count] = cards[i];
                    }
                }
            }
        });
    }

    /**
     * cellphoneWheel()
     * ask a friend on a cellphone(possibly) the correct answer
     */
    public void cellphoneWheel() {
        helpWheels[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChart();
                helpWheels[1].setEnabled(false);
                ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(1,1,1,1,2,3,4));
                Iterator itr = al.iterator();
                while (itr.hasNext()) {
                    int x = (Integer)itr.next();
                    if ((x == checkFifty[0] || x == checkFifty[1]) && x != 1)
                        itr.remove();
                }
                Collections.shuffle(al);
                helpWheelText.setText(getResources().getString(R.string.CORRECT_ANSWER) +" " + questionMatrix[questionsOrder[numberOfQuestions]][al.get(0)]);
                helpWheelText.setBackgroundColor(Color.BLACK);
            }
        });
    }

    /**
     *crowdWheel()
     * using a pieChart external library to represent the crowd votes
     * the answer might be incorrect and consider the fifty fifty wheel
     */
    public void crowdWheel() {
        helpWheels[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChart = true;
                helpWheels[2].setEnabled(false);
                ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(1,1,1,1,2,3,4));
                int random1 = new Random().nextInt(5) + 1,
                        random2 = new Random().nextInt(5) + 1,
                        random3 = new Random().nextInt(5) + 1;
                al.add(random1); al.add(random2); al.add(random3);

                Iterator itr = al.iterator();
                while (itr.hasNext()) {
                    int x = (Integer)itr.next();
                    if ((x == checkFifty[0] || x == checkFifty[1]) && x != 1)
                        itr.remove();
                }

                Collections.shuffle(al);
                double firstAns = 100*Collections.frequency(al, 1) / (al.size()),
                        secondAns = 100*Collections.frequency(al, 2)/(al.size()),
                        thirdAns = 100*Collections.frequency(al, 3)/(al.size()),
                        fourthAns = 100*Collections.frequency(al, 4)/(al.size());


                pieChart.setEnabled(true);
                if(firstAns > 0)
                    pieEntryList.add(new PieEntry((float) firstAns, questionMatrix[questionsOrder[numberOfQuestions]][1]));
                if(secondAns > 0)
                    pieEntryList.add(new PieEntry((float) secondAns, questionMatrix[questionsOrder[numberOfQuestions]][2]));
                if(thirdAns > 0)
                    pieEntryList.add(new PieEntry((float) thirdAns, questionMatrix[questionsOrder[numberOfQuestions]][3]));
                if(fourthAns > 0)
                    pieEntryList.add(new PieEntry((float) fourthAns, questionMatrix[questionsOrder[numberOfQuestions]][4]));
                PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Crowd Answers:");
                pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.setDrawHoleEnabled(false);
            }
        });
    }

    /**
     * clearChart()
     * clear the values and the appearance of the pie chart
     */
    void clearChart()
    {
        if(isChart){
            pieChart.clearValues();
            pieChart.clear();
            isChart= false;
        }
    }

    /**
     * creatematrix()
     *getting the selected array from the String.xml by the given difficult and category with size of 150((1 question + 4 answers) X 30 questions)
     * making the array into matrix of question(5X30)
     * each row represent:
     * row[0] = the question
     * row[1] = the correct answer
     * row[2,3,4] = the incorrect answers
     * @return
     */
    public String[][] creatematrix(){
        String[] arr = new String[150];
        if(category.compareTo("3") == 0){
            if(difficult.compareTo("1") == 0)
                arr = getResources().getStringArray(R.array.GeographyEASY);
            if(difficult.compareTo("2") == 0)
                arr = getResources().getStringArray(R.array.Geography_MEDIUM);
            if(difficult.compareTo("3") == 0)
                arr = getResources().getStringArray(R.array.Geography_HARD);
        }
        else if(category.compareTo("1") == 0) {
            if (difficult.compareTo("1") == 0)
                arr = getResources().getStringArray(R.array.EASY_PERSONS);
            if (difficult.compareTo("2") == 0)
                arr = getResources().getStringArray(R.array.MEDIUM_PERSONS);
            if (difficult.compareTo("3") == 0)
                arr = getResources().getStringArray(R.array.HARD_PERSONS);
        }
        else{
            if (difficult.compareTo("1") == 0)
                arr = getResources().getStringArray(R.array.EASY_PROGRAMMING);
            if (difficult.compareTo("2") == 0)
                arr = getResources().getStringArray(R.array.MEDIUM_PROGRAMMING);
            if (difficult.compareTo("3") == 0)
                arr = getResources().getStringArray(R.array.HARD_PROGRAMMING);
        }

        String newMatrix[][] = new String[arr.length/5][5];
        int count = 0;
        for(int i=0; i<arr.length/5; i++)
            for(int j=0;j<5;j++)
                newMatrix[i][j] = arr[count++];

        return newMatrix;
    }

    /**
     * startTimer()
     * using Thread to count a timer
     */
    void startTimer(){
        stopTimer = false;
        Thread run = new Thread() {
            public void run() {
                String seconds = " ";
                while(timerTime > 0){
                    if(!stopTimer){
                        if (globalTimer%60 < 9)
                            seconds = "0";
                        else seconds = "";
                        globalTimer += 1;
                        timerTime -= 1;
                        timer.setText(String.valueOf(timerTime));
                        if (timerTime == 10){
                            timeMusic = MediaPlayer.create(getApplicationContext(), R.raw.res_drawable_clock_tick);
                            timeMusic.start();
                        }

                        globalTimerText.setText(String.valueOf(globalTimer/60) + ":" + seconds + String.valueOf(globalTimer%60));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(timerTime <= 0)
                            mHandler.post(new Runnable() {
                                public void run(){
                                    endGame();
                                }
                            });
                    }
                }
            }
        };
        run.start();
    }

    /**
     * endGame()
     * create the alertBox
     * save with SharedPreferences the prize amount,name,difficult and time using person object array
     */

    void endGame(){
        alertBox();
        String time = String.valueOf(globalTimer/60) + ":" + String.valueOf(globalTimer%60);
        sp = getSharedPreferences("HIGH_SCORE",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        PersonArray sc;
        Gson gson = new Gson();
        if(sp.contains("first_test")) {
            String json = sp.getString("SCORE_TEST", "");
            sc = gson.fromJson(json, PersonArray.class);
        }
        else{
            sc = new PersonArray();
            editor.putBoolean("first_test",false);
        }

        Person p = new Person(String.valueOf(prize[numberOfQuestions]),name,diff[Integer.parseInt(difficult)-1], time);
        sc.add(p);

        String jsonSave = gson.toJson(sc);
        editor.putString("SCORE_TEST", jsonSave);
        editor.commit();
    }

    /**
     * alertBox()
     * create the alert dialog when the game is finished
     */

    void alertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Question_Activity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Question_Activity.this);
        View view = layoutInflaterAndroid.inflate(R.layout.activity_alert_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        view.findViewById(R.id.return_to_menu).setOnClickListener(v -> startActivity(intent));
        TextView moneyEarned = view.findViewById(R.id.money_Earned);
        TextView text1 = view.findViewById(R.id.game_over_text);
        Button returnMenu = view.findViewById(R.id.return_to_menu);
        returnMenu.setText(R.string.BACK_TO_MENU);
        text1.setText(R.string.END_GAME);
        moneyEarned.setText(getString(R.string.WON_MONEY)+" "+String.valueOf(prize[numberOfQuestions]));
    }

    @Override
    public void onBackPressed() {
        return;
    }
}