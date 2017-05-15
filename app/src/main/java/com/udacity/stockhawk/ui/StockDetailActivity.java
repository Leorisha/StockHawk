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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
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

class DateValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date(Float.valueOf(value).longValue());
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
    }
}

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

        for (int i = values.length-1; i >= 0; i--) {

            String value = values[i];
            String[] valuesInsideValue = value.split(", ");
            // turn your data into Entry objects
            entries.add(new Entry(new Float(valuesInsideValue[0]),Float.parseFloat(valuesInsideValue[1])));
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new DateValueFormatter());

        YAxis left = chart.getAxisLeft();
        left.setEnabled(true);
        left.setLabelCount(10, true);
        left.setTextColor(Color.WHITE);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextSize(16f);
        Description desc = new Description();
        String name = getResources().getString(R.string.quote_description);
        desc.setText(name);
        chart.setDescription(desc);
        LineDataSet dataSet = new LineDataSet(entries, this.quoteSymbol);
        LineData lineData = new LineData(dataSet);
        chart.animateX(2500);
        chart.setData(lineData);
    }
}
