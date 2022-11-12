package com.example.budgetmanagement.ui.coming;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddNewComingElementTest extends TestCase {

    @Before
    public void setup() {
        FragmentScenario<AddNewComingElement> addNewComingFragmentScenario = FragmentScenario.launchInContainer(AddNewComingElement.class);
        addNewComingFragmentScenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void When_LoadFragment_Expect_CyclicalFieldsAreGone() {
        onView(withId(R.id.timeBetweenPayLayout)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.endDateLayout)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.timeBetweenPay)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.endDate)).check(matches(not(isCompletelyDisplayed())));
    }

    @Test
    public void When_LoadFragment_Expect_CategoryFieldHasDefaultValue() {
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.category_example_various)));
    }

    @Test
    public void When_LoadFragment_Expect_DateFieldHasDefaultValue() {
        Calendar calendar = Calendar.getInstance();
        String parsedDate = DateProcessor.parseDate(calendar.getTimeInMillis(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).check(matches(withText(parsedDate)));
    }

    @Test
    public void When_LoadFragment_Expect_TitleFieldIsEmpty() {
        onView(withId(R.id.title)).check(matches(withText("")));
    }

    @Test
    public void When_LoadFragment_Expect_AmountFieldIsEmpty() {
        onView(withId(R.id.amount)).check(matches(withText("")));
    }

    @Test
    public void When_ClickToShowCyclicalFields_Expect_CyclicalFieldsAreVisible() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPayLayout)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.endDateLayout)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.timeBetweenPay)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.endDate)).check(matches(isCompletelyDisplayed()));
    }
}