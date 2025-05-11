// TaskViewModel.java
package com.mine.pl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repo = new TaskRepository();
    public final MutableLiveData<Result> lastResult = repo.lastResult;

    public TaskViewModel(@NonNull Application app){ super(app); }

    public void submit(String token, String taskId, List<String> answers) {
        repo.submit(token, taskId, answers);
    }
}
