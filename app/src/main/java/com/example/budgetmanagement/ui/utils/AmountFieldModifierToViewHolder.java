package com.example.budgetmanagement.ui.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.budgetmanagement.R;

import java.math.BigDecimal;

public class AmountFieldModifierToViewHolder {

    private final TextView amountField;

    public AmountFieldModifierToViewHolder(TextView amountField) {
        this.amountField = amountField;
    }

    public void setRedColorIfIsNegative(String amount) {
        BigDecimal amountInBigDecimal = new BigDecimal(amount);
        if (checkIsNegative(amountInBigDecimal)) {
            changeColorForLossTransaction();
        }
    }

    private boolean checkIsNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private void changeColorForLossTransaction() {
        this.amountField.setTextColor(ContextCompat.getColor(getContext(), getLossTransactionColor()));
    }

    private Context getContext() {
        return this.amountField.getContext();
    }

    private int getLossTransactionColor() {
        return R.color.nonProfitTransaction;
    }
}
