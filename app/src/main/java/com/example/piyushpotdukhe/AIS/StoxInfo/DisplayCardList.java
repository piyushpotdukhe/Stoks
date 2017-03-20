package com.example.piyushpotdukhe.AIS.StoxInfo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.piyushpotdukhe.AIS.R;

public class DisplayCardList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card_list);

        FloatingActionButton addStock = (FloatingActionButton) findViewById(R.id.fabAddStockToList);
        addStock.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplicationContext(),
                                                    "adding stock to list",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        mAdapter = new MyAdapter(myDataset);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }
}
