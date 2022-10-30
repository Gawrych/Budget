package com.example.budgetmanagement.ui.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPrecision {

    private String content;

    public DecimalPrecision(String content) {
        this.content = content;
    }

    public BigDecimal getParsedContent() {
        return new BigDecimal(replaceAllCommaToDot(content)).setScale(2, RoundingMode.DOWN);
    }

    private String replaceAllCommaToDot(String numberToProcessing) {
        String dot = ".";
        String comma = ",";
        return numberToProcessing.replaceAll(comma, dot);
    }
}
