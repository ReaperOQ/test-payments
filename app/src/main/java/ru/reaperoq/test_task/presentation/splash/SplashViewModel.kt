package ru.reaperoq.test_task.presentation.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.reaperoq.test_task.presentation.BaseViewModel
import ru.reaperoq.test_task.presentation.splash.models.SplashEvent
import ru.reaperoq.test_task.presentation.splash.models.SplashViewState
import ru.reaperoq.test_task.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel<SplashViewState, SplashEvent>(
    initialState = SplashViewState()
) {
    override fun obtainEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.CheckAuth -> {
                viewState = if (repository.isLoggedIn()) {
                    SplashViewState(isLoggedIn = true)
                } else {
                    SplashViewState(isLoggedIn = false)
                }
            }
        }
    }
}