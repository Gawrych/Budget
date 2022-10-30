package com.example.budgetmanagement.ui.Coming;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.ui.utils.DecimalPrecision;

import java.math.BigDecimal;
import java.util.List;

public class Section {

    private final int labelId;
    private final List<ComingAndTransaction> comingAndTransactionList;
    private BigDecimal balance = new BigDecimal("0.00");

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Section(int labelId, List<ComingAndTransaction> comingAndTransactionList) {
        this.labelId = labelId;
        this.comingAndTransactionList = comingAndTransactionList;
        prepareBalance();
    }

    public int getLabelId() {
        return labelId;
    }

    public List<ComingAndTransaction> getComingAndTransactionList() {
        return comingAndTransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void prepareBalance() {
        comingAndTransactionList.forEach(item -> {
            BigDecimal bigDecimal = new DecimalPrecision(item.transaction.getAmount()).getParsedContent();
            balance = balance.add(bigDecimal);
        });
    }

    public String getBalance() {
        return balance.toString();
    }

    public boolean isBalanceNegative() {
        return balance.signum() < 0;
    }
}
