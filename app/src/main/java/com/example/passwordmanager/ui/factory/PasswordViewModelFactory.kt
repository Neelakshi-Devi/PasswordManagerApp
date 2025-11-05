package com.example.passwordmanager.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanager.ui.repository.PasswordsRepository
import com.example.passwordmanager.ui.viewmodel.PasswordsViewModel

class PasswordsViewModelFactory(private val repository: PasswordsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}