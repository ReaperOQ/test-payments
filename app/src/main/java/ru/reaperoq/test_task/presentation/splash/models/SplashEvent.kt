package ru.reaperoq.test_task.presentation.splash.models

sealed class SplashEvent {
    data object CheckAuth : SplashEvent()
}