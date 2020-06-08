package com.mukhtarinc.thescoop.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mukhtarinc.thescoop.R;
import com.mukhtarinc.thescoop.databinding.CategoryItemBinding;
import com.mukhtarinc.thescoop.databinding.TopHeadlineBinding;
import com.mukhtarinc.thescoop.model.Category;

import java.util.List;


/**
 * Created by Raiyan Mukhtar on 6/3/2020.
 **/
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {


    List<Category>categories;

    RequestManager requestManager;


    public CategoryListAdapter(RequestManager requestManager){

        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        CategoryItemBinding binding = DataBindingUtil.inflate(inflater,R.layout.category_item,parent,false);

        return new CategoryViewHolder(binding.getRoot());

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        CategoryItemBinding binding = DataBindingUtil.getBinding(holder.itemView);
        Category category = categories.get(position);

        if (binding != null) {
            binding.setCategory(category);
            //binding.catIg.setImageResource(category.getCat_image());
            requestManager.load(category.getCat_image()).placeholder(R.drawable.image_placeholder).centerCrop().into(binding.catIg);

        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setData(List<Category> data){

        categories = data;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}