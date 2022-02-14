package com.example.budgetmanagement.database.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is incoming fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}