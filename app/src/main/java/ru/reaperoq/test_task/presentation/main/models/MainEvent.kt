package ru.reaperoq.test_task.presentation.main.models

sealed class MainEvent {
    data object GetPayments : MainEvent()
    data object Logout : MainEvent()
}