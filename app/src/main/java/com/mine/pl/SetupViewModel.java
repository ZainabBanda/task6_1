package com.mine.pl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class SetupViewModel extends AndroidViewModel {
    public final MutableLiveData<List<Interest>> interests = new MutableLiveData<>();
    public final MutableLiveData<Boolean> accountCreated = new MutableLiveData<>();

    public SetupViewModel(@NonNull Application app) {
        super(app);
    }

    public void loadInterests() {
        try (InputStreamReader is = new InputStreamReader(
            getApplication().getAssets().open("initial_interests.json"))) {
            Type listType = new TypeToken<List<Interest>>() {}.getType();
            List<Interest> list = new Gson().fromJson(is, listType);
            interests.postValue(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stub: Simulate account creation success/failure
    public void createAccount(String username, String email, String pass, String phone) {
        if (!username.isEmpty() && !email.isEmpty()) {
            accountCreated.postValue(true);  // Simulated success
        } else {
            accountCreated.postValue(false);
        }
    }
}
