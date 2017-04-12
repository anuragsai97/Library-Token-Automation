package com.example.dell.snulibrary;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String NetId, pass;
    //private Document document;
    private Connection.Response loginForm = null;
    private MenuItem menu_item_refresh = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NetId = extras.getString("username");
            pass = extras.getString("password");
            new FetchPage().execute();
        }
    }

    private class FetchPage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setRefreshActionButtonState(menu_item_refresh, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            loginForm = null;

            try {
                loginForm = Jsoup.connect("http://myaccount.snu.edu.in/loginSubmit.php")
                        .method(Connection.Method.POST)
                        .data("snuNetId", NetId)
                        .data("password", pass)
                        .data("submit", "Login")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /*@Override
        protected void onPostExecute(Void result) {
            new FetchResult().execute();
        }*/
    }

    public void setRefreshActionButtonState(MenuItem refreshItem, final boolean refreshing) {
        //final MenuItem refreshItem = findViewById(R.id.action_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }

}
