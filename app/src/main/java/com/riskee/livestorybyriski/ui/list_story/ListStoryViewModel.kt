package com.riskee.livestorybyriski.ui.list_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.response.Story
import com.riskee.livestorybyriski.utils.SharedPrefManager

class ListStoryViewModel(
    repository: AppRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {
    val getAllStories: LiveData<PagingData<Story>> =
        repository.getAllStories().cachedIn(viewModelScope)

    fun logout() {
        sharedPrefManager.token = null
    }

    fun checkLoggedIn() = sharedPrefManager.token != null
}
