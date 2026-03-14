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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kaizencheck.app.data.ChecklistEntity
import com.kaizencheck.app.data.ChecklistItemEntity
import com.kaizencheck.app.ui.components.ChecklistItemRow
import com.kaizencheck.app.viewmodel.ChecklistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    checklistId: Long,
    viewModel: ChecklistViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemText by remember { mutableStateOf("") }
    var checklistName by remember { mutableStateOf("Checklist") }

    LaunchedEffect(checklistId) {
        checklistName = viewModel.getChecklistName(checklistId) ?: "Checklist"
    }

    val items by viewModel.getItemsFlow(checklistId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(checklistName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
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
                Icon(Icons.Default.Add, contentDescription = "Novo item")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (items.isEmpty()) {
                EmptyItemsState(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = items,
                        key = { it.id }
                    ) { item ->
                        ChecklistItemRow(
                            item = item,
                            onCheckedChange = { viewModel.toggleItem(item) },
                            onDelete = { viewModel.deleteItem(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddItemDialog(
            text = newItemText,
            onTextChange = { newItemText = it },
            onDismiss = {
                showAddDialog = false
                newItemText = ""
            },
            onConfirm = {
                if (newItemText.isNotBlank()) {
                    viewModel.addItem(checklistId, newItemText.trim())
                    showAddDialog = false
                    newItemText = ""
                }
            }
        )
    }
}

@Composable
private fun EmptyItemsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhum item",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toque em + para adicionar itens",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AddItemDialog(
    text: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo item") },
        text = {
            androidx.compose.material3.OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text("O que precisa fazer?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onConfirm) {
                Text("Adicionar", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}
