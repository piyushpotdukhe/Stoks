package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.piyushpotdukhe.AIS.StoxInfo.StockDetails.getStockDetailsObject;

class RetrieveCmp extends AsyncTask<String, Void, Integer> {

    private Exception exception;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    private void fillCmp(String cmp) {
        getStockDetailsObject().setCmp(cmp);
    }

    private void readStream(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                String cmpLine = ",\"l\" : \"";
                if (line.startsWith(cmpLine)) { //parse line with current price
                    String cmp = line.substring(cmpLine.length()/*line.indexOf(cmlLine) + 3*/ , line.length()-1);
                    Log.e("SEROTONIN", "CMP=" + cmp);
                    fillCmp(cmp);
                }
            }
            stream.close();
//            Log.e("SEROTONIN", "sb=" + sb.toString());
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
    }

    public JSONObject getJSONFromUrl(String url_in) {

        try {
            URL url = new URL(url_in);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch ( java.io.FileNotFoundException e ){

        } catch ( java.net.MalformedURLException e ){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;

    }


    protected Integer doInBackground(String... stringStockQuery) {
        try {
            getJSONFromUrl(stringStockQuery[0]);
        } catch (Exception e) {
            this.exception = e;
//            return null;
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(Integer i) {

    }
}