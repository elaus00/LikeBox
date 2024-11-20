package com.example.likebox.presentation.view.screens.library.viewmodel
import androidx.lifecycle.ViewModel

abstract class BaseDetailViewModel : ViewModel() {
    protected fun handleError(error: Throwable): String {
        return error.message ?: "An unexpected error occurred"
    }
}