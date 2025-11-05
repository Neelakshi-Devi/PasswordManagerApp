package com.example.passwordmanager.ui

import androidx.room.TypeConverter
import com.example.passwordmanager.ui.encryption.CryptoManager

object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromPlainPasswordToEncrypted(plain: String?): String? {
        if (plain == null) return null
        return CryptoManager.encrypt(plain)
    }

    @TypeConverter
    @JvmStatic
    fun fromEncryptedToPlain(encrypted: String?): String? {
        if (encrypted == null) return null
        return CryptoManager.decrypt(encrypted)
    }
}
