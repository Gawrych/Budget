package com.example.budgetmanagement.ui.History;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HistoryFragmentTest {

    public FragmentScenario<HistoryFragment> historyFragmentScenario;

    @Before
    public void beforeClass() {
        historyFragmentScenario = FragmentScenario.launchInContainer(HistoryFragment.class);
        historyFragmentScenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void test() {

    }

}