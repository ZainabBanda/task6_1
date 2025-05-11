// LoginViewModel.java
package com.mine.pl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {
    private final UserRepository repo = new UserRepository();
    public final MutableLiveData<User> loggedIn = repo.currentUser;

    public LoginViewModel(@NonNull Application app){ super(app); }

    public void login(String u, String p){ repo.login(u,p); }
}
