package com.example.opticyou.ui

import androidx.lifecycle.ViewModel
import com.example.opticyou.data.IOUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 *  ViewModel que gestiona l'estat de les operacions d'entrada/sortida (I/O).
 */
open class IOViewModel : ViewModel() {
    /** Estat actual mutable **/
    private val _uiState = MutableStateFlow(IOUiState())
    /** Variable de consulta per a la IU **/
    val uiIOState: StateFlow<IOUiState> = _uiState.asStateFlow()

    fun setGoodResult(goodResult: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                goodResult = goodResult
            )
        }
    }

    fun getGoodResult():Boolean{
        return  uiIOState.value.goodResult;
    }
}