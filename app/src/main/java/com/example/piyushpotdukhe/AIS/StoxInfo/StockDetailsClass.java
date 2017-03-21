package com.example.piyushpotdukhe.AIS.StoxInfo;

public class StockDetailsClass {

    private static StockDetailsClass mStockDetails = null;
    StockDetailsClass(String script, String cmp){
        this.script = script;
        this.cmp = cmp;
        this.exchange = "NSE:";
    }

    StockDetailsClass(String script, String cmp, String exchange){
        this.script = script;
        this.cmp = cmp;
        this.exchange = exchange;
    }

    public String toString() {
        return ( this.exchange + this.script + "=" + this.cmp );
    }

    // this is to get the class object
    public static StockDetailsClass getStockDetailsObject(){
        if (mStockDetails == null) {
            mStockDetails = new StockDetailsClass("EMPTY", "EMPTY");
        }
        return mStockDetails;
    }

    //add more data
    String exchange;
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
