package com.example.dell.snulibrary;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Submit extends AppCompatActivity {
    String name;
    String roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("nam");
            roll = extras.getString("rn");
            System.out.println(name);
            System.out.println(roll);
        }

        setContentView(R.layout.activity_submit);

        TextView naam = (TextView) findViewById(R.id.textView3);
        TextView rno = (TextView) findViewById(R.id.textView4);

        naam.setText("Name:- " + name);
        rno.setText("Roll Number:- "+roll);




    }
}
