package com.example.dell.snulibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String NetId, pass;
    private static final String TAG = "MainActivity";
    private int retry_time = 5000;
    private String tableHTML = null;
    //private Document document;
    private Connection.Response loginForm = null;
    private MenuItem menu_item_refresh = null;
    private SharedPreferences sharedPreferences;
    View view = null;
    //String id, password;
    Boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedPreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NetId = extras.getString("username");
            pass = extras.getString("password");
            new FetchPage().execute();
        }
    }

    private class FetchPage extends AsyncTask<Void, Void, Void> {
        Document page = null;

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
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }




            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", NetId);
            editor.putString("password", pass);
            editor.commit();
            Intent startdetails = new Intent(getApplicationContext(), Details.class);
            startdetails.putExtra("username", NetId);
            startdetails.putExtra("password", pass);
            startdetails.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(startdetails);
            finish();

            return null;
        }


        /*@Override
        protected void onPostExecute(Void result) {
            new FetchResult().execute();
        }*/


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

    Call post(String mode, String username, String password, Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("mode", mode)
                .add("username", username + "@snu.in")
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.50.1/24online/servlet/E24onlineHTTPClient")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    /**
     * Logs the user out and then logs them back in and executes the FetchPage AsyncTask.
     * Logging-out and then logging-in forces the server to start a new session.
     */
    public void logoutLoginRefresh(final String username, final String password) {
        try {
            post("193", username, password, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(TAG, "Logout failed with " + e.toString());
                    login(username, password);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    Log.i(TAG, "Logout: " + response.toString());
                    login(username, password);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "HTTP POST IOError: " + e.toString());
        }
    }

    /**
     * Logs the user in.
     */
    private void login(final String username, final String password) {
        try {
            post("191", username, password, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(TAG, "Login failed with " + e.toString());
                    onLoginFailure(username, password);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    Log.i(TAG, "Login: " + response.toString());

                    // Run from UI thread to interact with UI
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "HTTP POST IOError: " + e.toString());
        }
    }

    /**
     * If login failed retry 5 times over a period of 5.25 minutes.
     */

    private void onLoginFailure(final String username, final String password) {
        if (retry_time > 160) {
            retry_time = 0;
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoutLoginRefresh(username, password);
            }
        }, retry_time);
        retry_time *= 2;    // Increase retry time for a better chance of success
    }

}