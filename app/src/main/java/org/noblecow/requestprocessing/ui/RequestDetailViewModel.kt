package org.noblecow.requestprocessing.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.noblecow.requestprocessing.R
import org.noblecow.requestprocessing.data.ApprovalException
import org.noblecow.requestprocessing.data.RequestRepository
import org.noblecow.requestprocessing.domain.Request

data class RequestDetailUiState(
    val isLoading: Boolean = false,
    val request: Request? = null,
    @StringRes val successStringRes: Int? = null,
    @StringRes val errorStringRes: Int? = null
)

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val requestRepository: RequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RequestDetailUiState())
    val uiState: StateFlow<RequestDetailUiState> = _uiState.asStateFlow()
    private var currentJob: Job? = null
    private var request: Request? = null

    init {
        viewModelScope.launch {
            val isNew: Boolean = savedStateHandle["newRequest"] ?: false
            request = requestRepository.getRequest(isNew)
            _uiState.emit(RequestDetailUiState(request = request))
        }
    }

    fun approve() {
        request?.let { request ->
            currentJob?.cancel()
            currentJob = viewModelScope.launch {
                try {
                    _uiState.emit(RequestDetailUiState(true, request = request))
                    requestRepository.approveRequest(request)
                    _uiState.emit(
                        RequestDetailUiState(
                            isLoading = false,
                            request = request,
                            successStringRes = R.string.request_approved
                        )
                    )
                } catch (_: ApprovalException) {
                    _uiState.emit(
                        RequestDetailUiState(
                            isLoading = false,
                            request = request,
                            errorStringRes = R.string.approval_error
                        )
                    )
                }
            }
        }
    }

    fun reject() {
        request?.let { request ->
            currentJob?.cancel()
            currentJob = viewModelScope.launch {
                _uiState.emit(RequestDetailUiState(true, request = request))
                if (requestRepository.rejectRequest(request)) {
                    _uiState.emit(
                        RequestDetailUiState(
                            isLoading = false,
                            request = request,
                            errorStringRes = R.string.request_rejected
                        )
                    )
                }
            }
        }
    }
}
