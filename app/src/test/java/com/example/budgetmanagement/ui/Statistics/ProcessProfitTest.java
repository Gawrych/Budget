package com.example.budgetmanagement.ui.Statistics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class ProcessProfitTest {

    @Test
    public void checkSummaryProfitAndLoss() {
        ArrayList<PriceAndProfit> priceAndProfits = new ArrayList<>(2);
        priceAndProfits.add(new PriceAndProfit(1000.5f, true));
        priceAndProfits.add(new PriceAndProfit(50.2f, false));
        ProcessProfit processProfit = new ProcessProfit(priceAndProfits);

        assertEquals(950.30f, processProfit.sum(), 0.00f);
    }

    @Test
    public void checkIfSummaryIsInTwoDecimalPlacesPrecision() {
        ArrayList<PriceAndProfit> priceAndProfits = new ArrayList<>(2);
        priceAndProfits.add(new PriceAndProfit(0.51f, true));
        priceAndProfits.add(new PriceAndProfit(0.26f, false));
        ProcessProfit processProfit = new ProcessProfit(priceAndProfits);

        assertEquals(0.25f, processProfit.sum(), 0.00f);
    }

}