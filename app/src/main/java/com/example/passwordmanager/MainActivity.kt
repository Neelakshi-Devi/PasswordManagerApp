package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.passwordmanager.ui.AppDatabase
import com.example.passwordmanager.ui.composables.PasswordManagerScreenWithDetails
import com.example.passwordmanager.ui.factory.PasswordsViewModelFactory
import com.example.passwordmanager.ui.repository.PasswordsRepository
import com.example.passwordmanager.ui.viewmodel.PasswordsViewModel

class MainActivity : ComponentActivity() {
    // We'll initialize DB & repo in onCreate and create the VM via factory
    private lateinit var viewModelFactory: PasswordsViewModelFactory
    private val vm: PasswordsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        // Initialize DB and repository
        val db = AppDatabase.getInstance(applicationContext)
        val repo = PasswordsRepository(db.passwordEntryDao())
        viewModelFactory = PasswordsViewModelFactory(repo)

        setContent {
            PasswordManagerApp {
                PasswordManagerScreenWithDetails(viewModel = vm)
            }
        }
    }
}

@Composable
fun PasswordManagerApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(color = Color(0xFFF3F5F8)) {
            content()
        }
    }
}