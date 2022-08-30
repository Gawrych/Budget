package com.example.budgetmanagement.ui.utils;

import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.HistoryAndTransaction;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SortListByFiltersTest {

    private final HashMap<Integer, Integer> filters = new HashMap<>();
    private final List<HistoryAndTransaction> originalList = new ArrayList<>();
    private SortListByFilters sortListByFilters;
    private HistoryAndTransaction elementA;
    private HistoryAndTransaction elementB;
    private HistoryAndTransaction elementC;

    @Before
    public void setUp() {
        elementA = new HistoryAndTransaction();
        elementA.transaction = new Transaction(1, 1, 0, "A", "1", 1, LocalDate.now().toEpochDay(), true);
        elementA.history = new History(1, 1, LocalDate.now().toEpochDay());

        elementB = new HistoryAndTransaction();
        elementB.transaction = new Transaction(2, 2, 0, "B", "-2", 2, LocalDate.now().toEpochDay(), false);
        elementB.history = new History(2, 2, LocalDate.now().toEpochDay());

        elementC = new HistoryAndTransaction();
        elementC.transaction = new Transaction(3, 2, 0, "C", "-3", 3, LocalDate.now().toEpochDay(), false);
        elementC.history = new History(3, 3, LocalDate.now().toEpochDay());

        originalList.add(elementC);
        originalList.add(elementA);
        originalList.add(elementB);

        filters.put(PROFIT_FILTER_ID, 0);
        filters.put(ORDER_FILTER_ID, 0);
        filters.put(CATEGORY_FILTER_ID, 0);
        filters.put(REVERSE_FILTER_ID, 0);
    }

    @Test
    public void When_FilterByProfit_Expect_ShowOnlyElementA() {
        filters.put(PROFIT_FILTER_ID, 1);
        sortListByFilters = new SortListByFilters(originalList, filters);

        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertTrue(sortedList.contains(elementA));
        assertFalse(sortedList.contains(elementB));
        assertFalse(sortedList.contains(elementC));
    }

    @Test
    public void When_FilterByLoss_Expect_ShowOnlyElementBAndElementC() {
        filters.put(PROFIT_FILTER_ID, 2);
        sortListByFilters = new SortListByFilters(originalList, filters);

        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertFalse(sortedList.contains(elementA));
        assertTrue(sortedList.contains(elementB));
        assertTrue(sortedList.contains(elementC));
    }

    @Test
    public void When_SetCategoryFilter_Expect_ShowOnlyElementWithThatCategoryId() {
        filters.put(CATEGORY_FILTER_ID, 2);
        sortListByFilters = new SortListByFilters(originalList, filters);

        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertFalse(sortedList.contains(elementA));
        assertTrue(sortedList.contains(elementB));
        assertTrue(sortedList.contains(elementC));

        filters.put(CATEGORY_FILTER_ID, 1);
        sortListByFilters = new SortListByFilters(originalList, filters);

        sortedList = sortListByFilters.sort();

        assertTrue(sortedList.contains(elementA));
        assertFalse(sortedList.contains(elementB));
        assertFalse(sortedList.contains(elementC));
    }

    @Test
    public void When_SelectOrder_Expect_SetElementInSelectedOrder() {
        filters.put(ORDER_FILTER_ID, 1);
        sortListByFilters = new SortListByFilters(originalList, filters);
        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertEquals(elementA, sortedList.get(0));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));

        filters.put(ORDER_FILTER_ID, 2);
        sortListByFilters = new SortListByFilters(originalList, filters);
        sortedList = sortListByFilters.sort();

        assertEquals(elementA, sortedList.get(2));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementC, sortedList.get(0));

        filters.put(ORDER_FILTER_ID, 3);
        sortListByFilters = new SortListByFilters(originalList, filters);
        sortedList = sortListByFilters.sort();

        assertEquals(elementA, sortedList.get(0));
        assertEquals(elementB, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));
    }

    @Test
    public void When_SelectReverse_Expect_ReverseList() {
        filters.put(REVERSE_FILTER_ID, 1);
        sortListByFilters = new SortListByFilters(originalList, filters);
        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertEquals(elementB, sortedList.get(0));
        assertEquals(elementA, sortedList.get(1));
        assertEquals(elementC, sortedList.get(2));
    }

    @Test
    public void When_SelectMultipleFilters_Expect_ApplyFilters() {
        filters.put(PROFIT_FILTER_ID, 2);
        filters.put(ORDER_FILTER_ID, 2);
        filters.put(CATEGORY_FILTER_ID, 2);
        filters.put(REVERSE_FILTER_ID, 1);
        sortListByFilters = new SortListByFilters(originalList, filters);
        List<HistoryAndTransaction> sortedList = sortListByFilters.sort();

        assertFalse(sortedList.contains(elementA));
        assertTrue(sortedList.contains(elementB));
        assertTrue(sortedList.contains(elementC));
        assertEquals(elementB, sortedList.get(0));
        assertEquals(elementC, sortedList.get(1));
    }

}