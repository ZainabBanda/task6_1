// InterestsViewModel.java
package com.mine.pl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class InterestsViewModel extends AndroidViewModel {
    public final MutableLiveData<List<Interest>> selected = new MutableLiveData<>();

    public InterestsViewModel(@NonNull Application app){ super(app); }

    public void submitSelection(List<Interest> choices) {
        selected.postValue(choices);
    }
}
