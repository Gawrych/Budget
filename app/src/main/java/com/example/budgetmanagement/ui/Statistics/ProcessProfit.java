package com.example.budgetmanagement.ui.Statistics;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ProcessProfit {

    private ArrayList<PriceAndProfit> priceAndProfits;

    public ProcessProfit(ArrayList<PriceAndProfit> priceAndProfits) {
        this.priceAndProfits = priceAndProfits;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public float sum() {
        float summary = 0;

        for (PriceAndProfit priceAndProfit : priceAndProfits) {
            if (priceAndProfit.isProfit()) {
                summary += priceAndProfit.getAmount();
            } else {
                summary -= priceAndProfit.getAmount();
            }
        }

        return summary;
    }
}
