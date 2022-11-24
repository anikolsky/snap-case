package com.omtorney.snapcase.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.local.CaseDao
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.DeleteCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val caseDao: CaseDao,
    private val deleteCaseUseCase: DeleteCaseUseCase
) : ViewModel() {

    val allCases = this.caseDao.getAll()
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun deleteCase(case: Case) {
        deleteCaseUseCase.execute(case)
    }
}