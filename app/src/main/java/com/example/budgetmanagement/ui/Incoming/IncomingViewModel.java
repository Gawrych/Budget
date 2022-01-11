package com.example.budgetmanagement.ui.Incoming;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IncomingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IncomingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is incoming fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}