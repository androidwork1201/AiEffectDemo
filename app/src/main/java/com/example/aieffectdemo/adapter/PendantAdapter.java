package com.example.aieffectdemo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aieffectdemo.R;
import com.example.aieffectdemo.databinding.ItemNavBinding;
import com.example.aieffectdemo.databinding.ItemPendantBinding;

import java.util.ArrayList;
import java.util.List;

public class PendantAdapter extends RecyclerView.Adapter<PendantAdapter.ViewHolder>{

    private PendantAdapter.OnItemListener onItemListener;
    private final ArrayList<String> dataList = new ArrayList<>();



    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PendantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PendantAdapter.ViewHolder(ItemPendantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendantAdapter.ViewHolder holder, int position) {
        holder.onBind(position);

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPendantBinding binding;

        public ViewHolder(ItemPendantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {

            if (dataList.get(position).contains("背景")) {
                binding.ivPendant.setImageResource(R.drawable.baseline_image_24);

            } else {
                if (position == dataList.size() - 1) {
                    binding.ivPendant.setImageResource(R.drawable.baseline_cancel_24);
                } else {
                    binding.ivPendant.setImageResource(R.drawable.baseline_tag_faces_24);
                }
            }

            binding.tvPendant.setText(dataList.get(position));
            binding.llPendant.setOnClickListener(v -> onItemListener.onPendant(position, dataList.get(position)));
        }
    }

    public void setOnItemListener(PendantAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onPendant(int position, String itemName);
    }
}
