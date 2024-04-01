package com.riskee.livestorybyriski.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.request.RegisterRequest
import com.riskee.livestorybyriski.data.response.BaseResponse
import com.riskee.livestorybyriski.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AppRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<BaseResponse>>(Resource.INIT())
    val registerState: StateFlow<Resource<BaseResponse>> = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(RegisterRequest(name, email, password))
                .collect { result -> _registerState.value = result }
        }
    }
}