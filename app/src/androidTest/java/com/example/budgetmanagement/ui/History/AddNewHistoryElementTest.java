package com.example.budgetmanagement.ui.History;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.Coming.AddNewComingElement;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddNewHistoryElementTest extends TestCase {

    @Before
    public void setup() {
        FragmentScenario<AddNewHistoryElement> addNewComingFragmentScenario = FragmentScenario.launchInContainer(AddNewHistoryElement.class);
        addNewComingFragmentScenario.moveToState(Lifecycle.State.RESUMED);
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
    public void When_LoadFragment_Expect_CategoryFieldHasDefaultValue() {
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.category_example_various)));
    }

    @Test
    public void When_LoadFragment_Expect_DateFieldHasDefaultValue() {
        Calendar calendar = Calendar.getInstance();
        String parsedDate = DateProcessor.parseDate(calendar.getTimeInMillis(), DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).check(matches(withText(parsedDate)));
    }
}
