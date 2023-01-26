package com.example.budgetmanagement.database.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagement.R;

public class BottomColorPickerAdapter extends RecyclerView.Adapter<BottomColorPickerAdapter.BottomColorPickerHolder> {

    private final OnSelectedListener listener;
    private final Context context;
    private final int colorToSetChecked;
    private final int[] colors;
    private CheckBox selectedColor;
    public CheckBox[] colorCheckBoxes;

    public BottomColorPickerAdapter(OnSelectedListener listener, Context context, int colorToSetChecked) {
        this.listener = listener;
        this.context = context;
        this.colorToSetChecked = colorToSetChecked;
        colors = context.getResources().getIntArray(R.array.colors);
        colorCheckBoxes = new CheckBox[colors.length];
    }

    @NonNull
    @Override
    public BottomColorPickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_picker_item, parent, false);
        return new BottomColorPickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomColorPickerHolder holder, int position) {
        boolean setChecked = position == colorToSetChecked;
        holder.bind(position, setChecked);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    class BottomColorPickerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox color;
        private final CheckBox colorStroke;

        public BottomColorPickerHolder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            colorStroke = itemView.findViewById(R.id.colorStroke);
            itemView.setOnClickListener(this);
            color.setOnClickListener(this);
        }

        public void bind(int position, boolean setChecked) {
            colorCheckBoxes[position] = colorStroke;

            int colorRes = colors[position];
            Drawable ovalWithColorBackground =
                    getDrawableWithColor(R.drawable.background_oval, colorRes);

            if (ovalWithColorBackground != null) {
                color.setBackground(ovalWithColorBackground);
            }

            if (setChecked) {
                colorStroke.setChecked(true);
                selectedColor = colorStroke;
            }
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                CheckBox newSelectedColor = (CheckBox) v;
                if (newSelectedColor != selectedColor) {
                    selectedColor.setChecked(false);
                    colorStroke.setChecked(true);
                    selectedColor = colorStroke;
                } else {
                    selectedColor.setChecked(true);
                }
                listener.onContentSelected(getBindingAdapterPosition());
            }
        }
    }

    private Drawable getDrawableWithColor(int drawableId, int color) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableId, null);

        if (drawable != null) {
            Drawable drawableWrapped = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawableWrapped, color);
            return drawableWrapped;
        }

        return null;
    }

    public interface OnSelectedListener {
        void onContentSelected(int position);
    }
}