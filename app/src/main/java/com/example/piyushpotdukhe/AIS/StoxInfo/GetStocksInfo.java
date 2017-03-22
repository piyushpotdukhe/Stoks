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
import android.widget.Toast;

import com.example.piyushpotdukhe.AIS.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import static com.example.piyushpotdukhe.AIS.StoxInfo.UserStockList.stockHashSet;

public class GetStocksInfo extends AppCompatActivity implements Observer{

    @Override
    public void update(Observable o, Object arg) {
        Toast.makeText(this
                , "notified dude ...!!!" /*+ myBase.getObserver().getValue()*/
                , Toast.LENGTH_SHORT).show();
    }

    String mExchange;
    String mScript;
    String mCmp;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stock_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String username = "serotonin pkon"; //mUser.getDisplayName().toString();
        String msg = "Welcome " + username.substring(0, username.indexOf(" ")) + ", enter scrip here.";
        ((EditText)findViewById(R.id.scriptFromUser)).setHint(msg);

        rv = (RecyclerView)findViewById(R.id.recycler_view);

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
                mScript = ((EditText)findViewById(R.id.scriptFromUser)).getText().toString().toUpperCase();
//                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=NSE:RELIANCE";
                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=" + mExchange + mScript;
                new RetrieveCmp().execute(gFinUrl);
            }
        });
    }


    public void onClickShowListButton(View v) {
        RVAdapter adapter = new RVAdapter(stockHashSet);
//        Toast.makeText(getApplicationContext(), "test toast", Toast.LENGTH_SHORT).show();
        Log.d("serotonin", "onClickShowListButton");
//        RVAdapter adapter = null;
        rv = (RecyclerView)findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);
    }

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
                        cmp = line.substring(cmpLine.length()/*line.indexOf(cmlLine) + 3*/ , line.length()-1);
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
            } catch ( java.io.FileNotFoundException e ){
                e.printStackTrace();
            } catch ( java.net.MalformedURLException e ){
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
                mCmp = "0.0";
            }
            return mCmp;
        }

        protected void onPostExecute(String cmp) {
            updateDataToUI(cmp);
        }

        private void updateDataToUI(final String CMP) {
            String currency = "Rs.";
            String textToDisplay = "NSE:" + currency + CMP;
            ((TextView)findViewById(R.id.displayTV))
                    .setText(textToDisplay);
            fillListToCreateCards(mScript, mExchange, CMP);

        }

        private void fillListToCreateCards(String script, String exchange, String cmp){
            //stockHashSet.add(new StockDetailsClass(script, cmp, exchange));
            UserStockList.getUserStockListObj().addToUserStockList(script, exchange, cmp);

            //just debug prints:
            /*Iterator<StockDetailsClass> itr = stockHashSet.iterator();
            while(itr.hasNext()) {
                StockDetailsClass temp = itr.next();
                System.out.println(temp);
            }*/
        }
    }

}
