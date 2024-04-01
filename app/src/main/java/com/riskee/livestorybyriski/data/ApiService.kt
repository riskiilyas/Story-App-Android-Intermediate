package com.riskee.livestorybyriski.data

import com.riskee.livestorybyriski.data.request.LoginRequest
import com.riskee.livestorybyriski.data.request.RegisterRequest
import com.riskee.livestorybyriski.data.response.BaseResponse
import com.riskee.livestorybyriski.data.response.ListStoryResponse
import com.riskee.livestorybyriski.data.response.LoginResponse
import com.riskee.livestorybyriski.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {


    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): BaseResponse

    @Multipart
    @POST("stories/guest")
    suspend fun addNewStoryGuest(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): BaseResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): ListStoryResponse

    @GET("stories")
    suspend fun getMapStories(
        @Header("Authorization") authorization: String,
        @Query("location") location: Int = 1
    ): ListStoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): StoryResponse

}