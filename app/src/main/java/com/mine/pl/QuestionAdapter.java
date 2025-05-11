package com.mine.pl;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mine.pl.databinding.ItemQuestionBinding;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.VH> {
    private final List<Question> questions = new ArrayList<>();
    private final Map<Integer, String> selectedAnswers = new HashMap<>();

    public void submitList(List<Question> list) {
        questions.clear();
        if (list != null) questions.addAll(list);
        selectedAnswers.clear();
        notifyDataSetChanged();
    }

    public List<Question> getCurrentList() {
        return questions;
    }

    public String[] getUserAnswers() {
        String[] answers = new String[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            answers[i] = selectedAnswers.getOrDefault(i, "");
        }
        return answers;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionBinding b = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent, false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        holder.bind(questions.get(pos), pos);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private final ItemQuestionBinding b;

        VH(ItemQuestionBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(Question q, int pos) {
            b.tvStem.setText((pos + 1) + ". " + q.getQuestion());
            b.radioGroup.removeAllViews();

            List<String> options = q.getOptions();
            for (String option : options) {
                RadioButton rb = new RadioButton(itemView.getContext());
                rb.setText(option);
                rb.setId(View.generateViewId());
                rb.setChecked(option.equals(q.getUserAnswer())); // Reflect saved answer
                b.radioGroup.addView(rb);
            }

            b.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selected = group.findViewById(checkedId);
                if (selected != null) {
                    String selectedAnswer = selected.getText().toString();
                    q.setUserAnswer(selectedAnswer); // <-- critical line
                    selectedAnswers.put(pos, selectedAnswer); // also keep in map
                }
            });
        }

    }
}
