package com.example.passwordmanager.ui

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.passwordmanager.ui.model.PasswordEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordEntryDao {
    @Query("SELECT * FROM password_entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<PasswordEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: PasswordEntryEntity)

    @Delete
    suspend fun delete(entry: PasswordEntryEntity)

    @Update
    suspend fun update(entry: PasswordEntryEntity)
}