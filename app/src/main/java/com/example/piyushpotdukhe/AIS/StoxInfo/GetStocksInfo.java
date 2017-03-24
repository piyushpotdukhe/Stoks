package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piyushpotdukhe.AIS.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStocksInfo extends AppCompatActivity {

    String mExchange;
    String mScript;
    String mCmp;
    private RecyclerView rv;

    private static final String WRONG_SCRIPT = "< no such script >";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stock_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareStockListObject();
        prepareRecyclerViewDisplay();

        String username = "serotonin pkon"; //mUser.getDisplayName().toString();
        String msg = "Welcome " + username.substring(0, username.indexOf(" ")) + ", enter scrip here.";
        ((EditText) findViewById(R.id.scriptFromUser)).setHint(msg);

        rv = (RecyclerView) findViewById(R.id.recycler_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrice(view);
            }

            private void getPrice(View view) {
                Snackbar.make(view, "getting your data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mExchange = "NSE:";
                mScript = ((EditText) findViewById(R.id.scriptFromUser)).getText().toString().toUpperCase();
//                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=NSE:RELIANCE";
                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=" + mExchange + mScript;
                new RetrieveCmp().execute(gFinUrl);
            }
        });
    }

    private void prepareStockListObject() {
        // this is just to create an empty object, otherwise, whenever .size() is called
        // the code will crash, as object was null.
        UserStockList.getUserStockListObj();
    }

    private void prepareRecyclerViewDisplay() {
        RVAdapter adapter = new RVAdapter(UserStockList.getUserStockListObj().getStockHashSet());
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
    }

    /*public void onClickShowListButton(View v) {
        if (UserStockList.getUserStockListObj().getStockHashSet().size() > 0) {
            prepareRecyclerViewDisplay();
        } else {
            Toast.makeText(getApplicationContext(), "No stocks to populate list to display."
                    , Toast.LENGTH_SHORT).show();
        }
    }*/

    // async task to get the CMP from gfinance.
    class RetrieveCmp extends AsyncTask<String, Void, String/*Integer*/> {

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
                        cmp = line.substring(cmpLine.length()/*line.indexOf(cmlLine) + 3*/, line.length() - 1);
                        Log.e("SEROTONIN", "CMP=" + cmp);
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
            } catch (java.io.FileNotFoundException e) {
                // FileNotFoundException: when we give stock name that does not exists at all
                e.printStackTrace();
                cmp = WRONG_SCRIPT;
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cmp;
        }

        protected String doInBackground(String... stringStockQuery) {
            mCmp = "0.0";
            try {
                mCmp = getJSONFromUrl(stringStockQuery[0]);
            } catch (Exception e) {
                Log.d("GetStocksInfo", "doInBackground, Exception = " + e.toString());
                mCmp = "-1";
            }
            return mCmp;
        }

        protected void onPostExecute(String cmp) {
            updateDataToUI(cmp);
        }

        private void updateDataToUI(final String CMP) {
            String textToDisplay = WRONG_SCRIPT;

            if (!CMP.equalsIgnoreCase(WRONG_SCRIPT)) {
                String currency = "Rs.";
                textToDisplay = "NSE: " + currency + CMP;

                //add stock to list for display
                fillListToCreateCards(mScript, mExchange, CMP);
            }

            // just display current price to user
            ((TextView) findViewById(R.id.displayTV)).setText(textToDisplay);

        }

        private void fillListToCreateCards(String script, String exchange, String cmp) {
            UserStockList.getUserStockListObj().addToUserStockList(script, exchange, cmp);
            prepareRecyclerViewDisplay();

            //just debug prints: to print the contents of the complete stock object
            /*Iterator<StockDetailsClass> itr = stockHashSet.iterator();
            while(itr.hasNext()) {
                StockDetailsClass temp = itr.next();
                System.out.println(temp);
            }*/
        }
    }

}
