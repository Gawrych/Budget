package com.example.budgetmanagement.ui.History;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.Transaction;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryAndTransactionListSortingMethodTest {

    private final List<HistoryAndTransaction> originalList = new ArrayList<>();
    private HistoryAndTransactionListSortingMethod sortingMethod;
    private HistoryAndTransaction elementA;
    private HistoryAndTransaction elementB;
    private HistoryAndTransaction elementC;

    @Before
    public void setUp() {
        elementA = new HistoryAndTransaction();
        elementA.transaction = new Transaction(1, 1, "A", "1", 1, LocalDate.now().toEpochDay(), true);
        elementA.history = new History(1, 1, LocalDate.now().toEpochDay());

        elementB = new HistoryAndTransaction();
        elementB.transaction = new Transaction(2, 2, "B", "-2", 2, LocalDate.now().toEpochDay(), false);
        elementB.history = new History(2, 2, LocalDate.now().toEpochDay());

        elementC = new HistoryAndTransaction();
        elementC.transaction = new Transaction(3, 2, "C", "-3", 3, LocalDate.now().toEpochDay(), false);
        elementC.history = new History(3, 3, LocalDate.now().toEpochDay());

        originalList.add(elementC);
        originalList.add(elementA);
        originalList.add(elementB);

        sortingMethod = new HistoryAndTransactionListSortingMethod(originalList);
    }

    @Test
    public void When_CallToFilterByProfit_Expect_ShowOnlyElementA() {
        sortingMethod.filterByProfit();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertTrue(sortedList.contains(elementA));
        assertFalse(sortedList.contains(elementB));
        assertFalse(sortedList.contains(elementC));
    }

    @Test
    public void When_CallToFilterByLoss_Expect_ShowElementBAndElementC() {
        sortingMethod.filterByLoss();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertFalse(sortedList.contains(elementA));
        assertTrue(sortedList.contains(elementB));
        assertTrue(sortedList.contains(elementC));
    }

    @Test
    public void When_CallReverseList_Expect_ReverseList() {
        sortingMethod.reverseList();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertEquals(elementB, sortedList.get(0));
        assertEquals(elementA, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));
    }

    @Test
    public void When_CallToOrderByName_Expect_ElementAIsFirstAndElementCIsLast() {
        sortingMethod.sortByName();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertEquals(elementA, sortedList.get(0));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));
    }

    @Test
    public void When_CallToOrderByAmount_Expect_ElementCIsFirstAndElementAIsLast() {
        sortingMethod.sortByAmount();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertEquals(elementC, sortedList.get(0));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementA, sortedList.get(2));
    }

    @Test
    public void When_CallToOrderByDate_Expect_ElementAIsFirstAndElementCIsLast() {
        sortingMethod.sortByDate();
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertEquals(elementA, sortedList.get(0));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));
    }

    @Test
    public void When_CallToFilterByCategory_Expect_StayOnlyElementA() {
        sortingMethod.sortByCategory(1);
        List<HistoryAndTransaction> sortedList = sortingMethod.getSortedList();
        assertTrue(sortedList.contains(elementA));
        assertFalse(sortedList.contains(elementB));
        assertFalse(sortedList.contains(elementC));
    }

}