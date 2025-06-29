package com.shinjaehun.winternotesroomcompose

import com.shinjaehun.winternotesroomcompose.data.IImageStorage
import com.shinjaehun.winternotesroomcompose.data.ImagePathResult

class FakeImageStorage : IImageStorage {
    override suspend fun saveImageAndThumbnail(bytes: ByteArray): ImagePathResult {
        return ImagePathResult(
            originalPath = "fake/original/path.jpg",
            thumbnailPath = "fake/thumbnail/path.jpg"
        )
    }

    override suspend fun getImage(fileName: String): ByteArray {
        return "fakeImage".toByteArray()
    }

    override suspend fun deleteImage(fileName: String) {
        // no-op
    }
}