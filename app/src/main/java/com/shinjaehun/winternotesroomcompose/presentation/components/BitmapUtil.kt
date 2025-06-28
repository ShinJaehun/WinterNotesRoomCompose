package com.shinjaehun.winternotesroomcompose.presentation.components

import android.graphics.Bitmap
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

//    if (bytes == null) return null
//
//    return produceState<ImageBitmap?>(initialValue = null, key1 = bytes.contentHashCode()) {
//        withContext(Dispatchers.IO) {
//            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            value = bitmap?.asImageBitmap()
//        }
//    }.value

    return produceState<ImageBitmap?>(initialValue = null, key1 = bytes?.contentHashCode()) {
        if (bytes != null) {
            withContext(Dispatchers.IO) {
                val sampledBitmap = decodeSampledBitmapFromBytes(bytes, reqWidth = 300, reqHeight = 300)
                value = sampledBitmap.asImageBitmap()
            }
        }
    }.value
}

fun decodeSampledBitmapFromBytes(bytes: ByteArray, reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}