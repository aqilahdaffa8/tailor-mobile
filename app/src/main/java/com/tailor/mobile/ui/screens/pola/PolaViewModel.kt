package com.tailor.mobile.ui.screens.pola

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tailor.mobile.data.repository.PolaRepository
import com.tailor.mobile.data.source.remote.request.JenisPakaianRequest
import com.tailor.mobile.data.source.remote.request.PolaDetailRequest
import com.tailor.mobile.data.source.remote.response.JenisPakaianDto
import com.tailor.mobile.data.source.remote.response.KategoriPolaDto
import com.tailor.mobile.data.source.remote.response.PolaDetailDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PolaUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val kategoriList: List<KategoriPolaDto> = emptyList(),
    val selectedKategoriId: Int? = null, // null means "Semua"
    val searchQuery: String = "",
    val errorMessage: String? = null,

    // Detail Jenis Pakaian state
    val selectedJenisPakaian: JenisPakaianDto? = null,
    val selectedJenisDetails: List<PolaDetailDto> = emptyList(),
    val isLoadingDetail: Boolean = false,
    val detailErrorMessage: String? = null,
    val selectedSizeIndex: Int = 0,

    // Form submission states
    val isSaving: Boolean = false,
    val formError: String? = null,
    val successSnackbar: String? = null
)

class PolaViewModel(application: Application) : AndroidViewModel(application) {

    private val polaRepository = PolaRepository.create(application)

    private val _uiState = MutableStateFlow(PolaUiState())
    val uiState: StateFlow<PolaUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadKategoriList()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            loadKategoriList(isRefresh = false, search = query)
        }
    }

    fun selectKategori(kategoriId: Int?) {
        _uiState.update { it.copy(selectedKategoriId = kategoriId) }
    }

    fun loadKategoriList(isRefresh: Boolean = false, search: String? = null) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            } else if (_uiState.value.kategoriList.isEmpty()) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            val query = search ?: _uiState.value.searchQuery.ifBlank { null }
            when (val result = polaRepository.getKategoriList(query)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            kategoriList = result.data,
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

    fun selectJenisPakaian(jenis: JenisPakaianDto) {
        _uiState.update {
            it.copy(
                selectedJenisPakaian = jenis,
                selectedJenisDetails = emptyList(),
                selectedSizeIndex = 0,
                isLoadingDetail = true,
                detailErrorMessage = null
            )
        }
        loadPolaDetail(jenis.id)
    }

    fun clearSelectedJenisPakaian() {
        _uiState.update {
            it.copy(
                selectedJenisPakaian = null,
                selectedJenisDetails = emptyList(),
                selectedSizeIndex = 0,
                detailErrorMessage = null
            )
        }
    }

    fun setSelectedSizeIndex(index: Int) {
        _uiState.update { it.copy(selectedSizeIndex = index) }
    }

    fun loadPolaDetail(jenisId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true, detailErrorMessage = null) }
            when (val result = polaRepository.getPolaDetailByJenis(jenisId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedJenisDetails = result.data,
                            selectedSizeIndex = if (result.data.isNotEmpty()) 0 else 0,
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

    fun createJenisPakaian(
        kategoriId: Int,
        namaJenis: String,
        deskripsi: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }
            val request = JenisPakaianRequest(
                kategoriPolaId = kategoriId,
                namaJenis = namaJenis.trim(),
                deskripsi = deskripsi?.trim()?.ifBlank { null }
            )
            when (val result = polaRepository.createJenisPakaian(request)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = null,
                            successSnackbar = "Jenis pakaian '${result.data.namaJenis}' berhasil ditambahkan."
                        )
                    }
                    loadKategoriList(isRefresh = true)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, formError = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deleteJenisPakaian(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
            when (val result = polaRepository.deleteJenisPakaian(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingDetail = false,
                            selectedJenisPakaian = null,
                            successSnackbar = "Jenis pakaian berhasil dihapus."
                        )
                    }
                    loadKategoriList(isRefresh = true)
                    onSuccess()
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

    fun savePolaDetail(
        detailId: Int?,
        jenisPakaianId: Int,
        size: String,
        dataUkuran: Map<String, String>,
        catatan: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }
            val request = PolaDetailRequest(
                jenisPakaianId = jenisPakaianId,
                size = size.trim(),
                dataUkuran = dataUkuran,
                catatan = catatan?.trim()?.ifBlank { null }
            )

            val result = if (detailId == null) {
                polaRepository.createPolaDetail(request)
            } else {
                polaRepository.updatePolaDetail(detailId, request)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = null,
                            successSnackbar = "Ukuran standard '$size' berhasil disimpan."
                        )
                    }
                    loadPolaDetail(jenisPakaianId)
                    loadKategoriList(isRefresh = true)
                    onSuccess()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, formError = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deletePolaDetail(detailId: Int, jenisPakaianId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetail = true) }
            when (val result = polaRepository.deletePolaDetail(detailId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingDetail = false,
                            successSnackbar = "Ukuran standar berhasil dihapus."
                        )
                    }
                    loadPolaDetail(jenisPakaianId)
                    loadKategoriList(isRefresh = true)
                    onSuccess()
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

    fun clearSnackbar() {
        _uiState.update { it.copy(successSnackbar = null) }
    }

    fun clearFormError() {
        _uiState.update { it.copy(formError = null) }
    }
}
