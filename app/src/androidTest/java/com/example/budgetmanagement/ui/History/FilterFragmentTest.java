package com.example.budgetmanagement.ui.History;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.budgetmanagement.ui.DrawableMatchers.withBackgroundColor;
import static com.example.budgetmanagement.ui.DrawableMatchers.withImageDrawable;
import static org.hamcrest.CoreMatchers.not;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.budgetmanagement.R;

import org.junit.Before;
import org.junit.Test;

public class FilterFragmentTest {

    public FragmentScenario<FilterFragment> filterFragmentScenario;

    @Before
    public void setUp() {
        filterFragmentScenario = FragmentScenario.launchInContainer(FilterFragment.class);
        filterFragmentScenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void When_clickToSortMethod_Expect_ChangeIconToColorVersion() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block_color)));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin_color)));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar_color)));
    }

    @Test
    public void When_clickToSortMethod_Expect_ChangeBackgroundColor() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).check(matches(withBackgroundColor(R.color.very_light_gray)));
    }

    @Test
    public void When_unselectSortMethod_Expect_ResetIconToBorderVersion() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block_color)));
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block)));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin)));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar)));
    }

    @Test
    public void When_unselectSortMethod_Expect_ResetBackgroundColor() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));
    }

    @Test
    public void When_selectOtherSortMethod_Expect_ResetIconToBorderVersion() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByNameIcon)).check(matches(withImageDrawable(R.drawable.block)));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByAmountIcon)).check(matches(withImageDrawable(R.drawable.dollar_coin)));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByDateIcon)).check(matches(withImageDrawable(R.drawable.calendar)));
    }

    @Test
    public void When_selectOtherSortMethod_Expect_ResetBackgroundColor() {
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));

        onView(withId(R.id.sortByAmountLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByAmountLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));

        onView(withId(R.id.sortByDateLayout)).perform(click());
        onView(withId(R.id.sortByNameLayout)).perform(click());
        onView(withId(R.id.sortByDateLayout)).check(matches(not(withBackgroundColor(R.color.very_light_gray))));
    }

    @Test
    public void When_clickToShowBottomSheetToSelectCategory_Expect_showBottomSheetWithCategories() {
        onView(withId(R.id.showCategoryBottomSheetSelector)).perform(click());
        onView(withChild(withText(R.string.category_example_various)))
                .check(matches(isDisplayed()));
    }

}