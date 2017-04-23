package com.example.dell.snulibrary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Submit extends AppCompatActivity implements View.OnClickListener{
    String name;
    String roll;
    private Button buttonSubmit;

    private static final String SUBMIT_URL = "http://10.6.11.171/SNU_Library/submit.php";
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

        buttonSubmit = (Button) findViewById(R.id.button2);
        buttonSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view == buttonSubmit){
            submitUser();
        }
    }

    private void submitUser() {
        submit(roll);
    }


    private void submit(String rollnumber) {
        String urlSuffix = "?rollnumber="+rollnumber;
        String temp = urlSuffix;
        final String n=name;
        final String r=rollnumber;
        temp = temp.replaceAll(" ", "%20");
        System.out.println(temp);
        class SubmitUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Submit.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                System.out.println(s);
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(SUBMIT_URL+s);
                    System.out.println(url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        SubmitUser ru = new SubmitUser();
        ru.execute(temp);
        //ru.execute(urlSuffix);
    }



}
