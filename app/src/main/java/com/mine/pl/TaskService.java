// TaskService.java
package com.mine.pl;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TaskService {
    @POST("tasks/generate")
    Call<List<Task>> generate(@Body GenerateReq req);

    @POST("tasks/submit")
    Call<Result> submit(@Body SubmitReq req);

    class GenerateReq {
        public String token;
        public List<String> interests;
        public GenerateReq(String t, List<String> i){ token=t; interests=i; }
    }
    class SubmitReq {
        public String token, taskId;
        public List<String> answers;
        public SubmitReq(String t, String id, List<String> a){
            token=t; taskId=id; answers=a;
        }
    }
}
