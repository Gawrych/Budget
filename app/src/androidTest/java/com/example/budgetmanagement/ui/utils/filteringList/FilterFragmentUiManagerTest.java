package com.example.budgetmanagement.ui.utils.filteringList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.example.budgetmanagement.ui.DrawableMatchers.withBackground;
import static com.example.budgetmanagement.ui.DrawableMatchers.withBackgroundColor;
import static com.example.budgetmanagement.ui.DrawableMatchers.withImageDrawable;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_AMOUNT_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_DATE_METHOD;
import static com.example.budgetmanagement.ui.utils.filteringList.SortListByFilters.SORT_BY_NAME_METHOD;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class FilterFragmentUiManagerTest {

    private FragmentScenario<FilterFragment> filterFragmentScenario;
    private FilterFragmentUiManager filterFragmentUiManager;

    @Before
    public void beforeClass() {
        filterFragmentScenario = FragmentScenario.launchInContainer(FilterFragment.class);
        filterFragmentScenario.moveToState(Lifecycle.State.RESUMED);
        filterFragmentScenario.onFragment(action -> {
            View view = action.getView();
            CategoryBottomSheetSelector selector = new CategoryBottomSheetSelector(action);
            if (view != null) {
                setupUiManager(view, selector);
            } else {
                throw new NullPointerException();
            }
        });
    }

    private void setupUiManager(@NonNull View view, @NonNull CategoryBottomSheetSelector selector) {
        filterFragmentUiManager = new FilterFragmentUiManager(view, selector);
    }

    private void selectOrderSorting(int sortMethod) throws Throwable {
        runOnUiThread(() -> filterFragmentUiManager.selectOrderSorting(sortMethod));
    }

    private void markUiFilter(HashMap<Integer, Integer> filters) throws Throwable {
        runOnUiThread(() -> filterFragmentUiManager.markUiFilters(filters));
    }

    @Test
    public void When_CallSelectOrderSorting_Expect_ChangeDrawable() throws Throwable {
        selectOrderSorting(SORT_BY_NAME_METHOD);
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block_color)));

        selectOrderSorting(SORT_BY_AMOUNT_METHOD);
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin_color)));

        selectOrderSorting(SORT_BY_DATE_METHOD);
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar_color)));
    }

    @Test
    public void When_CallSelectOrderSorting_Expect_SetCorrectSortMethodToGet() throws Throwable {
        selectOrderSorting(SORT_BY_NAME_METHOD);
        assertTrue(filterFragmentUiManager.isSelectedToSortByName());

        selectOrderSorting(SORT_BY_AMOUNT_METHOD);
        assertTrue(filterFragmentUiManager.isSelectedToSortByAmount());

        selectOrderSorting(SORT_BY_DATE_METHOD);
        assertTrue(filterFragmentUiManager.isSelectedToSortByDate());
    }

    @Test
    public void When_callMarkUiFilters_Expect_MarkCorrectFilters() throws Throwable {
        HashMap<Integer, Integer> filters = new HashMap<>();
        filters.put(PROFIT_FILTER_ID, 1);
        markUiFilter(filters);
        onView(withId(R.id.profitIcon)).check(matches(withBackground(R.drawable.profits_color)));

        filters.put(PROFIT_FILTER_ID, 2);
        markUiFilter(filters);
        onView(withId(R.id.lossIcon)).check(matches(withBackground(R.drawable.loss_profit_color)));

        filters.put(ORDER_FILTER_ID, 1);
        markUiFilter(filters);
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block_color)));
        onView(withId(R.id.sortByNameLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));

        filters.put(ORDER_FILTER_ID, 2);
        markUiFilter(filters);
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin_color)));
        onView(withId(R.id.sortByAmountLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));

        filters.put(ORDER_FILTER_ID, 3);
        markUiFilter(filters);
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar_color)));
        onView(withId(R.id.sortByDateLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));

        filters.put(REVERSE_FILTER_ID, 1);
        markUiFilter(filters);
        onView(withId(R.id.reversedCheck)).check(matches(withBackground(R.drawable.swap_in_color)));
        onView(withId(R.id.reversedCheckTitle)).check(matches(withText(R.string.high_to_low)));

        filters.put(CATEGORY_FILTER_ID, 1);
        markUiFilter(filters);
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.category_example_various)));
    }

    @Test
    public void When_CallMarkUiFilter_Expect_SetCorrectValueInVariable() throws Throwable {
        HashMap<Integer, Integer> filters = new HashMap<>();
        filters.put(ORDER_FILTER_ID, 1);
        markUiFilter(filters);
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block_color)));
        onView(withId(R.id.sortByNameLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));
        assertTrue(filterFragmentUiManager.isSelectedToSortByName());
        assertFalse(filterFragmentUiManager.isSelectedToSortByAmount());
        assertFalse(filterFragmentUiManager.isSelectedToSortByDate());


        filters.put(ORDER_FILTER_ID, 2);
        markUiFilter(filters);
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin_color)));
        onView(withId(R.id.sortByAmountLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));
        assertFalse(filterFragmentUiManager.isSelectedToSortByName());
        assertTrue(filterFragmentUiManager.isSelectedToSortByAmount());
        assertFalse(filterFragmentUiManager.isSelectedToSortByDate());

        filters.put(ORDER_FILTER_ID, 3);
        markUiFilter(filters);
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar_color)));
        onView(withId(R.id.sortByDateLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));
        assertFalse(filterFragmentUiManager.isSelectedToSortByName());
        assertFalse(filterFragmentUiManager.isSelectedToSortByAmount());
        assertTrue(filterFragmentUiManager.isSelectedToSortByDate());
    }

    @Test
    public void When_CallMarkUiFilterWithEmptyList_Expect_DoNotMarkAnything() throws Throwable {
        HashMap<Integer, Integer> filters = new HashMap<>();
        markUiFilter(filters);
        onView(withId(R.id.profitIcon)).check(matches(withBackground(R.drawable.profits)));
        onView(withId(R.id.lossIcon)).check(matches(withBackground(R.drawable.loss_profit)));
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block)));
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));
        onView(withId(R.id.reversedCheck)).check(matches(withBackground(R.drawable.swap)));
        onView(withId(R.id.reversedCheckTitle)).check(matches(withText(R.string.low_to_high)));
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.select_category)));
    }

    @Test
    public void When_CallMarkUiFilterWithOutOfRangeValues_Expect_DoNotMarkAnything() throws Throwable {
        HashMap<Integer, Integer> filters = new HashMap<>();
        filters.put(PROFIT_FILTER_ID, 3);
        markUiFilter(filters);
        onView(withId(R.id.profitIcon)).check(matches(withBackground(R.drawable.profits)));
        onView(withId(R.id.lossIcon)).check(matches(withBackground(R.drawable.loss_profit)));

        filters.put(ORDER_FILTER_ID, 4);
        markUiFilter(filters);
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block)));
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin)));
        onView(withId(R.id.sortByAmountLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar)));
        onView(withId(R.id.sortByDateLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));

        filters.put(REVERSE_FILTER_ID, 3);
        markUiFilter(filters);
        onView(withId(R.id.reversedCheck)).check(matches(withBackground(R.drawable.swap)));
        onView(withId(R.id.reversedCheckTitle)).check(matches(withText(R.string.low_to_high)));
    }

    @Test
    public void When_CallMarkUiFilterWithOutOfRangeCategoryValue_Expect_SetDefaultValue() throws Throwable {
        HashMap<Integer, Integer> filters = new HashMap<>();
        filters.put(CATEGORY_FILTER_ID, -1);
        markUiFilter(filters);
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.select_category)));
    }

}