package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ananeto on 29/04/2017.
 */

public class StockDetailActivity extends AppCompatActivity {

    private String quoteSymbol;
    private String quoteHistory;


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        this.quoteSymbol = intent.getStringExtra(MainActivity.EXTRA_SYMBOL);
        this.quoteHistory = intent.getStringExtra(MainActivity.EXTRA_HISTORY);

        this.setTitle(this.quoteSymbol);
        this.buildHistoryArrays();
    }

    private void buildHistoryArrays() {
        String[] values = this.quoteHistory.split("\n");
        ArrayList<Entry> entries = new ArrayList<Entry>();
        String startingTimeMilis = "";
        int j = 0;
        for (int i = values.length-1; i >= 0; i--) {

            String value = values[i];
            String[] valuesInsideValue = value.split(", ");
            // turn your data into Entry objects
            entries.add(new Entry(new Float(valuesInsideValue[0]),Float.parseFloat(valuesInsideValue[1])));

            if (j == 0) {
                startingTimeMilis = valuesInsideValue[0];
            }

            j++;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(startingTimeMilis));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        LineDataSet dataSet = new LineDataSet(entries, "Value over time since " + sdf.format(calendar.getTime()));
        dataSet.setFillAlpha(65);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        Description desc = new Description();
        desc.setText("Dolar");
        chart.setDescription(desc);
        chart.invalidate();
    }
}
