package com.mine.pl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.Chip;
import com.mine.pl.databinding.FragmentInterestsBinding;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class InterestsFragment extends Fragment {
    private FragmentInterestsBinding b;
    private final Set<String> selectedTopics = new LinkedHashSet<>();
    private String username;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentInterestsBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Pull username from Safe-Args
        username = InterestsFragmentArgs
            .fromBundle(getArguments())
            .getUsername();

        // 2) Build choice chips
        String[] topics = {
            "Algorithms", "Data Structures", "Web Development", "Testing",
            "Machine Learning", "Databases", "System Design", "Networking",
            "Security", "DevOps", "UI/UX", "Mobile", "Cloud", "AI Ethics"
        };
        for (String t : topics) {
            Chip chip = (Chip) getLayoutInflater()
                .inflate(R.layout.widget_choice_chip, b.chipGroup, false);
            chip.setText(t);
            chip.setOnCheckedChangeListener((c, checked) -> {
                if (checked) selectedTopics.add(t);
                else          selectedTopics.remove(t);
                b.btnNext.setEnabled(!selectedTopics.isEmpty());
            });
            b.chipGroup.addView(chip);
        }

        // 3) Disable Next until at least one is chosen
        b.btnNext.setEnabled(false);

        // 4) On Next: save both in prefs and navigate
        b.btnNext.setOnClickListener(v -> {
            // persist the interests set
            SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit()
                .putStringSet("interests_set", selectedTopics)
                .apply();

            // pass the array via Safe-Args
            String[] arr = selectedTopics.toArray(new String[0]);
            InterestsFragmentDirections.ActionInterestsToHome action =
                InterestsFragmentDirections
                    .actionInterestsToHome(username, arr);

            NavHostFragment.findNavController(this).navigate(action);
        });
    }
}
