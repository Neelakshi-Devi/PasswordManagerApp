package com.example.passwordmanager.ui.repository

import androidx.annotation.WorkerThread
import com.example.passwordmanager.ui.PasswordEntryDao
import com.example.passwordmanager.ui.encryption.CryptoManager
import com.example.passwordmanager.ui.model.PasswordEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PasswordsRepository(private val dao: PasswordEntryDao) {
    fun allEntries(): Flow<List<PasswordEntryEntity>> = dao.getAllEntries()

    fun allEntriesDecrypted(): Flow<List<PasswordEntryEntity>> {
        // If DAO returns encrypted password strings, map and decrypt
        return dao.getAllEntries().map { list ->
            list.map { entity ->
                entity.copy(password = CryptoManager.decrypt(entity.password))
            }
        }
    }

    @WorkerThread
    suspend fun addEntry(title: String, username: String, password: String) {
        val encrypted = CryptoManager.encrypt(password)
        dao.insert(PasswordEntryEntity(title = title, username = username, password = encrypted))
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