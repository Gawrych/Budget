package com.example.budgetmanagement.ui.Coming;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.example.budgetmanagement.MainActivity;
import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.Rooms.Category;
import com.example.budgetmanagement.database.Rooms.Coming;
import com.example.budgetmanagement.database.Rooms.ComingAndTransaction;
import com.example.budgetmanagement.database.Rooms.History;
import com.example.budgetmanagement.database.Rooms.Transaction;
import com.example.budgetmanagement.database.ViewModels.CategoryViewModel;
import com.example.budgetmanagement.database.ViewModels.ComingViewModel;
import com.example.budgetmanagement.database.ViewModels.HistoryViewModel;
import com.example.budgetmanagement.ui.Category.App;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Calendar;

public class ComingBottomSheetDetails extends Fragment {

    private final Context context;
    private final BottomSheetDialog bottomSheetDialog;
    private final HistoryViewModel historyViewModel;
    private final ComingViewModel comingViewModel;
    private final TextView transactionNameField;
    private final TextView addDateField;
    private final TextView remainingDaysField;
    private final TextView remainingDaysLabelField;
    private final TextView daysLabelField;
    private final TextView lastModifiedDateField;
    private final TextView deadlineDateField;
    private final TextView executedDate;
    private final View root;
    private final TextView amountField;
    private final CategoryViewModel categoryViewModel;
    private final TextView categoryName;
    private final ImageView categoryIcon;
    private final ImageView profitIcon;
    private final TextView executeLabel;
    private final ImageView executeIcon;
    private IconPack iconPack;
    private Transaction transaction;
    private Coming coming;

    // TODO: merge this class and inheritance parent class (maybe abstract) with CategoryBottomSheetDetails

    public ComingBottomSheetDetails(Context context, IconPack iconPack, ViewModelStoreOwner owner, View root) {
        this.context = context;
        this.iconPack = iconPack;
        this.root = root;

        this.comingViewModel = new ViewModelProvider(owner).get(ComingViewModel.class);
        this.historyViewModel = new ViewModelProvider(owner).get(HistoryViewModel.class);
        this.categoryViewModel = new ViewModelProvider(owner).get(CategoryViewModel.class);

        this.bottomSheetDialog = new BottomSheetDialog(context);
        this.bottomSheetDialog.setContentView(R.layout.coming_bottom_sheet_details);

        this.transactionNameField = bottomSheetDialog.findViewById(R.id.transactionName);
        this.addDateField = bottomSheetDialog.findViewById(R.id.addDate);
        this.remainingDaysLabelField = bottomSheetDialog.findViewById(R.id.remainingDaysLabel);
        this.remainingDaysField = bottomSheetDialog.findViewById(R.id.remainingDays);
        this.daysLabelField = bottomSheetDialog.findViewById(R.id.daysLabel);
        this.lastModifiedDateField = bottomSheetDialog.findViewById(R.id.lastEditDate);
        this.deadlineDateField = bottomSheetDialog.findViewById(R.id.remainingDate);
        this.executedDate = bottomSheetDialog.findViewById(R.id.dateWhenWasPaid);
        this.amountField = bottomSheetDialog.findViewById(R.id.amount);
        this.categoryName = bottomSheetDialog.findViewById(R.id.categoryName);
        this.categoryIcon = bottomSheetDialog.findViewById(R.id.categoryIcon);
        this.profitIcon = bottomSheetDialog.findViewById(R.id.profitIcon);
        this.executeLabel = bottomSheetDialog.findViewById(R.id.executeLabel);
        this.executeIcon = bottomSheetDialog.findViewById(R.id.executeIcon);

        LinearLayout deleteButton = bottomSheetDialog.findViewById(R.id.deleteLayout);
        LinearLayout editButton = bottomSheetDialog.findViewById(R.id.editLayout);
        LinearLayout executeButton = bottomSheetDialog.findViewById(R.id.executeLayout);
        LinearLayout duplicateButton = bottomSheetDialog.findViewById(R.id.duplicateLayout);

        assert deleteButton != null;
        deleteButton.setOnClickListener(v -> deleteItem());

        assert editButton != null;
        editButton.setOnClickListener(v -> editSelectedElement());

        assert duplicateButton != null;
        duplicateButton.setOnClickListener(v -> createNewComingByThisPattern());

        assert executeButton != null;
        executeButton.setOnClickListener(v -> executePay());
    }

    public void setData(ComingAndTransaction comingAndTransaction) {
        transaction = comingAndTransaction.transaction;
        coming = comingAndTransaction.coming;

        setTextInFields();

        boolean isBeforeDeadline = getRemainingDays(coming.getRepeatDate()) < 0;
        boolean isExecute = coming.isExecute();

        setVisibilityOfExecutedDate(GONE);
        setVisibilityOfRemainingDaysFields(VISIBLE);

        if (isBeforeDeadline) {
            setRemainingElements(R.string.Remain, R.color.font_default);
        } else {
            setRemainingElements(R.string.after_the_deadline, R.color.mat_red);
        }

        if (isExecute) {
            setRemainingElements(R.string.realized, R.color.main_green);
            setExecuteButtonProperties(R.string.cancel_completed, R.drawable.ic_baseline_cancel_24);
            setVisibilityOfRemainingDaysFields(GONE);
            setVisibilityOfExecutedDate(VISIBLE);
            executedDate.setText(DateProcessor.parseDate(coming.getExecutedDate()));
        }
    }

    private void setTextInFields() {
        Category category = getCategory(transaction);
        transactionNameField.setText(transaction.getTitle());
        amountField.setText(transaction.getAmount());
        addDateField.setText(DateProcessor.parseDate(coming.getAddDate()));
        lastModifiedDateField.setText(getLastEditDate(coming.getModifiedDate()));
        categoryName.setText(category.getName());

        setAmountIconDependOfValue(transaction.getAmount());
        setCategoryIcon(category);
        setExecuteButtonProperties(R.string.realize, R.drawable.ic_baseline_done_all_24);

        int remainingDays = getRemainingDays(coming.getRepeatDate());
        deadlineDateField.setText(DateProcessor.parseDate(coming.getRepeatDate()));
        remainingDaysField.setText(String.valueOf(Math.abs(remainingDays)));
    }

    private void setVisibilityOfExecutedDate(int visibility) {
        executedDate.setVisibility(visibility);
    }

    private void setVisibilityOfRemainingDaysFields(int visibility) {
        daysLabelField.setVisibility(visibility);
        remainingDaysField.setVisibility(visibility);
    }

    private void setCategoryIcon(Category category) {
        int iconId = category.getIcon();
        Drawable icon = iconPack.getIcon(iconId).getDrawable();
        categoryIcon.setImageDrawable(icon);
    }

    private Category getCategory(Transaction transaction) {
        int categoryId = transaction.getCategoryId();
        return categoryViewModel.getCategoryById(categoryId);
    }

    private void setAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            profitIcon.setColorFilter(context.getColor(R.color.mat_red));
        } else {
            profitIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            profitIcon.setColorFilter(context.getColor(R.color.main_green));
        }
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private void createNewComingByThisPattern() {
        Bundle bundle = new Bundle();
        bundle.putInt("comingId", this.coming.getComingId());

        Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_addNewComingElement, bundle);
        bottomSheetDialog.cancel();
    }

    private void editSelectedElement() {
        Bundle bundle = new Bundle();
        bundle.putInt("comingId", this.coming.getComingId());

        Navigation.findNavController(root)
                .navigate(R.id.action_navigation_incoming_to_editComingElement, bundle);
        bottomSheetDialog.cancel();
    }

    private Calendar getCalendarWithValue(long value) {
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setTimeInMillis(value);
        return calendarInstance;
    }

    private String getLastEditDate(long modifiedDate) {
        if (modifiedDate != 0) {
            return DateProcessor.parseDate(modifiedDate);
        }
        return context.getResources().getString(R.string.never);
    }

    private int getRemainingDays(long repeatDate) {
        Calendar todayDate = getTodayDate();
        Calendar deadlineDate = getCalendarWithValue(repeatDate);
        return todayDate.get(Calendar.DAY_OF_YEAR) - deadlineDate.get(Calendar.DAY_OF_YEAR);
    }

    private void executePay() {
        boolean isExecute = !this.coming.isExecute();

        this.coming.setExecute(!this.coming.isExecute());
        this.coming.setExecutedDate(getTodayDate().getTimeInMillis());
        updateComingInDatabase();

        int comingId = this.coming.getComingId();
        History newHistoryElement = new History(0, comingId,
                this.transaction.getTransactionId(),
                getTodayDate().getTimeInMillis());

        if (isExecute) {
            historyViewModel.insert(newHistoryElement);
        } else {
            historyViewModel.deleteByComingId(comingId);
        }

        bottomSheetDialog.cancel();
    }

    private Calendar getTodayDate() {
        return Calendar.getInstance();
    }

    private void deleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    removeFromDatabase();
                    bottomSheetDialog.cancel();
                    Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> {}).show();
    }

    private void setRemainingElements(int text, int color) {
        setColor(color);
        setRemainingDaysLabelField(text);
    }

    private void setColor(int color) {
        executedDate.setTextColor(context.getColor(color));
        deadlineDateField.setTextColor(context.getColor(color));
        remainingDaysLabelField.setTextColor(context.getColor(color));
        remainingDaysField.setTextColor(context.getColor(color));
        daysLabelField.setTextColor(context.getColor(color));
    }

    private void setRemainingDaysLabelField(int text) {
        remainingDaysLabelField.setText(text);
    }

    private void setExecuteButtonProperties(int text, int drawable) {
        setExecuteButtonText(text);
        setExecuteIcon(drawable);
    }
    
    private void setExecuteButtonText(int text) {
        executeLabel.setText(text);
    }

    private void setExecuteIcon(int drawable) {
        executeIcon.setImageResource(drawable);
    }

    private void removeFromDatabase() {
        comingViewModel.delete(this.coming);
    }

    private void updateComingInDatabase() {
        comingViewModel.update(this.coming);
    }

    public void show() {
        bottomSheetDialog.show();
    }
}
