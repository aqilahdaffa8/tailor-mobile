package com.tailor.mobile.ui.screens.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tailor.mobile.data.repository.DashboardRepository
import com.tailor.mobile.data.source.remote.response.BahanMenipisDto
import com.tailor.mobile.data.source.remote.response.DashboardSummaryDto
import com.tailor.mobile.data.source.remote.response.PesananRingkasDto
import com.tailor.mobile.data.source.remote.response.ReminderDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DashboardTab {
    MENDEKATI,
    TERLAMBAT
}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val summary: DashboardSummaryDto? = null,
    val pesananMendekati: List<PesananRingkasDto> = emptyList(),
    val pesananTerlambat: List<PesananRingkasDto> = emptyList(),
    val stokMenipis: List<BahanMenipisDto> = emptyList(),
    val reminders: List<ReminderDto> = emptyList(),
    val activeTab: DashboardTab = DashboardTab.MENDEKATI,
    val errorMessage: String? = null
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardRepository = DashboardRepository.create(application)

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun selectTab(tab: DashboardTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
        loadDashboardData(isRefresh = true)
    }

    fun loadDashboardData(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            try {
                // Fetch in parallel using async coroutines
                val summaryDeferred = async { dashboardRepository.getSummary() }
                val mendekatiDeferred = async { dashboardRepository.getPesananMendekati(10) }
                val terlambatDeferred = async { dashboardRepository.getPesananTerlambat(10) }
                val stokDeferred = async { dashboardRepository.getStokMenipis(10) }
                val remindersDeferred = async { dashboardRepository.getReminders() }

                val summaryRes = summaryDeferred.await()
                val mendekatiRes = mendekatiDeferred.await()
                val terlambatRes = terlambatDeferred.await()
                val stokRes = stokDeferred.await()
                val remindersRes = remindersDeferred.await()

                val summaryData = if (summaryRes is Resource.Success) summaryRes.data else null
                val mendekatiData = if (mendekatiRes is Resource.Success) mendekatiRes.data else emptyList()
                val terlambatData = if (terlambatRes is Resource.Success) terlambatRes.data else emptyList()
                val stokData = if (stokRes is Resource.Success) stokRes.data else emptyList()
                val remindersData = if (remindersRes is Resource.Success) remindersRes.data else emptyList()

                val errorMsg = when {
                    summaryRes is Resource.Error -> summaryRes.message
                    mendekatiRes is Resource.Error -> mendekatiRes.message
                    else -> null
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        summary = summaryData ?: it.summary,
                        pesananMendekati = mendekatiData,
                        pesananTerlambat = terlambatData,
                        stokMenipis = stokData,
                        reminders = remindersData,
                        errorMessage = errorMsg
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = "Gagal memuat data: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun dismissReminder(reminderId: Int) {
        viewModelScope.launch {
            val resource = dashboardRepository.dismissReminder(reminderId)
            if (resource is Resource.Success) {
                _uiState.update { current ->
                    current.copy(
                        reminders = current.reminders.filter { it.id != reminderId }
                    )
                }
            }
        }
    }
}
