package com.example.passwordmanager.ui

import android.app.Application
import com.example.passwordmanager.ui.encryption.CryptoManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // This ensures key is created before encryption is attempted.
        CryptoManager.ensureKeyExists()
    }
}