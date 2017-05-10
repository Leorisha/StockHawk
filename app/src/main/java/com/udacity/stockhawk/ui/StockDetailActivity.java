package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ananeto on 29/04/2017.
 */

public class StockDetailActivity extends AppCompatActivity {


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);
    }
}
