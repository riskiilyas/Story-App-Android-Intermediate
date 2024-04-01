package com.riskee.livestorybyriski.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.response.Story
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: AppRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _storyState = MutableStateFlow<Resource<List<Story>>>(Resource.INIT())
    val storyState: StateFlow<Resource<List<Story>>> = _storyState

    fun getMapStories() {
        viewModelScope.launch {
            repository.getMapStories(authorization = "Bearer ${sharedPrefManager.token}")
                .collect { result -> _storyState.value = result }
        }
    }
}