package com.tailor.mobile.ui.screens.pelanggan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tailor.mobile.data.repository.PelangganRepository
import com.tailor.mobile.data.repository.PengukuranRepository
import com.tailor.mobile.data.source.remote.request.PelangganRequest
import com.tailor.mobile.data.source.remote.request.PengukuranRequest
import com.tailor.mobile.data.source.remote.response.PelangganDto
import com.tailor.mobile.data.source.remote.response.PengukuranDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PelangganUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val pelangganList: List<PelangganDto> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,

    // Detail state
    val selectedPelanggan: PelangganDto? = null,
    val isLoadingDetail: Boolean = false,
    val detailErrorMessage: String? = null,

    // Pengukuran state
    val pengukuranList: List<PengukuranDto> = emptyList(),
    val latestPengukuran: PengukuranDto? = null,
    val isLoadingPengukuran: Boolean = false,

    // Form submission states
    val isSaving: Boolean = false,
    val formError: String? = null,
    val successSnackbar: String? = null
)

class PelangganViewModel(application: Application) : AndroidViewModel(application) {

    private val pelangganRepository = PelangganRepository.create(application)
    private val pengukuranRepository = PengukuranRepository.create(application)

    private val _uiState = MutableStateFlow(PelangganUiState())
    val uiState: StateFlow<PelangganUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPelangganList()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350) // Debounce 350ms
            loadPelangganList()
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
        loadPelangganList(isRefresh = true)
    }

    fun loadPelangganList(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh && _uiState.value.pelangganList.isEmpty()) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            val query = _uiState.value.searchQuery
            val result = pelangganRepository.getPelangganList(search = query)

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            pelangganList = result.data,
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

    fun selectPelanggan(pelangganId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true, detailErrorMessage = null) }
            val result = pelangganRepository.getPelangganDetail(pelangganId)
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingDetail = false,
                            selectedPelanggan = result.data,
                            pengukuranList = result.data.pengukuran ?: emptyList()
                        )
                    }
                    loadPengukuranHistory(pelangganId)
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

    fun clearSelectedPelanggan() {
        _uiState.update {
            it.copy(
                selectedPelanggan = null,
                pengukuranList = emptyList(),
                latestPengukuran = null,
                detailErrorMessage = null
            )
        }
    }

    fun loadPengukuranHistory(pelangganId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPengukuran = true) }
            val riwayatResult = pengukuranRepository.getPengukuranByPelanggan(pelangganId)
            val latestResult = pengukuranRepository.getLatestPengukuran(pelangganId)

            val riwayat = if (riwayatResult is Resource.Success) riwayatResult.data else emptyList()
            val latest = if (latestResult is Resource.Success) latestResult.data else null

            _uiState.update {
                it.copy(
                    isLoadingPengukuran = false,
                    pengukuranList = riwayat,
                    latestPengukuran = latest
                )
            }
        }
    }

    fun savePelanggan(
        id: Int? = null,
        nama: String,
        noHp: String,
        alamat: String?,
        catatan: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }
            val request = PelangganRequest(
                nama = nama.trim(),
                noHp = noHp.trim(),
                alamat = alamat?.trim()?.ifBlank { null },
                catatan = catatan?.trim()?.ifBlank { null }
            )

            val result = if (id == null) {
                pelangganRepository.createPelanggan(request)
            } else {
                pelangganRepository.updatePelanggan(id, request)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            successSnackbar = if (id == null) "Pelanggan berhasil ditambahkan" else "Data pelanggan diperbarui",
                            formError = null
                        )
                    }
                    loadPelangganList(isRefresh = true)
                    if (id != null) {
                        selectPelanggan(id)
                    }
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deletePelanggan(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }
            val result = pelangganRepository.deletePelanggan(id)
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            selectedPelanggan = null,
                            successSnackbar = "Pelanggan berhasil dihapus"
                        )
                    }
                    loadPelangganList(isRefresh = true)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun savePengukuran(
        id: Int? = null,
        pelangganId: Int,
        tanggalUkur: String,
        dataUkuran: Map<String, String>,
        catatan: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }
            val request = PengukuranRequest(
                pelangganId = pelangganId,
                tanggalUkur = tanggalUkur,
                dataUkuran = dataUkuran,
                catatan = catatan?.trim()?.ifBlank { null }
            )

            val result = if (id == null) {
                pengukuranRepository.createPengukuran(request)
            } else {
                pengukuranRepository.updatePengukuran(id, request)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            successSnackbar = if (id == null) "Pengukuran baru disimpan" else "Pengukuran diperbarui",
                            formError = null
                        )
                    }
                    loadPengukuranHistory(pelangganId)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deletePengukuran(id: Int, pelangganId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val result = pengukuranRepository.deletePengukuran(id)
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            successSnackbar = "Pengukuran dihapus"
                        )
                    }
                    loadPengukuranHistory(pelangganId)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearFormError() {
        _uiState.update { it.copy(formError = null) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(successSnackbar = null) }
    }
}
