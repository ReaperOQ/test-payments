package ru.reaperoq.test_task.presentation.main.models

import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.datasource.models.Payment

data class MainViewState(
    val isLoading: Boolean = false,
    val error: MainDataSource.ErrorCodes? = null,
    val payments: List<Payment> = emptyList(),
    val logout: Boolean = false
)
