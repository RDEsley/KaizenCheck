package com.kaizencheck.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kaizencheck.app.data.ChecklistItemEntity

@Composable
fun ChecklistItemRow(
    item: ChecklistItemEntity,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (item.isCompleted) 0.6f else 1f,
        label = "alpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
