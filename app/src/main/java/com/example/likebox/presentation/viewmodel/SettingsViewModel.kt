package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.Settings
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.model.settings.NotificationSettings
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings
import com.example.likebox.domain.repository.SettingsRepository
import com.example.likebox.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()
    val uiState = MutableStateFlow(SettingsState())

    private val _uiEvent = MutableSharedFlow<SettingsUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadSettings()
        loadUserProfile()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings()
                .catch { error ->
                    emitUiEvent(SettingsUiEvent.Error(error.message ?: "Failed to load settings"))
                }
                .collect { settings ->
                    _settingsState.update { it.copy(
                        isLoading = false,
                        theme = settings.theme,
                        sync = settings.sync,
                        notification = settings.notification
                    )}
                }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getCurrentUser()
                .catch { error ->
                    emitUiEvent(SettingsUiEvent.Error(error.message ?: "Failed to load profile"))
                }
                .collect { user ->
                    _settingsState.update { it.copy(
                        user = user
                    )}
                }
        }
    }

    fun updateThemeSettings(themeSettings: ThemeSettings) {
        updateSettings { it.copy(theme = themeSettings) }
    }

    fun updateSyncSettings(syncSettings: SyncSettings) {
        updateSettings { it.copy(sync = syncSettings) }
    }

    fun updateNotificationSettings(notificationSettings: NotificationSettings) {
        updateSettings { it.copy(notification = notificationSettings) }
    }

    fun updateProfile(
        nickname: String? = null,
        email: String? = null,
        phoneNumber: Number? = null
    ) {
        viewModelScope.launch {  // coroutine scope 안에서
            _settingsState.update { it.copy(isLoading = true) }

            val currentUser = _settingsState.value.user ?: return@launch
            val updatedUser = currentUser.copy(
                nickName = nickname ?: currentUser.nickName,
                email = email ?: currentUser.email,
                phoneNumber = phoneNumber ?: currentUser.phoneNumber
            )

            userRepository.updateUser(updatedUser)
                .onSuccess {
                    _settingsState.update { it.copy(
                        isLoading = false,
                        user = updatedUser
                    )}
                    emitUiEvent(SettingsUiEvent.ProfileUpdateSuccess)  // suspend 함수 호출
                }
                .onFailure { error ->
                    _settingsState.update { it.copy(isLoading = false) }
                    emitUiEvent(SettingsUiEvent.Error(error.message ?: "Failed to update profile"))  // suspend 함수 호출
                }
        }
    }

    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            _settingsState.update { it.copy(isLoading = true) }

            userRepository.updateProfileImage(imageUri)
                .onSuccess { newImageUrl ->
                    _settingsState.update { state ->
                        state.copy(
                            isLoading = false,
                            user = state.user?.copy(profilePictureUrl = newImageUrl)
                        )
                    }
                    emitUiEvent(SettingsUiEvent.ProfileImageUpdateSuccess)
                }
                .onFailure { error ->
                    _settingsState.update { it.copy(isLoading = false) }
                    emitUiEvent(SettingsUiEvent.Error(error.message ?: "Failed to update profile image"))
                }
        }
    }

    private fun updateSettings(update: (Settings) -> Settings) {
        viewModelScope.launch {
            _settingsState.update { it.copy(isLoading = true) }

            val currentSettings = with(_settingsState.value) {
                Settings(
                    userId = user?.userId ?: return@launch,
                    theme = theme,
                    sync = sync,
                    notification = notification
                )
            }

            settingsRepository.updateSettings(update(currentSettings))
                .onSuccess {
                    loadSettings()
                    emitUiEvent(SettingsUiEvent.SettingsUpdateSuccess)
                }
                .onFailure { error ->
                    _settingsState.update { it.copy(isLoading = false) }
                    emitUiEvent(SettingsUiEvent.Error(error.message ?: "Failed to update settings"))
                }
        }
    }

    private suspend fun emitUiEvent(event: SettingsUiEvent) {
        _uiEvent.emit(event)
    }
}

data class SettingsState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val theme: ThemeSettings = ThemeSettings(),
    val sync: SyncSettings = SyncSettings(),
    val notification: NotificationSettings = NotificationSettings()
)

sealed interface SettingsUiEvent {
    data class Error(val message: String) : SettingsUiEvent
    data object SettingsUpdateSuccess : SettingsUiEvent
    data object ProfileUpdateSuccess : SettingsUiEvent
    data object ProfileImageUpdateSuccess : SettingsUiEvent
}