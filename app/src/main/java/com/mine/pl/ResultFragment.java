// ResultFragment.java
package com.mine.pl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mine.pl.ResultFragmentArgs;
import java.util.Arrays;
import java.util.List;
import androidx.navigation.fragment.NavHostFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mine.pl.databinding.FragmentResultBinding;

import java.util.Arrays;
import java.util.List;

public class ResultFragment extends Fragment {
    private FragmentResultBinding b;
    private ResultAdapter      adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentResultBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Pull the parcelled array out of the nav-args
        Question[] questionArray =
            ResultFragmentArgs.fromBundle(getArguments()).getQuizData();

        // 2) Convert it to a List for easy looping
        List<Question> questionList = Arrays.asList(questionArray);

        // 3) Compute the score
        int score = 0;
        for (Question q : questionList) {
            String userAnswer    = q.getUserAnswer()    != null
                ? q.getUserAnswer().trim()
                : "";
            String correctAnswer = q.getCorrectAnswer() != null
                ? q.getCorrectAnswer().trim()
                : "";

            if (!userAnswer.isEmpty()
                && userAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
            }
        }

        // 4) Display the score
        b.tvScore.setText(
            "You scored " + score + " out of " + questionList.size()
        );

        // 5) Hook up your RecyclerView with the data
        adapter = new ResultAdapter();
        b.rvResults.setLayoutManager(
            new LinearLayoutManager(requireContext())
        );
        b.rvResults.setAdapter(adapter);
        adapter.submitList(questionList);
        // after adapter.submitList(...)
        b.btnContinue.setOnClickListener(v -> {
            // pull in the same args you used to score the quiz
            ResultFragmentArgs args = ResultFragmentArgs.fromBundle(getArguments());
            String username = args.getUsername();
            String[] interests = args.getInterests();

            // navigate back to HomeFragment, passing username + interests
            ResultFragmentDirections.ActionResultToHome action =
                ResultFragmentDirections.actionResultToHome(username, interests);
            NavHostFragment.findNavController(this).navigate(action);
        });

    }

}
