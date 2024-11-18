package com.example.likebox.presentation.viewmodel.library
import androidx.lifecycle.ViewModel

abstract class BaseDetailViewModel : ViewModel() {
    protected fun handleError(error: Throwable): String {
        return error.message ?: "An unexpected error occurred"
    }
}