package com.example.likebox.presentation.view.screens.auth.platform

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.data.util.SpotifyConfig
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.view.navigation.NavigationCommand
import com.example.likebox.presentation.view.screens.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class PlatformConnectionViewModel @Inject constructor(
    private val platformRepository: PlatformRepository,
    private val authRepository: AuthRepository,
    application: Application
) : ViewModel() {

    data class UiState(
        val platform: MusicPlatform? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isConnected: Boolean = false,
        val navigationCommand: NavigationCommand? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private val context = application
    private var authReceiver: BroadcastReceiver? = null


    fun initializePlatform(platformId: String) {
        try {
            val platform = MusicPlatform.fromId(platformId)
            _uiState.update { it.copy(platform = platform) }
            checkPlatformConnection(platform)
        } catch (e: Exception) {
            setError("Invalid platform ID")
        }
    }

    private fun checkPlatformConnection(platform: MusicPlatform) {
        viewModelScope.launch {
            platformRepository.isPlatformConnected(platform)
                .onSuccess { isConnected ->
                    _uiState.update { it.copy(isConnected = isConnected) }
                }
                .onFailure { handleError(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun PlatformConnect(activity: ComponentActivity) {  // Activity를 파라미터로 받음
        val platform = _uiState.value.platform ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val authUrl = getSpotifyAuthUrl()
                val authCode = openBrowserAndWaitForResult(activity, authUrl)  // activity 전달

                platformRepository.connectPlatform(platform, authCode)
                    .onSuccess {
                        _uiState.update { state ->
                            state.copy(
                                isConnected = true,
                                isLoading = false,
                                navigationCommand = NavigationCommand.NavigateToAndClearStack(
                                    Screens.Main.Home.Root
                                )
                            )
                        }
                    }
                    .onFailure { handleError(it) }

            } catch (e: Exception) {
                handleError(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun openBrowserAndWaitForResult(activity: ComponentActivity, url: String): String {
        return suspendCancellableCoroutine { continuation ->
            try {
                val intent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()

                authReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val code = intent.data?.getQueryParameter("code")
                        if (code != null) {
                            continuation.resume(code)
                            activity.unregisterReceiver(this)
                            authReceiver = null
                        }
                    }
                }

                activity.registerReceiver(
                    authReceiver,
                    IntentFilter("com.example.likebox.AUTH_CALLBACK"),
                    Context.RECEIVER_NOT_EXPORTED
                )

                intent.launchUrl(activity, Uri.parse(url))

                continuation.invokeOnCancellation {
                    authReceiver?.let {
                        activity.unregisterReceiver(it)
                        authReceiver = null
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    // ViewModel이 clear될 때 receiver 해제
    override fun onCleared() {
        super.onCleared()
        authReceiver?.let { receiver ->
            context.unregisterReceiver(receiver)
            authReceiver = null
        }
    }

    private fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                error = throwable.message ?: "An unknown error occurred",
                isLoading = false
            )
        }
    }

    fun onNavigationComplete() {
        _uiState.update { it.copy(navigationCommand = null) }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(error = message, isLoading = false) }
    }

    private fun getSpotifyAuthUrl(): String {
        val scopes = "user-library-read user-library-modify playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read"
//        val scopes = "user-library-read user-library-modify playlist-read-private playlist-modify-private playlist-modify-public user-top-read"
        return "https://accounts.spotify.com/authorize" +
                "?client_id=${SpotifyConfig.CLIENT_ID}" +
                "&response_type=code" +
                "&redirect_uri=${URLEncoder.encode("com.example.likebox://callback", "UTF-8")}" +
                "&scope=${URLEncoder.encode(scopes, "UTF-8")}"
    }
}