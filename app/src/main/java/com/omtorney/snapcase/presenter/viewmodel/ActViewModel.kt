package com.omtorney.snapcase.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.domain.usecase.ShowActTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActViewModel @Inject constructor(
    private val showActTextUseCase: ShowActTextUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _actText = MutableStateFlow(arrayListOf<String>())
    val actText = _actText.asStateFlow()

    fun loadActText(url: String) = viewModelScope.launch {
        _isLoading.value = true
        _actText.value = showActTextUseCase.execute(Courts.Dmitrov, url)
        _isLoading.value = false
    }

}