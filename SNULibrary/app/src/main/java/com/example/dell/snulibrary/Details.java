package com.example.dell.snulibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import android.os.AsyncTask;
import android.os.Bundle;
import java.security.AccessControlContext;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.jsoup.nodes.Document;
import android.widget.TextView;
import org.jsoup.nodes.Element;
public class Details extends AppCompatActivity {

    String Netid, pass;
    Boolean connected;
    private String tableHTML = null;
    EditText book;
    //View view=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String bookname;
        String preference;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        book=(EditText)findViewById(R.id.editText);


        //bookname=R.layout.activity_details.



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Netid = extras.getString("username");
            pass = extras.getString("password");
            new myAsyncTask().execute();
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
                    String name = nametable.substring(nametable.indexOf(" ") + 1, nametable.indexOf("["));
                    String rno = nametable.substring(nametable.indexOf("["), nametable.indexOf("]") + 1);
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
}
