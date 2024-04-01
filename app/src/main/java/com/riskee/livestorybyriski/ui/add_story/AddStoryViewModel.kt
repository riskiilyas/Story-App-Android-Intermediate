package com.riskee.livestorybyriski.ui.add_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.response.BaseResponse
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryViewModel(
    private val repository: AppRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _addStoryState = MutableStateFlow<Resource<BaseResponse>>(Resource.INIT())
    val addStoryState: StateFlow<Resource<BaseResponse>> = _addStoryState

    fun addNewStory(
        description: String,
        photo: File,
        lat: String? = null,
        lon: String? = null
    ) {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        viewModelScope.launch {
            repository.addNewStory(
                "Bearer ${sharedPrefManager.token}",
                descriptionBody,
                multipartBody,
                lat?.toRequestBody(MultipartBody.FORM),
                lon?.toRequestBody(MultipartBody.FORM)
            ).collect { result -> _addStoryState.value = result }
        }
    }

    fun addNewStoryGuest(
        description: String,
        photo: File,
        lat: String? = null,
        lon: String? = null
    ) {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        viewModelScope.launch {
            repository.addNewStoryGuest(
                descriptionBody,
                multipartBody,
                lat?.toRequestBody(MultipartBody.FORM),
                lon?.toRequestBody(MultipartBody.FORM)
            ).collect { result -> _addStoryState.value = result }
        }
    }
}