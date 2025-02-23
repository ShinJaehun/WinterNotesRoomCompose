package com.shinjaehun.winternotesroomcompose.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shinjaehun.winternotesroomcompose.domain.Note

@Composable
fun NoteListItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    Row(
       modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = note.title,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))

        Text(
            text = note.dateTime,
            modifier = Modifier.weight(1f)
        )
    }
}