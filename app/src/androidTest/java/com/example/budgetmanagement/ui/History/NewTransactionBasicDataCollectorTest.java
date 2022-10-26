package com.example.budgetmanagement.ui.History;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.ui.utils.CategoryBottomSheetSelector;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.example.budgetmanagement.ui.utils.GetViewTransactionFields;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewTransactionBasicDataCollectorTest extends TestCase {

    private NewTransactionDataCollector newTransactionDataCollector;
    private Fragment rootFragment;
    private final String titleValue = "Hello World!";
    private final String amountValue = "1.00";


    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }

    @Before
    public void beforeClass() {
        FragmentScenario<AddNewHistoryElement> addNewComingElementFragmentScenario = FragmentScenario.launchInContainer(AddNewHistoryElement.class);
        addNewComingElementFragmentScenario.moveToState(Lifecycle.State.RESUMED);
        addNewComingElementFragmentScenario.onFragment(action -> {
            setDataCollector(action);
            setRootFragment(action);
        });
    }

    private void setDataCollector(GetViewTransactionFields getViewTransactionFields) {
        newTransactionDataCollector = new NewTransactionDataCollector(getViewTransactionFields);
    }

    private void setRootFragment(Fragment fragment) {
        this.rootFragment = fragment;
    }

    private NewTransactionDataCollector collectDataWithFilledTitleAndAmount() {
        onView(withId(R.id.title)).perform(typeText(titleValue));
        onView(withId(R.id.amount)).perform(typeText(amountValue));
        boolean correctlyCollected = newTransactionDataCollector.collectData();
        if (!correctlyCollected) {
            fail();
        }
        return newTransactionDataCollector;
    }

    @Test
    public void When_TypedTitle_Expect_CorrectlyCollectContent() {
        onView(withId(R.id.title)).perform(typeText(titleValue));
        onView(withId(R.id.amount)).perform(typeText(amountValue));
        boolean correctlyCollected = newTransactionDataCollector.collectData();
        if (!correctlyCollected) {
            fail();
        }
        String title = newTransactionDataCollector.getTransaction().getTitle();
        assertEquals(titleValue, title);
    }

    @Test
    public void When_TypedAmount_Expect_CorrectlyCollectContent() {
        onView(withId(R.id.profitSwitch)).perform(scrollTo(), click());
        NewTransactionDataCollector newTransaction = collectDataWithFilledTitleAndAmount();
        String amountContent = newTransaction.getTransaction().getAmount();
        assertEquals(amountValue, amountContent);
    }

    @Test
    public void When_CheckProfitSwitch_Expect_PositiveAmountValue() {
        onView(withId(R.id.profitSwitch)).perform(scrollTo(), click());
        NewTransactionDataCollector newTransaction = collectDataWithFilledTitleAndAmount();
        String amountContent = newTransaction.getTransaction().getAmount();
        assertEquals(amountValue, amountContent);
    }

    @Test
    public void When_UncheckProfitSwitch_Expect_NegativeAmountValue() {
        NewTransactionDataCollector newTransaction = collectDataWithFilledTitleAndAmount();
        String amountContent = newTransaction.getTransaction().getAmount();
        assertEquals("-"+amountValue, amountContent);
    }

    @Test
    public void When_TitleFieldIsEmpty_Expect_CollectingDataFailed() {
        boolean correctlyCollected = newTransactionDataCollector.collectData();
        if (correctlyCollected) {
            fail();
        }
    }

    @Test
    public void When_AmountFieldIsEmpty_Expect_CollectingDataFailed() {
        onView(withId(R.id.title)).perform(typeText(titleValue));
        boolean correctlyCollected = newTransactionDataCollector.collectData();
        if (correctlyCollected) {
            fail();
        }
    }

    @Test
    public void When_SelectCategory_Expect_ChangeValue() {
        onView(withId(R.id.categorySelector)).perform(click());
        onView(withText(R.string.category_example_foodstuff)).perform(click());
        onView(withId(R.id.categorySelector)).check(matches(withText(R.string.category_example_foodstuff)));
    }

    @Test
    public void When_SelectCategory_Expect_CorrectlyCollectContent() {
        onView(withId(R.id.categorySelector)).perform(click());
        onView(withText(R.string.category_example_foodstuff)).perform(click());
        NewTransactionDataCollector collectedTransactionFields =
                collectDataWithFilledTitleAndAmount();
        String categoryName = CategoryBottomSheetSelector.getCategoryName(collectedTransactionFields.getTransaction().getCategoryId(), rootFragment);
        assertEquals(getResourceString(R.string.category_example_foodstuff), categoryName);
    }

    @Test
    public void When_SelectDateInStartDatePicker_Expect_CorrectlyCollectContent() {
        long newDateInMillis = getTomorrow().getTimeInMillis();
        String newDateInPattern = DateProcessor.parseDate(newDateInMillis, DateProcessor.MONTH_NAME_YEAR_DATE_FORMAT);
        onView(withId(R.id.startDate)).perform(replaceText(newDateInPattern));
        NewTransactionDataCollector collectedTransactionFields = collectDataWithFilledTitleAndAmount();
        long collectDate = collectedTransactionFields.getTransaction().getAddDate();
        assertEquals(newDateInMillis, collectDate);
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
}