package com.example.budgetmanagement.ui.utils.filteringList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.CATEGORY_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.ORDER_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.PROFIT_FILTER_ID;
import static com.example.budgetmanagement.ui.utils.filteringList.FilterFragment.REVERSE_FILTER_ID;
import static org.junit.Assert.assertEquals;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.budgetmanagement.R;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class FiltersCollectorTest {

    private FragmentScenario<FilterFragment> filterFragmentScenario;
    private HashMap<Integer, Integer> filtersToCheck;

    @Before
    public void beforeClass() {
        filterFragmentScenario = FragmentScenario.launchInContainer(FilterFragment.class);
        filterFragmentScenario.moveToState(Lifecycle.State.RESUMED);
    }

    private void getFiltersToCheck() {
        filterFragmentScenario.onFragment(action -> filtersToCheck = action.getFiltersFromCollector());
    }

    @Test
    public void When_CallSelectOrderSorting_Expect_ChangeDrawable() {
        HashMap<Integer, Integer> correctFilters = new HashMap<>();
        correctFilters.put(PROFIT_FILTER_ID, 1);
        correctFilters.put(ORDER_FILTER_ID, 1);
        correctFilters.put(REVERSE_FILTER_ID, 1);
        correctFilters.put(CATEGORY_FILTER_ID, 1);

        onView(withId(R.id.profitIcon)).perform(click());
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.reversedCheck)).perform(click());
        onView(withId(R.id.categorySelector)).perform(click());
        onView(withChild(withText(R.string.category_example_various))).perform(click());

        getFiltersToCheck();

        assertEquals(correctFilters.get(PROFIT_FILTER_ID), filtersToCheck.get(PROFIT_FILTER_ID));
        assertEquals(correctFilters.get(ORDER_FILTER_ID), filtersToCheck.get(ORDER_FILTER_ID));
        assertEquals(correctFilters.get(REVERSE_FILTER_ID), filtersToCheck.get(REVERSE_FILTER_ID));
        assertEquals(correctFilters.get(CATEGORY_FILTER_ID), filtersToCheck.get(CATEGORY_FILTER_ID));


        correctFilters.put(PROFIT_FILTER_ID, 2);
        correctFilters.put(ORDER_FILTER_ID, 2);
        correctFilters.put(REVERSE_FILTER_ID, 0);
        correctFilters.put(CATEGORY_FILTER_ID, 3);

        onView(withId(R.id.lossIcon)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.reversedCheck)).perform(click());
        onView(withId(R.id.categorySelector)).perform(click());
        onView(withChild(withText(R.string.category_example_salary))).perform(click());

        getFiltersToCheck();

        assertEquals(correctFilters.get(PROFIT_FILTER_ID), filtersToCheck.get(PROFIT_FILTER_ID));
        assertEquals(correctFilters.get(ORDER_FILTER_ID), filtersToCheck.get(ORDER_FILTER_ID));
        assertEquals(correctFilters.get(REVERSE_FILTER_ID), filtersToCheck.get(REVERSE_FILTER_ID));
        assertEquals(correctFilters.get(CATEGORY_FILTER_ID), filtersToCheck.get(CATEGORY_FILTER_ID));
    }
}