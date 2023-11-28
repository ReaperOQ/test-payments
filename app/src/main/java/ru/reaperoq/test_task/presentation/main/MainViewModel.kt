package ru.reaperoq.test_task.presentation.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.presentation.BaseViewModel
import ru.reaperoq.test_task.presentation.main.models.MainEvent
import ru.reaperoq.test_task.presentation.main.models.MainViewState
import ru.reaperoq.test_task.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel<MainViewState, MainEvent>(
    initialState = MainViewState()
) {

    override fun obtainEvent(event: MainEvent) {
        when (event) {
            MainEvent.Logout -> {
                repository.logout()
                viewState = viewState.copy(logout = true)
            }

            MainEvent.GetPayments -> {
                update()
            }
        }
    }

    private fun update() {
        if (!viewState.isLoading) {
            viewState = viewState.copy(
                isLoading = true
            )
            viewModelScope.launch {
                repository.getPayments()
                    .onSuccess { payments ->
                        viewState = viewState.copy(
                            isLoading = false,
                            error = null,
                            payments = payments
                        )
                    }
                    .onFailure { error ->
                        viewState = viewState.copy(
                            isLoading = false,
                            error = error.message?.let { message ->
                                MainDataSource.ErrorCodes.valueOf(
                                    message
                                )
                            }
                        )
                    }
            }
        }
    }
}