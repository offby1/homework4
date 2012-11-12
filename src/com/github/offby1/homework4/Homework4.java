package com.github.offby1.homework4;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Homework4 extends Activity {

    myAsyncTask stockTask;

    private TextView stockValue;
    private EditText stockId;
    private Button getValue;

    String symbol = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen001);

        stockId = (EditText) findViewById(R.id.stockid);
        getValue = (Button) findViewById(R.id.getvalue);

        getValue.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    mumble();

                }
            });

        stockId.setOnEditorActionListener(new OnEditorActionListener() {

                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mumble ();
                        handled = true;
                    }
                    return handled;
                }

            });

    }

    private void mumble() {
        symbol = stockId.getText().toString();

        stockTask = new myAsyncTask();
        stockTask.execute(symbol);
    }

    public class myAsyncTask extends AsyncTask<String, Void, String> {
        String returnedValue = "";
        @Override
            protected String doInBackground(String... parms) {
            String currentURL = "http://finance.google.com/finance/info?client=ig&q=NASDAQ:"
                + parms[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                .setConnectionTimeout(client.getParams(), 30000);
            HttpResponse response = null;
            String responseText = "";
            HttpGet get = new HttpGet(currentURL);
            try {
                response = client.execute(get);
            } catch (Exception e) {
            }
            if (response != null) {
                HttpEntity entity = response.getEntity();
                try {
                    responseText = EntityUtils.toString(entity);
                    responseText = responseText.substring(3);
                    JSONObject json = new JSONObject();
                    JSONArray ja;
                    ja = new JSONArray(responseText);
                    for (int i = 0; i < ja.length(); i++) {
                        json = ja.getJSONObject(i);
                        returnedValue = json.get("l_cur").toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return returnedValue;
        }
        @Override
            protected void onPostExecute(String result) {
            stockValue = (TextView) findViewById(R.id.stockvalue);
            stockValue.setText(result);
        }
    }

}
