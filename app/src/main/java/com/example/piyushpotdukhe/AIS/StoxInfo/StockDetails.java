package com.example.piyushpotdukhe.AIS.StoxInfo;

public class StockDetails {

    private static StockDetails mStockDetails = null;
    StockDetails(String script, String cmp){
        this.script = script;
        this.cmp = cmp;
    }

    // this is to get the class object
    public static StockDetails getStockDetailsObject(){
        if (mStockDetails == null) {
            mStockDetails = new StockDetails("EMPTY", "EMPTY");
        }
        return mStockDetails;
    }

    //add more data
    String script;
    String cmp;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }
}
