package com.riskee.livestorybyriski.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.response.Story
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: AppRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _storyState = MutableStateFlow<Resource<Story>>(Resource.INIT())
    val storyState: StateFlow<Resource<Story>> = _storyState

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            repository.getStoryDetail(authorization = "Bearer ${sharedPrefManager.token}", id)
                .collect { result -> _storyState.value = result }
        }
    }
}