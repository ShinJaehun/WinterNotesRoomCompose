package com.shinjaehun.winternotesroomcompose.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shinjaehun.winternotesroomcompose.R
import com.shinjaehun.winternotesroomcompose.domain.Note

@Composable
fun NoteImage(
    title: String?,
    imageBytes: ByteArray?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {

    LaunchedEffect(imageBytes) {
        Log.i("NoteDebug", "imageBytes 크기: ${imageBytes?.size ?: 0} bytes")
    }

    val bitmap = rememberBitmapFromBytes(imageBytes)
    val imageModifier = modifier.clip(RoundedCornerShape(35))

    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = title,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
       Box(
          modifier = imageModifier
              .background(MaterialTheme.colorScheme.secondaryContainer),
          contentAlignment = Alignment.Center
       )  {
          Icon(
              painter = painterResource(R.drawable.ic_image),
              contentDescription = title,
              modifier = Modifier.size(iconSize),
              tint = MaterialTheme.colorScheme.onSecondaryContainer
          )
       }
    }
}