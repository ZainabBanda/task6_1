package com.mine.pl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApi {
    @GET("getQuiz")
    Call<QuizResponse> getQuiz(@Query("topic") String topic);
}
