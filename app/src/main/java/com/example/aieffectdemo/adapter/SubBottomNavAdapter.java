package com.example.aieffectdemo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aieffectdemo.databinding.ItemNavBinding;

import java.util.ArrayList;
import java.util.List;

public class SubBottomNavAdapter extends RecyclerView.Adapter<SubBottomNavAdapter.ViewHolder> {

    private OnItemListener onItemListener;
    private final ArrayList<String> dataList = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemNavBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemNavBinding binding;

        public ViewHolder(ItemNavBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            binding.clEffect.setVisibility(View.VISIBLE);
            binding.tvEffect.setText(dataList.get(position));
            binding.clEffect.setOnClickListener(v -> {
                onItemListener.onSubItem(dataList.get(position));
            });
        }
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onSubItem(String itemName);
    }
}
