package com.shinjaehun.winternotesroomcompose.data

interface IImageStorage {
    suspend fun saveImageAndThumbnail(bytes: ByteArray): ImagePathResult
    suspend fun getImage(fileName: String): ByteArray
    suspend fun deleteImage(fileName: String)
}