package com.example.budgetmanagement.ui.category;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetmanagement.R;
import com.example.budgetmanagement.database.rooms.Category;
import com.example.budgetmanagement.database.viewmodels.CategoryViewModel;
import com.example.budgetmanagement.databinding.CategoryElementDetailsBinding;
import com.example.budgetmanagement.ui.utils.AppIconPack;
import com.example.budgetmanagement.ui.utils.DateProcessor;
import com.maltaisn.icondialog.pack.IconPack;

import java.math.BigDecimal;
import java.util.Objects;

public class CategoryElementDetails extends Fragment {

    CategoryElementDetailsBinding binding;
    public static final String CATEGORY_ID_ARG = "categoryId";

    public static CategoryElementDetails newInstance(int categoryId) {
        CategoryElementDetails fragment = new CategoryElementDetails();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID_ARG, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CategoryElementDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int categoryId = getArguments() != null ? getArguments().getInt(CATEGORY_ID_ARG, -1) : -1;

        if (categoryId == -1) {
            Toast.makeText(requireContext(), R.string.not_found_id, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        Category category = categoryViewModel.getCategoryById(categoryId);
        setFields(category);
    }

    private void setFields(Category category) {
        binding.categoryName.setText(category.getName());

        Drawable amountIcon = getAmountIconDependOfValue(category.getBudget());
        binding.budget.setText(category.getBudget());
        binding.budget.setCompoundDrawablesWithIntrinsicBounds(amountIcon, null, null, null);

        Drawable icon = getCategoryIcon(category);
        binding.icon.setImageDrawable(icon);

        binding.addDate.setText(DateProcessor.parseDate(category.getAddDate()));
    }

    private Drawable getAmountIconDependOfValue(String value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        if (isNegative(bigDecimal)) {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_down_24, R.color.mat_red);
        } else {
            return getDrawableWithColor(R.drawable.ic_baseline_arrow_drop_up_24, R.color.mat_green);
        }
    }

    private Drawable getDrawableWithColor(int drawableResId, int colorResId) {
        Drawable drawable = ResourcesCompat.getDrawable(requireContext().getResources(), drawableResId, null);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(requireContext(), colorResId), PorterDuff.Mode.SRC_IN));
        } else {
            drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_outline_icon_not_found_24, null);
        }
        return drawable;
    }


    private boolean isNegative(BigDecimal bigDecimal) {
        return bigDecimal.signum() == -1;
    }

    private Drawable getCategoryIcon(Category category) {
        IconPack iconPack = ((AppIconPack) requireActivity().getApplication()).getIconPack();
        assert iconPack != null;
        return Objects.requireNonNull(iconPack.getIcon(category.getIcon())).getDrawable();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}