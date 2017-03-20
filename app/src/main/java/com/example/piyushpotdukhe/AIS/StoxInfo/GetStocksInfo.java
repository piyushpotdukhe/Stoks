package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piyushpotdukhe.AIS.R;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.piyushpotdukhe.AIS.StoxInfo.StockDetails.getStockDetailsObject;

public class GetStocksInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stock_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String username = "serotonin pkon"; //mUser.getDisplayName().toString();
        String msg = "Welcome " + username.substring(0, username.indexOf(" ")) + ", enter scrip here.";
        ((EditText)findViewById(R.id.scriptFromUser)).setHint(msg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrice(view);
//                launchCardList();
            }

            private void launchCardList() {
                Intent launchCardList = new Intent(GetStocksInfo.this, DisplayCardList.class);
                startActivity(launchCardList);
            }

            private void getPrice(View view) {
                Snackbar.make(view, "getting your data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                String mExchange = "NSE:";
                String mScript = ((EditText)findViewById(R.id.scriptFromUser)).getText().toString().toUpperCase();
//                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=NSE:RELIANCE";
                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=" + mExchange + mScript;
                getStockDetailsObject().setScript(mScript);
                new RetrieveCmp().execute(gFinUrl);
            }
        });
    }



// async task to get the CMP from gfinance.
    class RetrieveCmp extends AsyncTask<String, Void, String/*Integer*/> {

        private void fillCmp(String cmp) {
            getStockDetailsObject().setCmp(cmp);
        }

        private String readStream(InputStream stream) {
            String cmp = "0.0";
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    String cmpLine = ",\"l\" : \"";
                    if (line.startsWith(cmpLine)) { //parse line with current price
                        cmp = line.substring(cmpLine.length()/*line.indexOf(cmlLine) + 3*/ , line.length()-1);
                        Log.e("SEROTONIN", "CMP=" + cmp);
                        fillCmp(cmp);
                    }
                }
                stream.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return cmp;
        }

        public String getJSONFromUrl(String url_in) {

            String cmp = "0.0";
            try {
                URL url = new URL(url_in);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    cmp = readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch ( java.io.FileNotFoundException e ){

            } catch ( java.net.MalformedURLException e ){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cmp;
        }

        protected String doInBackground(String... stringStockQuery) {
            String cmp = "0.0";
            try {
                cmp = getJSONFromUrl(stringStockQuery[0]);
            } catch (Exception e) {
                Log.d("GetStocksInfo", "doInBackground, Exception = " + e.toString());
                return "0.0";
            }
            return cmp;
        }

        protected void onPostExecute(String cmp) {
            updateDataToUI(cmp);
        }

        private void updateDataToUI(final String CMP) {
                String currency = "Rs.";
                String textToDisplay = "NSE:" + currency + CMP;
                ((TextView)findViewById(R.id.displayTV))
                        .setText(textToDisplay);
            }
    }

}
