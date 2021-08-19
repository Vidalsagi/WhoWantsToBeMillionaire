package com.example.projectandroidfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class High_Score_Activity extends AppCompatActivity {
    SharedPreferences sp;
    List<Person> persons;

    /**
     * load the PersonArray from the SharedPreferences
     * create a listView and create a cardView for each person in Person array
     * sort the PersonArray list with the score values
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_high__score_);

        sp = getSharedPreferences("HIGH_SCORE",MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Gson gson = new Gson();
        PersonArray sc;
        if(sp.contains("first_test")) {
            String json = sp.getString("SCORE_TEST", "");
             sc = gson.fromJson(json, PersonArray.class);
        }else
            sc = new PersonArray();

        persons = sc.getList();
        Collections.sort(persons);
        Score_Adapter scoreadapter = new Score_Adapter(persons);
        recyclerView.setAdapter(scoreadapter);
    }
}