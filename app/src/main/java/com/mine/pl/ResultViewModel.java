// ResultViewModel.java
package com.mine.pl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ResultViewModel extends AndroidViewModel {
    private final TaskRepository repo = new TaskRepository();
    public final LiveData<Result> result = repo.lastResult;

    public ResultViewModel(@NonNull Application app){ super(app); }
}
