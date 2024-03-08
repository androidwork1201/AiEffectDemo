package com.example.aieffectdemo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aieffectdemo.R;
import com.example.aieffectdemo.databinding.ItemNavBinding;

import java.util.ArrayList;
import java.util.List;

public class BottomNavAdapter extends RecyclerView.Adapter<BottomNavAdapter.ViewHolder> {

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
            binding.clBottomNav.setVisibility(View.VISIBLE);
            binding.cvNew.setVisibility(View.VISIBLE);

            if (position == 0) {
                binding.ivOperation.setImageResource(R.drawable.ic_beauty);
            }
            if (position == 1) {
                binding.ivOperation.setImageResource(R.drawable.ic_smooth);
            }
            if (position == 2) {
                binding.ivOperation.setImageResource(R.drawable.ic_makeups);
            }
            if (position == 3) {
                binding.ivOperation.setImageResource(R.drawable.ic_filter);
            }


            binding.tvOperation.setText(dataList.get(position));

            binding.clBottomNav.setOnClickListener(v -> onItemListener.onItem(position));
        }
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onItem(int position);
    }
}
