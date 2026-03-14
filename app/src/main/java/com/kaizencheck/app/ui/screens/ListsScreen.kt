package com.kaizencheck.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kaizencheck.app.data.ChecklistWithCount
import com.kaizencheck.app.ui.components.ChecklistCard
import com.kaizencheck.app.viewmodel.ChecklistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    viewModel: ChecklistViewModel,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    val checklists by viewModel.checklistsWithCounts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Listas") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova lista")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (checklists.isEmpty()) {
                EmptyListsState(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = checklists,
                        key = { it.checklist.id }
                    ) { item ->
                        ChecklistCard(
                            checklist = item.checklist,
                            completedCount = item.completedCount,
                            totalCount = item.totalCount,
                            onClick = { onListClick(item.checklist.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddListDialog(
            name = newListName,
            onNameChange = { newListName = it },
            onDismiss = {
                showAddDialog = false
                newListName = ""
            },
            onConfirm = {
                if (newListName.isNotBlank()) {
                    viewModel.addChecklist(newListName.trim())
                    showAddDialog = false
                    newListName = ""
                }
            }
        )
    }
}

@Composable
private fun EmptyListsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhuma lista ainda",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toque em + para criar sua primeira checklist",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AddListDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova lista") },
        text = {
            androidx.compose.material3.OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nome da lista") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Criar", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}
