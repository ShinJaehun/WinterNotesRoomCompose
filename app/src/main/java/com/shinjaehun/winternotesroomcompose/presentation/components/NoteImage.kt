package com.shinjaehun.winternotesroomcompose.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shinjaehun.winternotesroomcompose.R
import com.shinjaehun.winternotesroomcompose.domain.Note
import java.io.File

@Composable
fun NoteImage(
    title: String?,
    imagePath: String?, // 변경됨: ByteArray → imagePath
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {

//    LaunchedEffect(imageBytes) {
//        Log.i("NoteDebug", "imageBytes 크기: ${imageBytes?.size ?: 0} bytes")
//    }

//    val bitmap = rememberBitmapFromBytes(imageBytes) //coil로 처리할꺼니까 이젠 더이상 필요 없어요~~

    val imageModifier = modifier.clip(RoundedCornerShape(35))

    Box(
        modifier = imageModifier
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
//        when {
//            bitmap != null -> {
//                Image(
//                    bitmap = bitmap,
//                    contentDescription = title,
//                    modifier = Modifier.matchParentSize(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//            imageBytes != null -> {
//                // 이미지 존재하지만 아직 로딩 중일 때
//                CircularProgressIndicator(modifier = Modifier.size(iconSize))
//            }
//            else -> {
//                // 이미지 자체가 없을 때
//                Icon(
//                    painter = painterResource(R.drawable.ic_image),
//                    contentDescription = title,
//                    modifier = Modifier.size(iconSize),
//                    tint = MaterialTheme.colorScheme.onSecondaryContainer
//                )
//            }
//        }
        if (imagePath != null) {
            AsyncImage(
                model = File(imagePath),
                contentDescription = title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_image),
                error =  painterResource(R.drawable.ic_image),
            )
        } else {
            // 이미지 자체가 없을 때
            Icon(
                painter = painterResource(R.drawable.ic_image),
                contentDescription = title,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}