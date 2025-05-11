package com.mine.pl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mine.pl.databinding.FragmentTaskBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.navigation.fragment.NavHostFragment;

public class TaskFragment extends Fragment {
    private FragmentTaskBinding b;
    private QuestionAdapter adapter;
    private String username, taskId;
    private String[] interests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTaskBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TaskFragmentArgs args = TaskFragmentArgs.fromBundle(getArguments());
        username  = args.getUsername();
        taskId    = args.getTaskId();
        interests = args.getInterests();

        adapter = new QuestionAdapter();
        b.rvQuestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.rvQuestions.setAdapter(adapter);

        fetchQuiz(taskId);

        b.btnSubmit.setOnClickListener(x -> submitAnswers());
    }

    private void fetchQuiz(String topic) {
        QuizApiService api = ApiClient.get().create(QuizApiService.class);
        api.getQuiz(topic).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call,
                                   Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> quizList = new ArrayList<>(response.body().getQuiz());

                    // —— FIX: convert letter to actual text ——
                    for (Question q : quizList) {
                        String letter = q.getCorrectAnswer();    // e.g. "B"
                        if (letter != null && letter.length() == 1) {
                            int idx = letter.charAt(0) - 'A';   // A→0, B→1, …
                            List<String> opts = q.getOptions();
                            if (idx >= 0 && idx < opts.size()) {
                                q.setCorrectAnswer(opts.get(idx));
                            }
                        }
                    }
                    // ————————————————————————————————

                    // now your adapter and scoring logic will compare full-text ↔ full-text
                    adapter.submitList(quizList);
                } else {
                    Toast.makeText(requireContext(),
                        "Failed to load quiz", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                    "Error fetching quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitAnswers() {
        List<Question> quizData   = adapter.getCurrentList();
        String[]    userAnswers   = adapter.getUserAnswers();

        // 1) Make sure every question is answered
        for (String a : userAnswers) {
            if (a == null || a.isEmpty()) {
                Toast.makeText(getContext(),
                    "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 2) Convert List<Question> → Question[]
        Question[] quizArray = quizData.toArray(new Question[0]);

        // 3) Build and navigate the Safe-Args action
        TaskFragmentDirections.ActionTaskToResult action =
            TaskFragmentDirections.actionTaskToResult(
                username,
                taskId,
                userAnswers,
                quizArray,
                interests
            );

        // 4) Navigate using the correct NavHostFragment API
        NavHostFragment.findNavController(this).navigate(action);
    }

}
