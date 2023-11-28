package ru.reaperoq.test_task.presentation.login.models

import ru.reaperoq.test_task.datasource.MainDataSource

data class LoginViewState(
    val isLoading: Boolean = false,
    val isPasswordError: Boolean = false,
    val isLoginError: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val error: MainDataSource.ErrorCodes? = null,
)
