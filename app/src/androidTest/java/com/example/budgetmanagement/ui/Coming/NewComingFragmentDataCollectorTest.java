package com.example.budgetmanagement.ui.Coming;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.budgetmanagement.ui.utils.DateProcessor.DEFAULT_DATE_FORMAT;
import static org.hamcrest.Matchers.equalTo;

import android.content.res.Resources;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.DateProcessor;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewComingFragmentDataCollectorTest extends TestCase {

    private NewComingFragmentDataCollector comingFragmentDataCollector;

    private Resources resources;

    @Before
    public void beforeClass() {
        FragmentScenario<AddNewComingElement> addNewComingElementFragmentScenario = FragmentScenario.launchInContainer(AddNewComingElement.class);
        addNewComingElementFragmentScenario.moveToState(Lifecycle.State.RESUMED);
        addNewComingElementFragmentScenario.onFragment(getViewComingFields -> {
            setDataCollector(getViewComingFields);
            setResources(getViewComingFields);
        });
    }

    private void setDataCollector(ComingFields comingFields) {
        comingFragmentDataCollector = new NewComingFragmentDataCollector(comingFields);
    }

    private void setResources(ComingFields comingFields) {
        resources = comingFields.getFragmentContext().getResources();
    }

    private NewComingFragmentDataCollector collectDataWithFilledRequireFields() {
        String titleValue = "Hello World!";
        onView(withId(R.id.title)).perform(typeText(titleValue));
        String amountValue = "1.00";
        onView(withId(R.id.amount)).perform(typeText(amountValue));

        boolean correctlyCollected = comingFragmentDataCollector.collectData();
        if (!correctlyCollected) {
            fail();
        }
        return comingFragmentDataCollector;
    }

    private Calendar getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    @Test
    public void When_SelectDateInEndDatePicker_Expect_EndDatePickerHasSelectedDate() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_day))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long newDateInMillis = getTomorrow().getTimeInMillis();
        String newDateInPattern = DateProcessor.parseDate(newDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(newDateInPattern));
        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        long collectDate = collectedComingFields.getEndDate();
        assertEquals(newDateInMillis, collectDate);
    }

    @Test
    public void When_SelectedTimeBetween_Expect_ChangeValueToSelected() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_year))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.timeBetweenPay)).check(matches(withText("Co rok")));
    }

    @Test
    public void When_SetDayCyclical_Expect_CorrectlyCollectedNextDates() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_day))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long startDateInMillis = getStartDate().getTimeInMillis();
        String startDateInPattern = DateProcessor.parseDate(startDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(startDateInPattern));

        long endDateInMillis = getEndDate(Calendar.JANUARY, 5, 2022).getTimeInMillis();
        String endDateInPattern = DateProcessor.parseDate(endDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(endDateInPattern));

        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        ArrayList<Long> nextDates = collectedComingFields.getNextDates();

        ArrayList<Long> exampleDates = new ArrayList<>();
        exampleDates.add(getMillisFromPattern("01.01.2022"));
        exampleDates.add(getMillisFromPattern("02.01.2022"));
        exampleDates.add(getMillisFromPattern("03.01.2022"));
        exampleDates.add(getMillisFromPattern("04.01.2022"));
        exampleDates.add(getMillisFromPattern("05.01.2022"));

        assertEquals(exampleDates, nextDates);
    }

    @Test
    public void When_SetMonthCyclical_Expect_CorrectlyCollectedNextDates() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_month))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long startDateInMillis = getStartDate().getTimeInMillis();
        String startDateInPattern = DateProcessor.parseDate(startDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(startDateInPattern));

        long endDateInMillis = getEndDate(Calendar.MARCH, 25, 2022).getTimeInMillis();
        String endDateInPattern = DateProcessor.parseDate(endDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(endDateInPattern));

        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        ArrayList<Long> nextDates = collectedComingFields.getNextDates();

        ArrayList<Long> exampleDates = new ArrayList<>();
        exampleDates.add(getMillisFromPattern("01.01.2022"));
        exampleDates.add(getMillisFromPattern("01.02.2022"));
        exampleDates.add(getMillisFromPattern("01.03.2022"));

        assertEquals(exampleDates, nextDates);
    }

    @Test
    public void When_SetWeekCyclical_Expect_CorrectlyCollectedNextDates() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_week))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long startDateInMillis = getStartDate().getTimeInMillis();
        String startDateInPattern = DateProcessor.parseDate(startDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(startDateInPattern));

        long endDateInMillis = getEndDate(Calendar.FEBRUARY, 13, 2022).getTimeInMillis();
        String endDateInPattern = DateProcessor.parseDate(endDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(endDateInPattern));

        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        ArrayList<Long> nextDates = collectedComingFields.getNextDates();

        ArrayList<Long> exampleDates = new ArrayList<>();
        exampleDates.add(getMillisFromPattern("01.01.2022"));
        exampleDates.add(getMillisFromPattern("08.01.2022"));
        exampleDates.add(getMillisFromPattern("15.01.2022"));
        exampleDates.add(getMillisFromPattern("22.01.2022"));
        exampleDates.add(getMillisFromPattern("29.01.2022"));
        exampleDates.add(getMillisFromPattern("05.02.2022"));
        exampleDates.add(getMillisFromPattern("12.02.2022"));

        assertEquals(exampleDates, nextDates);
    }

    @Test
    public void When_SetQuarterCyclical_Expect_CorrectlyCollectedNextDates() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_quarter))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long startDateInMillis = getStartDate().getTimeInMillis();
        String startDateInPattern = DateProcessor.parseDate(startDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(startDateInPattern));

        long endDateInMillis = getEndDate(Calendar.JULY, 1, 2022).getTimeInMillis();
        String endDateInPattern = DateProcessor.parseDate(endDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(endDateInPattern));

        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        ArrayList<Long> nextDates = collectedComingFields.getNextDates();

        ArrayList<Long> exampleDates = new ArrayList<>();
        exampleDates.add(getMillisFromPattern("01.01.2022"));
        exampleDates.add(getMillisFromPattern("01.04.2022"));
        exampleDates.add(getMillisFromPattern("01.07.2022"));

        assertEquals(exampleDates, nextDates);
    }

    @Test
    public void When_SetYearCyclical_Expect_CorrectlyCollectedNextDates() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_year))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        long startDateInMillis = getStartDate().getTimeInMillis();
        String startDateInPattern = DateProcessor.parseDate(startDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(startDateInPattern));

        long endDateInMillis = getEndDate(Calendar.JANUARY, 1, 2025).getTimeInMillis();
        String endDateInPattern = DateProcessor.parseDate(endDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.endDate)).perform(replaceText(endDateInPattern));

        NewComingFragmentDataCollector collectedComingFields = collectDataWithFilledRequireFields();
        ArrayList<Long> nextDates = collectedComingFields.getNextDates();

        ArrayList<Long> exampleDates = new ArrayList<>();
        exampleDates.add(getMillisFromPattern("01.01.2022"));
        exampleDates.add(getMillisFromPattern("01.01.2023"));
        exampleDates.add(getMillisFromPattern("01.01.2024"));
        exampleDates.add(getMillisFromPattern("01.01.2025"));

        assertEquals(exampleDates, nextDates);
    }

    @Test
    public void When_CollectDataWithEmptyEndDate_Expect_CollectedDataFailed() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.timeBetweenPay)).perform(click());
        onData(equalTo(resources.getString(R.string.each_year))).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        String titleValue = "Hello World!";
        onView(withId(R.id.title)).perform(typeText(titleValue));
        String amountValue = "1.00";
        onView(withId(R.id.amount)).perform(typeText(amountValue));
        boolean correctlyCollected = comingFragmentDataCollector.collectData();
        if (correctlyCollected) {
            fail();
        }
    }

    @Test
    public void When_CollectDataWithEmptyTimeBetween_Expect_CollectedDataFailed() {
        onView(withId(R.id.cyclicalSwitch)).perform(click());
        onView(withId(R.id.endDate)).perform(replaceText("13 march 2022"));

        String titleValue = "Hello World!";
        onView(withId(R.id.title)).perform(typeText(titleValue));
        String amountValue = "1.00";
        onView(withId(R.id.amount)).perform(typeText(amountValue));
        boolean correctlyCollected = comingFragmentDataCollector.collectData();
        if (correctlyCollected) {
            fail();
        }
    }

    private Calendar getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    private Calendar getEndDate(int month, int dayOfYear, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfYear);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    public long getMillisFromPattern(String dateInPattern) {
        LocalDate localDate = getSelectedDateInPattern(dateInPattern);
        return localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDate getSelectedDateInPattern(String dateInPattern) {
        return LocalDate.parse(dateInPattern, getPattern());
    }

    private DateTimeFormatter getPattern() {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    }

}