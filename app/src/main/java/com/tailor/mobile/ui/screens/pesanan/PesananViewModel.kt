package com.tailor.mobile.ui.screens.pesanan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tailor.mobile.data.repository.PesananRepository
import com.tailor.mobile.data.source.remote.response.PesananDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PesananUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val pesananList: List<PesananDto> = emptyList(),
    val searchQuery: String = "",
    val selectedStatus: String? = null, // null = Semua, "aktif", "selesai", "diambil", "dibatalkan"
    val selectedDeadlineFilter: String? = null, // "terlambat", "mendekati", "hari_ini"
    val selectedPembayaranFilter: String? = null, // "belum_lunas"
    val errorMessage: String? = null,

    // Detail State
    val selectedPesanan: PesananDto? = null,
    val isLoadingDetail: Boolean = false,
    val detailErrorMessage: String? = null,

    val isActionLoading: Boolean = false,
    val successSnackbar: String? = null
)

class PesananViewModel(application: Application) : AndroidViewModel(application) {

    private val pesananRepository = PesananRepository.create(application)

    private val _uiState = MutableStateFlow(PesananUiState())
    val uiState: StateFlow<PesananUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPesananList()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            loadPesananList(isRefresh = false)
        }
    }

    fun selectStatus(status: String?) {
        _uiState.update { it.copy(selectedStatus = status) }
        loadPesananList(isRefresh = false)
    }

    fun selectDeadlineFilter(deadline: String?) {
        _uiState.update { it.copy(selectedDeadlineFilter = deadline) }
        loadPesananList(isRefresh = false)
    }

    fun selectPembayaranFilter(pembayaran: String?) {
        _uiState.update { it.copy(selectedPembayaranFilter = pembayaran) }
        loadPesananList(isRefresh = false)
    }

    fun loadPesananList(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            } else if (_uiState.value.pesananList.isEmpty()) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            val result = pesananRepository.getPesananList(
                search = _uiState.value.searchQuery.ifBlank { null },
                status = _uiState.value.selectedStatus,
                deadline = _uiState.value.selectedDeadlineFilter,
                pembayaran = _uiState.value.selectedPembayaranFilter,
                perPage = 50
            )

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            pesananList = result.data,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun selectPesanan(pesanan: PesananDto) {
        _uiState.update {
            it.copy(
                selectedPesanan = pesanan,
                isLoadingDetail = true,
                detailErrorMessage = null
            )
        }
        loadPesananDetail(pesanan.id)
    }

    fun clearSelectedPesanan() {
        _uiState.update {
            it.copy(
                selectedPesanan = null,
                isLoadingDetail = false,
                detailErrorMessage = null
            )
        }
    }

    fun loadPesananDetail(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true, detailErrorMessage = null) }
            when (val result = pesananRepository.getPesananDetail(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedPesanan = result.data,
                            isLoadingDetail = false,
                            detailErrorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingDetail = false,
                            detailErrorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun batalkanPesanan(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true) }
            when (val result = pesananRepository.batalkanPesanan(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isActionLoading = false,
                            successSnackbar = "Pesanan berhasil dibatalkan."
                        )
                    }
                    loadPesananList(isRefresh = true)
                    loadPesananDetail(id)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isActionLoading = false,
                            detailErrorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(successSnackbar = null) }
    }
}
