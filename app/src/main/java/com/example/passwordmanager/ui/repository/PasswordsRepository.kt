package com.example.passwordmanager.ui.repository

import androidx.annotation.WorkerThread
import com.example.passwordmanager.ui.PasswordEntryDao
import com.example.passwordmanager.ui.model.PasswordEntryEntity
import kotlinx.coroutines.flow.Flow


class PasswordsRepository(private val dao: PasswordEntryDao) {
    fun allEntries(): Flow<List<PasswordEntryEntity>> = dao.getAllEntries()

    @WorkerThread
    suspend fun addEntry(title: String, username: String, password: String) {
        dao.insert(PasswordEntryEntity(title = title, username = username, password = password))
    }

    @WorkerThread
    suspend fun delete(entry: PasswordEntryEntity) {
        dao.delete(entry)
    }

    @WorkerThread
    suspend fun update(entry: PasswordEntryEntity) {
        dao.update(entry)
    }
}