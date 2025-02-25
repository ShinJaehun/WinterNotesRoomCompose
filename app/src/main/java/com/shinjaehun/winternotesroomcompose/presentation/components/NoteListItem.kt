package com.shinjaehun.winternotesroomcompose.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.presentation.simpleDate

@Composable
fun NoteListItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(
            containerColor = note.color?.colorValue ?: ImageColor.WHITE.colorValue
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ){
                NoteImage(
                    note = note,
                    modifier = Modifier.size(80.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = note.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = note.contents,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = simpleDate(note.dateTime),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

//    Row(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp)
//            .height(IntrinsicSize.Min),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Box(
//            modifier = Modifier
//            .height(100.dp),
//            contentAlignment = Alignment.Center
//        ){
//            NoteImage(
//                note = note,
//                modifier = Modifier.size(80.dp)
//            )
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .weight(1f),
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = note.title,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1,
//                fontSize = 16.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Text(
//                text = note.contents,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1,
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Text(
//                text = simpleDate(note.dateTime),
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }

}