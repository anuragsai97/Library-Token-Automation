package com.example.dell.snulibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class Submit extends AppCompatActivity implements View.OnClickListener{
    String name;
    String roll;
    String tokennumber;
    String tok;
    private Button buttonSubmit;
    private ProgressDialog loading;
    private static final String DATA_URL ="http://10.6.11.171/SNU_Library/token.php?id=";
    public static final String JSON_ARRAY = "result";
    public static final String KEY_TOKEN = "token";

    private static final String SUBMIT_URL = "http://10.6.11.171/SNU_Library/submit.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("nam");
            roll = extras.getString("rn");
            tokennumber=extras.getString("tok");
            System.out.println(name);
            System.out.println(roll);
            System.out.println(tokennumber);


        }
        setContentView(R.layout.activity_submit);
        TextView naam = (TextView) findViewById(R.id.textView3);
        TextView rno = (TextView) findViewById(R.id.textView4);
        getData(roll);



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


    private void getData(String rollnumber) {

        String url = DATA_URL+rollnumber;
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Submit.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        //String token="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject Data = result.getJSONObject(0);
            tok= Data.getString(KEY_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //tok=token;
        TextView token=(TextView) findViewById(R.id.textView9);
        token.setText(tok);
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
                if(s.equals("<br />")){
                    s="Submitted Successfully";
                }
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(s.equals("Submitted Successfully"))
                {

                    Intent details= new Intent(getApplicationContext(),Details.class);
                    startActivity(details);

                }
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
