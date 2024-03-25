package com.hc.support.mvi2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModelV2<S: UIState>: ViewModel() {
    private val stateFlow = MutableStateFlow<S?>(null)

    @JvmField
    var actionBus: ActionBus? = null

    fun updateUIState(reducer: S?.() -> S) {
        stateFlow.update {
            reducer.invoke(stateFlow.value)
        }
    }

    fun observeState(): Flow<S?> = stateFlow
}