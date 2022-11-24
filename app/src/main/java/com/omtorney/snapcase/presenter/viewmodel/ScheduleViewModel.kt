package com.omtorney.snapcase.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.ShowScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val showScheduleUseCase: ShowScheduleUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _schedule = MutableStateFlow(listOf<Case>())
    val schedule = _schedule.asStateFlow()

    fun showSchedule(date: String) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            _isLoading.value = true
            showScheduleUseCase.execute(Courts.Dmitrov, date)
        }.fold(
            onSuccess = { _schedule.value = it },
            onFailure = { Log.d("ScheduleViewModel", it.message ?: "") }
        )
        _isLoading.value = false
    }
}