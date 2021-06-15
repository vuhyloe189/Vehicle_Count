package com.example.authentication.Retrofit;

import java.sql.SQLInvalidAuthorizationSpecException;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

import com.example.authentication.Retrofit.*;
import com.example.authentication.Models.*;
public interface MyService {

    //Login Actitivity
    @POST("authenticate/login")
    @FormUrlEncoded
    Observable<Response<String>> basicLogin(@Field("email") String name,
                                            @Field("password") String email);

    //Register Activity
    @POST("authenticate/register")
    @FormUrlEncoded
    Observable<Response<String>> registerUser(@Field("name") String name,
                                              @Field("email") String email,
                                              @Field("password") String password);

    @POST("authenticate/logout")
    Observable<Response<String>> basicLogout();

    @POST
    @FormUrlEncoded
    Observable<Response<String>>  getProfile(@Url String urlGet,
                                   @Field("token") String token);

    @GET("cars/get")
    Observable<String>  getInformation();

    @POST("cars/post")
    @FormUrlEncoded
    Observable<Response<String>>  postInformation(@Field("volumes") String cars );

    @PUT
    @FormUrlEncoded
    Observable<Response<String>>  changePassword(@Url String urlGet,
                                                 @Field("token") String token,
                                                 @Field("password") String  oldPassword,
                                                 @Field( "newPassword" ) String newPassword);

}
