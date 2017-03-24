package com.example.piyushpotdukhe.AIS.StoxInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserStockList /*extends Observable*/ {

    public static List<StockDetailsClass> stockHashSet;

    private static UserStockList mUserStockList = null;
    private UserStockList() {
        // do nothing
    }

    public static UserStockList getUserStockListObj() {
        if (mUserStockList == null) {
            mUserStockList = new UserStockList();
            stockHashSet = new ArrayList<>(); //init
//            stockHashSet = new HashSet<>(); //init
        }
        return mUserStockList;
    }

    public List<StockDetailsClass> getStockHashSet() {
        return stockHashSet;
    }

    public void addToUserStockList(String script, String exchange, String cmp) {
        StockDetailsClass userSearchedForThisStock = new StockDetailsClass(script, cmp, exchange);
        //junk coding: why add-remove, why not > just update the cmp in the same object ??
        if(stockHashSet.contains(userSearchedForThisStock)) {
            stockHashSet.remove(userSearchedForThisStock);
        }
        stockHashSet.add(userSearchedForThisStock);
        Collections.sort(stockHashSet, getCompByScriptName());
    }


    // ----------------------- StockDetailsClass ---------------------------------//

    public Comparator<StockDetailsClass> getCompByScriptName() {
        Comparator comp = new Comparator<StockDetailsClass>(){
            @Override
            public int compare(StockDetailsClass o1, StockDetailsClass o2)
            {
                return ( (((StockDetailsClass)o1).getScript())
                        .compareTo(((StockDetailsClass)o2).getScript()) );
            }
        };
        return comp;
    }

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
        }

        public boolean equals(Object o) {
//            System.out.println("In equals");
            return ( (this.getScript()).equals(((StockDetailsClass)o).getScript()) );
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

