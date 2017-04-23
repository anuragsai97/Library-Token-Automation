package com.example.dell.snulibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Details extends AppCompatActivity implements OnClickListener{

    String Netid, pass;
    String tok;
    Boolean connected;
    private String tableHTML = null;
    String bookstatus;
    EditText book;
    String name,rno;


    //View view=null;

    private static final String REGISTER_URL = "http://10.6.11.171/SNU_Library/register.php";

    private Button button;
    private boolean flagmale = false;
    private boolean flagfemale = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String bookname;
        String preference;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        book = (EditText) findViewById(R.id.editText);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);

        // book= (EditText) findViewById(R.id.editText);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Netid = extras.getString("username");
            pass = extras.getString("password");
            new myAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        final RadioButton btbook = (RadioButton) findViewById(R.id.radioButton2);
        final RadioButton btbook1 = (RadioButton) findViewById(R.id.radioButton);




        btbook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (btbook.isChecked()) {
                    if (!flagmale) {
                        btbook.setChecked(true);
                        btbook1.setChecked(false);
                        flagmale = true;
                        flagfemale = false;
                        bookstatus="personal";
                    } else {
                        flagmale = false;
                        btbook.setChecked(false);
                        btbook1.setChecked(false);
                    }
                }
               // System.out.println(bookstatus);
            }
        });

        btbook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btbook1.isChecked()) {
                    if (!flagfemale) {
                        btbook1.setChecked(true);
                        btbook.setChecked(false);
                        flagfemale = true;
                        flagmale = false;
                        bookstatus="Library";
                    } else {
                        flagfemale = false;
                        btbook1.setChecked(false);
                        btbook.setChecked(false);
                    }
                }
                //System.out.println(bookstatus);
            }

        });

    }

    @Override
    public void onClick(View view) {
        if(view == button){
            //register("1510110327","anurag",2,"harry","library");
            String books = book.getText().toString().trim();
            register(rno,name,50,books,"library");


        }
    }




    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        Document page = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response loginform = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/login")
                        .method(Connection.Method.GET)
                        .validateTLSCertificates(false)
                        .execute();
                page = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                        .data("cookieexists", "false")
                        .data("login_user_name", Netid)
                        .data("login_password", pass)
                        .validateTLSCertificates(false)
                        .cookies(loginform.cookies())
                        .post();
                page = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/summary")
                        .cookies(loginform.cookies())
                        .get();
                Log.d("Main", page.title());
                if (!(page.title().startsWith("Login"))) connected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView naam = (TextView) findViewById(R.id.textView5);
            TextView roll = (TextView) findViewById(R.id.textView);
            System.out.println(bookstatus);
            /*
            WebView wv = (WebView) view.findViewById(R.id.webView1);
            wv.getSettings().setSupportZoom(true);
            wv.getSettings().setSaveFormData(true);
            wv.getSettings().setBuiltInZoomControls(true);
            //wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());
            wv.setInitialScale(140);*/
            if(connected)
            {
                try{
                    Element table = page.select("table[class=table table-bordered table-condensed table-striped]").first();
                    tableHTML = table.html();
                    tableHTML = "<table>" + tableHTML + "</table>";
                    //  wv.loadDataWithBaseURL(null, tableHTML, "text/html", "utf-8", null);
                    String nametable = page.select("div[class=container]").get(1).text();
                    name = nametable.substring(nametable.indexOf(" ") + 1, nametable.indexOf("["));
                    rno = nametable.substring(nametable.indexOf("["), nametable.indexOf("]") + 1);
                    naam.setText((CharSequence)"Name:- " + name);
                    roll.setText((CharSequence)"Roll Number:- " + rno);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),(CharSequence)"(2)AN internal error has made the app to force exit...Please try again ", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),(CharSequence)"(2)Network Communication Issues...Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void register(String rollnumber, final String name, int token, String books, String b_details) {
        String urlSuffix = "?rollnumber="+rollnumber+"&name="+name+"&tokennumber="+token+"&books="+books+"&bookdetails="+b_details;
        String temp = urlSuffix;
        final String n=name;
        final String r=rollnumber;
        temp = temp.replaceAll(" ", "%20");
        System.out.println(temp);
        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Details.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("<br />")){
                    s="Registered Successfully";
                }
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                if(s.equals("Registered Successfully"))
                {

                    Intent submit= new Intent(getApplicationContext(),Submit.class);
                    submit.putExtra("tok",tok);
                    submit.putExtra("nam",n);
                    submit.putExtra("rn",r);
                    startActivity(submit);

                }


            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
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

        RegisterUser ru = new RegisterUser();

        ru.execute(temp);
        //ru.execute(urlSuffix);
    }



}
