package com.example.budgetmanagement.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.History.HistoryFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortingMarkIconManagerTest {
    public FragmentScenario<HistoryFragment> historyFragmentScenario;

    @Before
    public void setup() {
        historyFragmentScenario = FragmentScenario.launchInContainer(HistoryFragment.class);
        historyFragmentScenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void When_AfterLoadFragment_Expect_SortingMarksConstraintLayoutIsGone() {
        onView(withId(R.id.mainLayout));
        onView(withId(R.id.sortingMarksConstraintLayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void When_AfterLoadFragment_Expect_AllSortingMarksAreGone() {
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.reverseSortingMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void When_UnselectAnyFilters_Expect_SortingMarksConstraintLayoutIsGone() {
//        Write this when you have better sorting gui
    }

    @Test
    public void When_UnselectAnyFilters_Expect_AllSortingMarksAreGone() {
//        Write this when you have better sorting gui
    }

    @Test
    public void When_SelectedCategoryToFilterBy_Expect_SortingMarksConstraintLayoutIsVisible() {
        onView(withId(R.id.categoryFilterButton)).perform(click());
        onView(withId(R.id.recyclerView)).perform(actionOnItem(withChild(withText(R.string.category_example_various)), click()));
        onView(withId(R.id.sortingMarksConstraintLayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_SelectedCategoryToFilterBy_Expect_CategoryFilterMarkIconIsVisible() {
        onView(withId(R.id.categoryFilterButton)).perform(click());
        onView(withId(R.id.recyclerView)).perform(actionOnItem(withChild(withText(R.string.category_example_various)), click()));
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_SelectedCategoryToFilterBy_Expect_CategoryFilterMarkIconHasTheSameIconAsWhatSelectedCategory() {
        onView(withId(R.id.categoryFilterButton)).perform(click());
        onView(withId(R.id.recyclerView)).perform(actionOnItem(withChild(withText(R.string.category_example_various)), click()));
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.ic_baseline_category_24))));

        onView(withId(R.id.categoryFilterButton)).perform(click());
        onView(withId(R.id.recyclerView)).perform(actionOnItem(withChild(withText(R.string.category_example_foodstuff)), click()));
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.ic_baseline_shopping_basket_24))));

        onView(withId(R.id.categoryFilterButton)).perform(click());
        onView(withId(R.id.recyclerView)).perform(actionOnItem(withChild(withText(R.string.category_example_salary)), click()));
        onView(withId(R.id.categoryFilterMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.ic_baseline_work_24))));
    }


    @Test
    public void When_ProfitInFilterIsSelected_Expect_IncomeStatementMarkIconIsVisible() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.profit)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_ProfitAndLossInFilterIsSelected_Expect_IncomeStatementMarkIconIsGone() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.profitAndLoss)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void When_LossInFilterIsSelected_Expect_IncomeStatementMarkIconIsVisible() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.loss)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_ProfitInFilterIsSelected_Expect_IncomeStatementMarkIconHasProfitIcon() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.profit)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.profit_icon))));
    }

    @Test
    public void When_LossInFilterIsSelected_Expect_IncomeStatementMarkIconHasLossIcon() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.loss)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.incomeStatementMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.loss_icon))));
    }


    @Test
    public void When_SelectedReverseSorting_Expect_ReverseSortingMarkIconIsVisible() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.reversedCheck)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.reverseSortingMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_SelectedWayToSortBy_Expect_OrderSortingMarkIconIsVisible() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void When_SelectedWayToSortBy_Expect_OrderSortingMarkIconHasTheSameIconAsWhatSelectedWay() {
        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.nameSort)).perform(click());
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.block))));

        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.amountSort)).perform(click());
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.dollar_coin))));

        onView(withId(R.id.orderFilterButton)).perform(click());
        onView(withId(R.id.dateSort)).perform(click());
        onView(withId(R.id.orderSortingMarkIcon)).check(matches(withTagValue(equalTo(R.drawable.calendar))));
    }
}