package ru.reaperoq.test_task.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.presentation.BaseViewModel
import ru.reaperoq.test_task.presentation.login.models.LoginEvent
import ru.reaperoq.test_task.presentation.login.models.LoginViewState
import ru.reaperoq.test_task.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel<LoginViewState, LoginEvent>(
    initialState = LoginViewState()
) {
    companion object {
        private const val TAG = "LoginViewModel"
    }
    override fun obtainEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ClickLogin -> {
                viewState = viewState.copy(
                    isLoginSuccess = false,
                    error = null
                )
                if (event.login.isEmpty() || event.password.isEmpty()) {
                    viewState = viewState.copy(
                        isLoginError = event.login.isEmpty(),
                        isPasswordError = event.password.isEmpty()
                    )
                } else {
                    viewState = viewState.copy(
                        isLoginError = false,
                        isPasswordError = false,
                        isLoading = true
                    )
                    viewModelScope.launch {
                        val result = repository.login(event.login, event.password)
                        result
                            .onSuccess {
                                Log.d(TAG, "obtainEvent: $it")
                                viewState = viewState.copy(
                                    isLoading = false,
                                    isLoginSuccess = true
                                )
                            }
                            .onFailure {
                                Log.d(TAG, "obtainEvent: ${it.message}")
                                viewState = viewState.copy(
                                    isLoading = false,
                                    error = it.message?.let { message ->
                                        MainDataSource.ErrorCodes.valueOf(message)
                                    }
                                )
                            }
                    }
                }
            }
        }
    }
}