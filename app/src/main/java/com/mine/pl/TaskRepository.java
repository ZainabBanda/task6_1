// TaskRepository.java
package com.mine.pl;

import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {
    private final TaskService api = ApiClient.get().create(TaskService.class);
    public final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    public final MutableLiveData<Result>     lastResult = new MutableLiveData<>();

    // If useRemote=false, load from assets
    public void fetchTasks(Context ctx, String token, List<String> interests) {
        boolean useRemote = false;
        if (useRemote) {
            api.generate(new TaskService.GenerateReq(token, interests))
                .enqueue(new Callback<List<Task>>() {
                    @Override public void onResponse(Call<List<Task>> c, Response<List<Task>> r){
                        if (r.isSuccessful()) tasks.postValue(r.body());
                    }
                    @Override public void onFailure(Call<List<Task>> c, Throwable t){}
                });
        } else {
            try (InputStreamReader is = new InputStreamReader(
                ctx.getAssets().open("initial_tasks.json"))) {
                Type listType = new TypeToken<List<Task>>(){}.getType();
                List<Task> list = new Gson().fromJson(is, listType);
                tasks.postValue(list);
            } catch (Exception e){ e.printStackTrace(); }
        }
    }

    public void submit(String token, String taskId, List<String> answers) {
        api.submit(new TaskService.SubmitReq(token, taskId, answers))
            .enqueue(new Callback<Result>() {
                @Override public void onResponse(Call<Result> c, Response<Result> r){
                    if (r.isSuccessful()) lastResult.postValue(r.body());
                }
                @Override public void onFailure(Call<Result> c, Throwable t){}
            });
    }
}
