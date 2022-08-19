package com.example.budgetmanagement.ui.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.budgetmanagement.R;

import java.math.BigDecimal;

public class AmountFieldModifierToViewHolder {

    private final TextView amountField;
    private final TextView currency;

    public AmountFieldModifierToViewHolder(TextView amountField, TextView currency) {
        this.amountField = amountField;
        this.currency = currency;
    }

    public void setRedColorIfIsNegative(String amount) {
        BigDecimal amountInBigDecimal = new BigDecimal(amount);
        if (checkIsNegative(amountInBigDecimal)) {
            changeAmountFontColorForLossTransaction();
            changeCurrencyFontColorForLossTransaction();
        } else {
            changeAmountFontColorForProfitTransaction();
            changeCurrencyFontColorForProfitTransaction();
        }
    }

    private boolean checkIsNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private void changeAmountFontColorForLossTransaction() {
        this.amountField.setTextColor(getLossColor());
    }

    private void changeCurrencyFontColorForLossTransaction() {
        this.currency.setTextColor(getLossColor());
    }

    private void changeAmountFontColorForProfitTransaction() {
        this.amountField.setTextColor(getProfitColor());
    }

    private void changeCurrencyFontColorForProfitTransaction() {
        this.currency.setTextColor(getProfitColor());
    }

    private int getLossColor() {
        return ContextCompat.getColor(getContext(), getLossTransactionColor());
    }

    private int getProfitColor() {
        return ContextCompat.getColor(getContext(), getProfitTransactionColor());
    }

    private Context getContext() {
        return this.amountField.getContext();
    }

    private int getLossTransactionColor() {
        return R.color.loss_transaction;
    }

    private int getProfitTransactionColor() {
        return R.color.main_green;
    }
}
