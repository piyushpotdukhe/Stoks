package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

public class StockDetailsChangeObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) { // for "implements Observer"
        Log.d("SEROTONIN", "notified dude ...!!!");
    }
}