package com.mine.pl;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mine.pl.databinding.ItemResultBinding;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.VH> {
    private final List<Question> data = new ArrayList<>();

    public void submitList(List<Question> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemResultBinding b = ItemResultBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent, false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        Question q = data.get(pos);

        String userAnswer = q.getUserAnswer() != null
            ? q.getUserAnswer().trim()
            : "";
        String correctAnswer = q.getCorrectAnswer() != null
            ? q.getCorrectAnswer().trim()
            : "";

        holder.b.tvQ.setText((pos + 1) + ". " + q.getQuestion());
        holder.b.tvUserAnswer.setText("Your answer: " + userAnswer);

        if (userAnswer.equalsIgnoreCase(correctAnswer) && !userAnswer.isEmpty()) {
            // correct
            holder.b.tvUserAnswer.setTextColor(Color.parseColor("#4CAF50")); // green
            holder.b.tvCorrectAnswer.setVisibility(View.GONE);
        } else {
            // wrong or blank
            holder.b.tvUserAnswer.setTextColor(Color.parseColor("#F44336")); // red

            holder.b.tvCorrectAnswer
                .setText("Correct answer: " + correctAnswer);
            holder.b.tvCorrectAnswer
                .setTextColor(Color.parseColor("#4CAF50")); // green
            holder.b.tvCorrectAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemResultBinding b;
        VH(ItemResultBinding binding) {
            super(binding.getRoot());
            b = binding;
        }
    }
}
