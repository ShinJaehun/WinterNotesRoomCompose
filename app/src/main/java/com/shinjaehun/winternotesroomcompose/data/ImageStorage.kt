package com.shinjaehun.winternotesroomcompose.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageStorage (
    private val context: Context
) {
//    suspend fun saveImage(bytes: ByteArray): String {
//        return withContext(Dispatchers.IO) {
//            val fileName = UUID.randomUUID().toString() + ".jpg"
//            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
//                outputStream.write(bytes)
//            }
//            fileName
//        }
//    }

//    suspend fun saveImageAndThumbnail(originalUri: Uri, context: Context): Pair<String, String>? {
//        val resolver = context.contentResolver
//        val inputStream = resolver.openInputStream(originalUri) ?: return null
//
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//
//        // 1. 원본 이미지 저장
//        val imageFileName = "note_image_${System.currentTimeMillis()}.jpg"
//        val imageFile = File(context.filesDir, imageFileName)
//        withContext(Dispatchers.IO) {
//            FileOutputStream(imageFile).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
//            }
//        }
//
//        // 2. 썸네일 생성 및 저장 (예: 100x100 픽셀)
//        val thumbnail = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
//        val thumbFileName = "thumb_${imageFileName}"
//        val thumbFile = File(context.filesDir, thumbFileName)
//        withContext(Dispatchers.IO) {
//            FileOutputStream(thumbFile).use { out ->
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, out)
//            }
//        }
//
//        return Pair(imageFile.absolutePath, thumbFile.absolutePath)
//    }

    suspend fun saveImageAndThumbnail(bytes: ByteArray): ImagePathResult {
        return withContext(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            val imageFileName = "note_image_${System.currentTimeMillis()}.jpg"
            val imageFile = File(context.filesDir, imageFileName)
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(bytes)
            }

            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            val thumbnail = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
            val thumbFileName = "thumb_${imageFileName}"
            val thumbFile = File(context.filesDir, thumbFileName)

            FileOutputStream(thumbFile).use { out ->
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }

            ImagePathResult(imageFile.absolutePath, thumbFile.absolutePath)
        }
    }

    suspend fun getImage(fileName: String): ByteArray {
//        return withContext(Dispatchers.IO) {
//            context.openFileInput(fileName).use { inputStream ->
//                inputStream.readBytes()
//            }
//        }
        return withContext(Dispatchers.IO) {
            val file = File(fileName)
            if (!file.exists()) {
                ByteArray(0)
            } else {
                file.inputStream().use { it.readBytes() }
            }
        }
    }

    suspend fun deleteImage(fileName: String) {
        return withContext(Dispatchers.IO) {
//            context.deleteFile(fileName)
            val file = File(fileName)
            file.delete()
        }
    }
}

data class ImagePathResult(
    val originalPath: String?,
    val thumbnailPath: String?
)