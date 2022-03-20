package com.example.budgetmanagement.ui.Category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.budgetmanagement.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewCategory extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText plannedBudgetEditText;
    private String name;
    private String plannedBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
//
//        nameEditText = findViewById(R.id.category_name);
//        plannedBudgetEditText = findViewById(R.id.planned_budget);
//
//        final ImageButton acceptButton = findViewById(R.id.accept_button);
//        acceptButton.setOnClickListener(view -> {
//
//            name = nameEditText.getText().toString();
//            plannedBudget = plannedBudgetEditText.getText().toString();
//
//            if (checkCorrectInputs()) {
//                closeActivity();
//            }
//        });
//
//        final ImageButton cancelButton = findViewById(R.id.cancel_button);
//        cancelButton.setOnClickListener(view -> {
//            finish();
//        });
//
//        final ImageView backArrow = findViewById(R.id.back_arrow);
//        backArrow.setOnClickListener(view -> {
//            finish();
//        });
    }

//    private void closeActivity() {
//        Intent intent = new Intent();
//        intent.putExtra("name", name);
//        intent.putExtra("plannedBudget", plannedBudget);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    private boolean checkCorrectInputs() {
//        if (name.length() == 0) {
//            nameEditText.setError(getString(R.string.errorEmptyField));
//            return false;
//        }
//        if (plannedBudget.length() == 0) {
//            plannedBudgetEditText.setError(getString(R.string.errorEmptyField));
//            return false;
//        }
//        return true;
//    }
}