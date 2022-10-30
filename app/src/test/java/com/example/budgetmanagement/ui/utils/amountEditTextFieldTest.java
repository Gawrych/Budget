package com.example.budgetmanagement.ui.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class amountEditTextFieldTest {

    @Test
    public void testFormatIntegerToDecimalPrecision() {
        DecimalPrecision decimalPrecision = new DecimalPrecision("20");

        assertEquals("20.00", decimalPrecision.getParsedContent().toString());

    }

    @Test
    public void testFormatDecimalToDecimalPrecision() {
        DecimalPrecision decimalPrecision = new DecimalPrecision("0.2");

        assertEquals("0.20", decimalPrecision.getParsedContent().toString());
    }

    @Test
    public void testReplacingCommaToDot() {
        DecimalPrecision decimalPrecision = new DecimalPrecision("0,0");

        assertEquals("0.00", decimalPrecision.getParsedContent().toString());
    }

    @Test
    public void testRoundingToSecondDecimalPoint() {
        DecimalPrecision decimalPrecision = new DecimalPrecision("0.1200");

        assertEquals("0.12", decimalPrecision.getParsedContent().toString());
    }

    @Test
    public void testRoundingToDown() {
        DecimalPrecision decimalPrecision = new DecimalPrecision("0.1299");

        assertEquals("0.12", decimalPrecision.getParsedContent().toString());
    }
}
