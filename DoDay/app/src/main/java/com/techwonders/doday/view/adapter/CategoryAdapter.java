package com.techwonders.doday.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techwonders.doday.R;
import com.techwonders.doday.databinding.SingleCategoryItemBinding;
import com.techwonders.doday.listeners.OnCategoryClickListener;
import com.techwonders.doday.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    public boolean isFirstTime = false;

    public CategoryAdapter(Context mContext, OnCategoryClickListener onCategoryClickListener) {
        this.mContext = mContext;
        this.onCategoryClickListener = onCategoryClickListener;
        categoryList = new ArrayList<>();
//        isFirstTime = true;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = getCategory(position);
        holder.binding.tvTitle.setText(category.getTitle());

        if (isFirstTime && position == 0) {
            holder.binding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.binding.cvCategory.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorYellow));
            isFirstTime = false;
        } else {
            if (category.isSelected()) {
                holder.binding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.textColor));
                holder.binding.cvCategory.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorYellow));
            } else {
                holder.binding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.binding.cvCategory.setCardBackgroundColor(mContext.getResources().getColor(R.color.subTextColor));
            }
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            if (onCategoryClickListener != null)
                onCategoryClickListener.onCategoryClick(position);
        });

        holder.binding.getRoot().setOnLongClickListener(v -> {
            if (onCategoryClickListener != null)
                onCategoryClickListener.onCategoryLongClick(position);

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateCategoryList(final List<Category> newCategoryList) {
        isFirstTime = true;
        this.categoryList.clear();
        this.categoryList = newCategoryList;
        notifyDataSetChanged();
    }

    public void updateSelection(int pos) {
        for (Category ca : categoryList) {
            ca.setSelected(false);
        }
        this.categoryList.get(pos).setSelected(true);
        notifyDataSetChanged();
    }

    public void updateSelectionAt(int id) {
        for (Category ca : categoryList) {
            if (ca.getId() == id) {
                ca.setSelected(true);
            }
        }
        notifyDataSetChanged();
    }

    public Category getCategory(int pos) {
        return categoryList.get(pos);
    }

    public List<Category> getAllCategory() {
        return categoryList;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        SingleCategoryItemBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleCategoryItemBinding.bind(itemView);
        }
    }

}
