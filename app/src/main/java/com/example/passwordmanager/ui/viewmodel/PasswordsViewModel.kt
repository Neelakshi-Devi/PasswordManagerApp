package com.example.passwordmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.ui.model.PasswordEntryEntity
import com.example.passwordmanager.ui.repository.PasswordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PasswordsViewModel(private val repo: PasswordsRepository) : ViewModel() {

    // Expose Flow from repo to UI - Compose will collect as state
    val entriesFlow: Flow<List<PasswordEntryEntity>> = repo.allEntries()

    fun addEntry(title: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addEntry(title.trim(), username.trim(), password)
        }
    }

    fun deleteEntry(entry: PasswordEntryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(entry)
        }
    }

    fun updateEntry(entry: PasswordEntryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.update(entry)
        }
    }
}