package com.example.piyushpotdukhe.AIS.StoxInfo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyushpotdukhe.AIS.R;

import java.util.Iterator;
import java.util.Set;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.StockViewHolder> {

    // RVAdapter constructor
    Set<UserStockList.StockDetailsClass> stockHashSet;
    RVAdapter(Set<UserStockList.StockDetailsClass> stockHashSet){
        this.stockHashSet = stockHashSet;
    }

    CardView cv;
    TextView tv_stockName;
    TextView tv_stockCmp;

    public class StockViewHolder extends RecyclerView.ViewHolder {
        public StockViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            tv_stockName = (TextView) v.findViewById(R.id.stock_name);
            tv_stockCmp = (TextView) v.findViewById(R.id.stock_cmp);
        }
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_stock_card_view, parent, false);
        StockViewHolder svh = new StockViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        if (position<0) {
            Log.d("serotonin", "onBindViewHolder: invalid position received");
            return;
        }

        UserStockList.StockDetailsClass sdc = null;
        Iterator<UserStockList.StockDetailsClass> itr = stockHashSet.iterator();
        while(position >= 0) {
            sdc = itr.next();
            position--;
        }
//        tv_stockName.setText("Script name = " + sdc.getScript());
        tv_stockName.setText(sdc.getScript());
        tv_stockCmp.setText(sdc.getCmp());
//        tv_stockCmp.setText("Current market price = " + sdc.getCmp());
    }

    @Override
    public int getItemCount() {
        return stockHashSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
} //e.o.RVAdapter