// UserRepository.java
package com.mine.pl;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final AuthService api = ApiClient.get().create(AuthService.class);
    public final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public void login(String u, String p){
        api.login(new AuthService.LoginRequest(u,p))
            .enqueue(new Callback<User>() {
                @Override public void onResponse(Call<User> c, Response<User> r){
                    currentUser.postValue(r.isSuccessful() ? r.body() : null);
                }
                @Override public void onFailure(Call<User> c, Throwable t){
                    currentUser.postValue(null);
                }
            });
    }
}
