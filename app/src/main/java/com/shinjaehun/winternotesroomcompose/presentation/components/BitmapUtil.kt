package com.shinjaehun.winternotesroomcompose.presentation.components

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? {
//    return remember(bytes) {
//        if (bytes != null) {
//            BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
//        } else {
//            null
//        }
//    }

//    return produceState<ImageBitmap?>(initialValue = null, key1 = bytes) {
//        if (bytes != null) {
//            withContext(Dispatchers.IO) {
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                value = bitmap.asImageBitmap()
//            }
//        }
//    }.value

    if (bytes == null) return null

    return produceState<ImageBitmap?>(initialValue = null, key1 = bytes) {
        withContext(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            value = bitmap?.asImageBitmap()
        }
    }.value

}