// AuthService.java
package com.mine.pl;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<User> login(@Body LoginRequest req);

    class LoginRequest {
        public String username, password;
        public LoginRequest(String u, String p){ username=u; password=p;}
    }
}
