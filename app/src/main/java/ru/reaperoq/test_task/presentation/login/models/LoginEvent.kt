package ru.reaperoq.test_task.presentation.login.models

sealed class LoginEvent {
    data class ClickLogin(
        val login: String,
        val password: String
    ) : LoginEvent()
}