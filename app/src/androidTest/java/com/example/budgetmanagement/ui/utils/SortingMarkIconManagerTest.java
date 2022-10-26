package com.example.budgetmanagement.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.example.budgetmanagement.ui.DrawableMatchers.withImageDrawable;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;
import static org.hamcrest.CoreMatchers.not;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.ui.History.HistoryFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortingMarkIconManagerTest {

    private final HashMap<Integer, Integer> filters =  new HashMap<>();
    private final List<Category> categoryList = new ArrayList<>();
    private SortingMarkIconManager manager;

    @Before
    public void setup() {
        FragmentScenario<HistoryFragment> historyFragmentScenario = FragmentScenario.launchInContainer(HistoryFragment.class);
        historyFragmentScenario.moveToState(Lifecycle.State.RESUMED);

        categoryList.add(new Category(1, "Różne", 190, 1, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay()));
        categoryList.add(new Category(2, "Spożywcze", 255, 1, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay()));
        categoryList.add(new Category(3, "Paliwo", 256, 1, LocalDate.now().toEpochDay(), LocalDate.now().toEpochDay()));

        historyFragmentScenario.onFragment(fragment ->
                manager = new SortingMarkIconManager(Objects.requireNonNull(fragment.getView()), categoryList));
    }

    private void setMarkIcons(HashMap<Integer, Integer> filters) throws Throwable {
        runOnUiThread(() -> manager.setMarkIcons(filters));
    }

    @Test
    public void When_AfterLoadFragment_Expect_AllSortingMarksAreInvisible() {
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.reverseSortingMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(not(isCompletelyDisplayed())));
    }

    @Test
    public void When_UnselectAnyFilters_Expect_AllSortingMarksAreInvisible() throws Throwable {
        filters.put(PROFIT_FILTER_ID, 1);
        filters.put(CATEGORY_FILTER_ID, 1);
        filters.put(ORDER_FILTER_ID, 1);
        filters.put(REVERSE_FILTER_ID, 1);
        setMarkIcons(filters);

        filters.put(PROFIT_FILTER_ID, 0);
        filters.put(CATEGORY_FILTER_ID, 0);
        filters.put(ORDER_FILTER_ID, 0);
        filters.put(REVERSE_FILTER_ID, 0);
        setMarkIcons(filters);

        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.reverseSortingMarkIcon)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(not(isCompletelyDisplayed())));
    }

    @Test
    public void When_SelectedCategoryToFilterBy_Expect_CategoryFilterMarkIconIsVisible() throws Throwable {
        filters.put(CATEGORY_FILTER_ID, 1);
        setMarkIcons(filters);

        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void When_SelectedCategoryToFilterBy_Expect_CategoryFilterMarkHasTheSameIconAsWhatSelectedCategory() throws Throwable {
        filters.put(CATEGORY_FILTER_ID, 1);
        setMarkIcons(filters);
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withImageDrawable(R.drawable.ic_baseline_category_24)));

        filters.put(CATEGORY_FILTER_ID, 2);
        setMarkIcons(filters);
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withImageDrawable(R.drawable.ic_baseline_shopping_basket_24)));

        filters.put(CATEGORY_FILTER_ID, 3);
        setMarkIcons(filters);
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withImageDrawable(R.drawable.ic_baseline_work_24)));
    }

    @Test
    public void When_ProfitInFilterIsSelected_Expect_IncomeStatementMarkHasProfitIcon() throws Throwable {
        filters.put(PROFIT_FILTER_ID, 1);
        setMarkIcons(filters);

        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.incomeStatementMarkIcon))
                .check(matches(withImageDrawable(R.drawable.profits)));
    }

    @Test
    public void When_LossInFilterIsSelected_Expect_IncomeStatementMarkIconIsVisible() throws Throwable {
        filters.put(PROFIT_FILTER_ID, 2);
        setMarkIcons(filters);

        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.incomeStatementMarkIcon))
                .check(matches(withImageDrawable(R.drawable.loss_profit)));
    }

    @Test
    public void When_ProfitAndLossInFilterIsSelected_Expect_IncomeStatementMarkIconIsInvisible() throws Throwable {
        filters.put(PROFIT_FILTER_ID, 0);
        setMarkIcons(filters);

        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(not(isCompletelyDisplayed())));
    }

    @Test
    public void When_SelectedReverseSorting_Expect_ReverseSortingMarkIconIsVisible() throws Throwable {
        filters.put(REVERSE_FILTER_ID, 1);
        setMarkIcons(filters);

        onView(withId(R.id.reverseSortingMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.reverseSortingMarkIcon))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_SelectedWayToSortBy_Expect_OrderSortingMarkIconHasTheSameIconAsWhatSelectedWay() throws Throwable {
        filters.put(ORDER_FILTER_ID, 1);
        setMarkIcons(filters);
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.orderSortingMarkIcon))
                .check(matches(withImageDrawable(R.drawable.block)));

        filters.put(ORDER_FILTER_ID, 2);
        setMarkIcons(filters);
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.orderSortingMarkIcon))
                .check(matches(withImageDrawable(R.drawable.dollar_coin)));

        filters.put(ORDER_FILTER_ID, 3);
        setMarkIcons(filters);
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.orderSortingMarkIcon))
                .check(matches(withImageDrawable(R.drawable.calendar)));
    }
}