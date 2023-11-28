package ru.reaperoq.test_task.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State, Event>(
    initialState: State
) : ViewModel() {
    private val _viewState = MutableLiveData(initialState)

    protected var viewState: State
        get() = _viewState.value!!
        set(value) {
            _viewState.value = value
        }

    abstract fun obtainEvent(event: Event)

    fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        _viewState.observe(owner, observer)
    }
}