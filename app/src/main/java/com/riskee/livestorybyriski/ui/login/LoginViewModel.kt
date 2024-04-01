package com.riskee.livestorybyriski.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.data.request.LoginRequest
import com.riskee.livestorybyriski.data.response.LoginResponse
import com.riskee.livestorybyriski.utils.Resource
import com.riskee.livestorybyriski.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: AppRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.INIT())
    val loginState: StateFlow<Resource<LoginResponse>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(LoginRequest(email, password))
                .collect { result ->
                    if (result is Resource.SUCCESS) {
                        sharedPrefManager.token = result.result.loginResult.token
                    }
                    _loginState.value = result
//                    println("EEEE: TKN "+sharedPrefManager.token)
                }
        }
    }
}