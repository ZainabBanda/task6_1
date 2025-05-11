package com.mine.pl;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final TaskRepository repo = new TaskRepository();
    public final MutableLiveData<List<Task>> tasks = repo.tasks;

    public HomeViewModel(@NonNull Application app) {
        super(app);
    }

    public void refresh(Context ctx, String token, List<String> interests) {
        repo.fetchTasks(ctx, token, interests);
    }
}
