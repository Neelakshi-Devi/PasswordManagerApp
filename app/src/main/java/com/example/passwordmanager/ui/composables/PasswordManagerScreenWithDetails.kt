package com.example.passwordmanager.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.model.PasswordEntryEntity
import com.example.passwordmanager.ui.viewmodel.PasswordsViewModel
import kotlinx.coroutines.launch

enum class SheetMode { HIDDEN, ADD, VIEW, EDIT }

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerScreenWithDetails(viewModel: PasswordsViewModel) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val entries by viewModel.entriesFlow.collectAsState(initial = emptyList())

    // sheet mode and selected entry
    var sheetMode by remember { mutableStateOf(SheetMode.HIDDEN) }
    var selectedEntry by remember { mutableStateOf<PasswordEntryEntity?>(null) }

    // helper to open sheets
    fun showAddSheet() {
        sheetMode = SheetMode.ADD
        selectedEntry = null
        scope.launch { sheetState.show() }
    }
    fun showViewSheet(entry: PasswordEntryEntity) {
        sheetMode = SheetMode.VIEW
        selectedEntry = entry
        scope.launch { sheetState.show() }
    }
    fun showEditSheet(entry: PasswordEntryEntity) {
        sheetMode = SheetMode.EDIT
        selectedEntry = entry
        scope.launch { sheetState.show() }
    }
    fun hideSheet() {
        scope.launch { sheetState.hide() }
        sheetMode = SheetMode.HIDDEN
        selectedEntry = null
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            // Decide sheet content by mode
            when (sheetMode) {
                SheetMode.ADD -> {
                    AddEditAccountBottomSheet(
                        initial = null,
                        onSave = { title, username, password ->
                            viewModel.addEntry(title, username, password)
                            hideSheet()
                        },
                        onCancel = { hideSheet() }
                    )
                }
                SheetMode.VIEW -> {
                    selectedEntry?.let { entry ->
                        AccountDetailsSheet(
                            entry = entry,
                            onEdit = { showEditSheet(entry) },
                            onDelete = {
                                viewModel.deleteEntry(entry)
                                hideSheet()
                            },
                            onClose = { hideSheet() }
                        )
                    } ?: Box(Modifier.height(200.dp)) // empty fallback
                }
                SheetMode.EDIT -> {
                    selectedEntry?.let { entry ->
                        AddEditAccountBottomSheet(
                            initial = entry,
                            onSave = { title, username, password ->
                                // create updated entity preserving id
                                val updated = entry.copy(title = title, username = username, password = password)
                                viewModel.updateEntry(updated)
                                hideSheet()
                            },
                            onCancel = { hideSheet() }
                        )
                    } ?: Box(Modifier.height(200.dp))
                }
                SheetMode.HIDDEN -> {
                    // nothing (ModalBottomSheetLayout requires some content)
                    Box(modifier = Modifier.height(1.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Password Manager", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddSheet() },
                    backgroundColor = Color(0xFF2E7DFF)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { paddingValues ->
            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No accounts yet. Tap + to add one.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = 8.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    items(entries, key = { it.id }) { item ->
                        PasswordItemFromEntity(
                            entry = item,
                            onClick = { showViewSheet(item) } // open details on click
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }
}