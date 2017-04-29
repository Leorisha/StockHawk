package com.udacity.stockhawk.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;


interface OnDataSendToActivity {
    public void sendData(Boolean result);
}

public class AddStockDialog extends DialogFragment implements OnDataSendToActivity{

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.dialog_stock)
    EditText stock;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.add_stock_dialog, null);

        ButterKnife.bind(this, custom);

        stock.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addStock();
                return true;
            }
        });
        builder.setView(custom);

        builder.setMessage(getString(R.string.dialog_title));
        builder.setPositiveButton(getString(R.string.dialog_add),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addStock();
                    }
                });
        builder.setNegativeButton(getString(R.string.dialog_cancel), null);

        Dialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        return dialog;
    }

    private void addStock() {
        Activity parent = getActivity();
        if (parent instanceof MainActivity) {
            isValidStock(stock.getText().toString());
        }

    }

    @Override
    public void sendData(Boolean result) {

        if (result) {
            ((MainActivity) getActivity()).addStock(stock.getText().toString());
            dismissAllowingStateLoss();
        }
        else {
            ((MainActivity) getActivity()).showErrorMessage();
        }
    }



    private class ValidateStockTask extends AsyncTask<String, Void, Boolean> {

        OnDataSendToActivity dataSendToActivity;
        public ValidateStockTask(DialogFragment activity){
            dataSendToActivity = (OnDataSendToActivity)activity;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Stock stock = YahooFinance.get(params[0]);
                StockQuote quote = stock.getQuote(true);

                if (quote.getChangeInPercent() != null
                        && quote.getChange() != null
                            && quote.getPrice() != null) {
                    return true;
                }

                return false;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dataSendToActivity.sendData(result);
        }
    }


    private void isValidStock(String symbol) {
        new ValidateStockTask(this).execute(symbol);
    }
}
