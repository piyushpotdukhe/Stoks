package com.example.piyushpotdukhe.AIS.StoxInfo;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class UserStockList extends Observable {

    public static Set<StockDetailsClass> stockHashSet;

    private static UserStockList mUserStockList = null;
    private UserStockList() {
        // do nothing
    }

    public static UserStockList getUserStockListObj() {
        if (mUserStockList == null) {
            mUserStockList = new UserStockList();
            stockHashSet = new HashSet<>(); //init
        }
        return mUserStockList;
    }

    public void addToUserStockList(String script, String exchange, String cmp) {
        stockHashSet.add(new StockDetailsClass(script, cmp, exchange));
        notifyObservers();
    }

    public Set getObserver() {
        return stockHashSet;
    }



    // ----------------------- StockDetailsClass ---------------------------------//

    public class StockDetailsClass {

        //add more data
        String exchange;
        String script;
        String cmp;

        StockDetailsClass(String script, String cmp, String exchange){
            this.script = script;
            this.cmp = cmp;
            this.exchange = exchange;
        }

        public String toString() {
            return ( this.exchange + this.script + "=" + this.cmp );
        }

        public int hashCode(){
//            System.out.println("In hashcode");
            int hashcode = 0;
            hashcode += this.getScript().hashCode();
            return hashcode;
//            return -1;
        }

        public boolean equals(Object o) {
//            System.out.println("In equals");
            return(this.getScript())
                    .equals(((StockDetailsClass)o).getScript());
        }

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
}

