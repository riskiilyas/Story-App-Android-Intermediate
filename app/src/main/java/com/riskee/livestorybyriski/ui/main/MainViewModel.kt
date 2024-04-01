package com.riskee.livestorybyriski.ui.main

import androidx.lifecycle.ViewModel
import com.riskee.livestorybyriski.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel(
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> = _loginState

    fun checkLoggedIn() {
        _loginState.value = sharedPrefManager.token != null
    }
}