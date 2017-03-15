package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piyushpotdukhe.AIS.R;
//import com.example.piyushpotdukhe.AIS.StoxInfo.RetrieveCmp;

import java.util.concurrent.ExecutionException;

import static com.example.piyushpotdukhe.AIS.LoginActivity.mUser;
import static com.example.piyushpotdukhe.AIS.StoxInfo.StockDetails.getStockDetailsObject;

public class GetStocksInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stock_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setHintToUser();
        String username = mUser.getDisplayName().toString();
        String msg = "Welcome " + username.substring(0, username.indexOf(" ")) + ", enter scrip here.";
        ((EditText)findViewById(R.id.scriptFromUser)).setHint(msg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "getting your data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                String mExchange = "NSE:";
                String mScript = ((EditText)findViewById(R.id.scriptFromUser)).getText().toString().toUpperCase();
//                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=NSE:RELIANCE";
                String gFinUrl = "http://finance.google.com/finance/info?client=ig&q=" + mExchange + mScript;

//                new JSONParser().getJSONFromUrl(gFinUrl);
                getStockDetailsObject().setScript(mScript);
                try {
                    new RetrieveCmp().execute(gFinUrl).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                updateDataToUI(mExchange);
            }

            private boolean isCmpValid(String cmp) {
                if (cmp.toUpperCase().contains("EMPTY")) {
                    return false;
                }
                return true;
            }


            private void updateDataToUI(String exchange) {
                String currency = "Rs.";
                String cmp = getStockDetailsObject().getCmp();
//                String script = getStockDetailsObject().getScript();

                String textToDisplay = exchange;
                if (isCmpValid(cmp)) {
                    textToDisplay = currency.concat(cmp);
                } else {
                    textToDisplay = "Could not find script on NSE.";
                }

                ((TextView)findViewById(R.id.displayTV))
                        .setText(textToDisplay);
            }
        });
    }

}
