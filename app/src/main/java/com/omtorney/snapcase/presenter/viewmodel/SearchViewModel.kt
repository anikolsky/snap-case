package com.omtorney.snapcase.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.SearchCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCaseUseCase: SearchCaseUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _search = MutableStateFlow(listOf<Case>())
    val search = _search.asStateFlow()

    fun searchCases(query: String) = viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            _isLoading.value = true
            searchCaseUseCase.execute(Courts.Dmitrov, query)
        }.fold(
            onSuccess = { _search.value = it },
            onFailure = { Log.d("SearchViewModel", it.message ?: "") }
        )
        _isLoading.value = false
    }
}