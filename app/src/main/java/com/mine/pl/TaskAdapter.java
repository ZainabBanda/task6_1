package com.mine.pl;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mine.pl.databinding.ItemTaskBinding;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    public interface OnClick {
        void open(Task task);
    }

    private final OnClick cb;
    private final List<Task> data = new ArrayList<>();

    public TaskAdapter(OnClick clickListener) {
        this.cb = clickListener;
    }

    public void submitList(List<Task> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding b = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Task task = data.get(position);
        holder.b.tvTaskTitle.setText(task.getTitle());
        holder.b.tvTaskDesc.setText(task.getDescription());
        holder.b.switchComplete.setChecked(task.isCompleted());

        holder.b.getRoot().setOnClickListener(v -> {
            v.setEnabled(false);  // Disable to prevent double tap
            cb.open(task);
        });

        holder.b.btnNext.setOnClickListener(v -> {
            v.setEnabled(false);
            cb.open(task);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemTaskBinding b;
        VH(ItemTaskBinding binding) {
            super(binding.getRoot());
            b = binding;
        }
    }
}
