package com.apps.poultryapp.Login.Login.Interfaces;

import com.apps.poultryapp.Login.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ClienteRetrofit {
    @FormUrlEncoded
    @POST("/api/login")
    Call<User> Login(@Field("email")String email,
                     @Field("password")String password);
}
