package com.riskee.livestorybyriski.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.riskee.livestorybyriski.data.request.LoginRequest
import com.riskee.livestorybyriski.data.request.RegisterRequest
import com.riskee.livestorybyriski.data.response.BaseResponse
import com.riskee.livestorybyriski.data.response.LoginResponse
import com.riskee.livestorybyriski.data.response.Story
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.SharedPrefManager
import com.riskee.livestorybyriski.utils.StoryRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(
    private val apiService: APIService,
    private val sharedPrefManager: SharedPrefManager,
    private val db: AppDb
) {

    fun register(request: RegisterRequest): Flow<Resource<BaseResponse>> = flow {
        emit(Resource.LOADING())
        try {
            val response = apiService.register(request)
            emit(Resource.SUCCESS(response))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun login(request: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.LOADING())
        try {
            val response = apiService.login(request)
            emit(Resource.SUCCESS(response))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun addNewStory(
        authorization: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<BaseResponse>> = flow {
        emit(Resource.LOADING())
        try {
            val response =
                apiService.addNewStory(authorization, description, photo, lat, lon)
            emit(Resource.SUCCESS(response))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun addNewStoryGuest(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<BaseResponse>> = flow {
        emit(Resource.LOADING())
        try {
            val response = apiService.addNewStoryGuest(description, photo, lat, lon)
            emit(Resource.SUCCESS(response))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(
    ): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(db, apiService, sharedPrefManager),
            pagingSourceFactory = {
//                StoryPagingSource(apiService, sharedPrefManager)
                db.storyDao().getAllQuote()
            }
        ).liveData
    }

    fun getStoryDetail(authorization: String, id: String): Flow<Resource<Story>> = flow {
        emit(Resource.LOADING())
        try {
            val response = apiService.getStoryDetail(authorization, id)
            emit(Resource.SUCCESS(response.story))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getMapStories(authorization: String): Flow<Resource<List<Story>>> = flow {
        emit(Resource.LOADING())
        try {
            val response = apiService.getMapStories(authorization)
            emit(Resource.SUCCESS(response.listStory))
        } catch (e: Exception) {
            emit(Resource.ERROR(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)
}
