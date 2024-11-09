package com.example.likebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
//    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
//    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()
//
//    fun startSync() {
//        viewModelScope.launch {
//            _syncState.value = SyncState.Syncing
//            try {
//                // 여기에 실제 동기화 로직 구현
//                delay(2000) // 시뮬레이션을 위한 딜레이
//                _syncState.value = SyncState.Success
//            } catch (e: Exception) {
//                _syncState.value = SyncState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
//            }
//        }
//    }
}