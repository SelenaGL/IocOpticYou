package com.example.opticyou.ui

import androidx.lifecycle.ViewModel
import com.example.opticyou.data.IOUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 *  Holds and updates if the i/o operation has been successful.
 *  This data can be retrieved from the screen.
 *  This class would be a superclass of classes i/o related.
 */
open class IOViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IOUiState())
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