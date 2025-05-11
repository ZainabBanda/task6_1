package com.mine.pl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.mine.pl.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding b;
    private TaskAdapter adapter;
    private List<Task> tasks;
    private List<String> interests;
    private String username;

    private final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) USERNAME: same as before
        String argName = HomeFragmentArgs.fromBundle(getArguments()).getUsername();
        if (argName == null || argName.isEmpty()) {
            username = requireActivity()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("username", "");
        } else {
            username = argName;
        }

        // 2) INTERESTS: ALWAYS load from prefs
        SharedPreferences prefs = requireActivity()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        Set<String> savedSet = prefs.getStringSet("interests_set", null);
        if (savedSet != null && !savedSet.isEmpty()) {
            interests = new ArrayList<>(savedSet);
        } else {
            // First run: if Safe-Args gave you some, persist them and use them
            String[] argsInterests = HomeFragmentArgs.fromBundle(getArguments()).getInterests();
            interests = new ArrayList<>();
            for (String s : argsInterests) interests.add(s);

            if (!interests.isEmpty()) {
                prefs.edit()
                    .putStringSet("interests_set", Set.copyOf(interests))
                    .apply();
            }
        }

        // 3) Update greeting + subtext
        b.tvGreeting.setText("Hello, " + username + "!");
        tasks = generateTasksFromTopics();
        b.tvSubtext.setText(
            "You have " + tasks.size() +
                " task" + (tasks.size() == 1 ? "" : "s") + " due"
        );

        // 4) RecyclerView setup
        adapter = new TaskAdapter(task -> fetchAndNavigate(task.getId()));
        b.rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.rvTasks.setAdapter(adapter);
        adapter.submitList(tasks);

        // 5) Logout: simply navigate back
        b.ivAvatar.setOnClickListener(v ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_home_to_login)
        );
    }

    private List<Task> generateTasksFromTopics() {
        List<Task> generated = new ArrayList<>();
        for (String topic : interests) {
            String id = topic.toLowerCase().replace(" ", "_");
            generated.add(new Task(
                id,
                topic,
                "Answer a few questions on " + topic,
                false,
                new ArrayList<>()
            ));
        }
        return generated;
    }

    private void fetchAndNavigate(String taskId) {
        String url = "http://10.0.2.2:5000/getQuiz?topic=" + taskId;
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("QUIZ", "API failure", e);
            }

            @Override public void onResponse(@NonNull Call call,
                                             @NonNull Response response)
                throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("QUIZ", "API error: " + response.code());
                    return;
                }
                String json = response.body().string();
                QuizResponse qr = new Gson().fromJson(json, QuizResponse.class);

                requireActivity().runOnUiThread(() -> {
                    if (NavHostFragment.findNavController(HomeFragment.this)
                        .getCurrentDestination().getId() == R.id.homeFragment) {

                        HomeFragmentDirections.ActionHomeToTask action =
                            HomeFragmentDirections
                                .actionHomeToTask(
                                    username,
                                    taskId,
                                    interests.toArray(new String[0])
                                );
                        NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(action);
                    }
                });
            }
        });
    }
}
