package com.shinjaehun.winternotesroomcompose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.shinjaehun.winternotesroomcompose.data.IImageStorage
import com.shinjaehun.winternotesroomcompose.data.ImageStorage
import com.shinjaehun.winternotesroomcompose.data.NoteDatabase
import com.shinjaehun.winternotesroomcompose.data.NoteRepositoryImpl
import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageStorage(@ApplicationContext context: Context): IImageStorage {
        return ImageStorage(context)
    }

//    @Provides
//    @Singleton
//    fun provideNoteDataRepository(db: NoteDatabase, @ApplicationContext context: Context): INoteRepository {
//        return NoteRepositoryImpl(
//            dao = db.noteDao,
//            imageStorage = ImageStorage(context = context)
//        )
//    }

    @Provides
    @Singleton
    fun provideNoteDataRepository(
        db: NoteDatabase,
        imageStorage: IImageStorage
    ): INoteRepository {
        return NoteRepositoryImpl(
            dao = db.noteDao,
            imageStorage = imageStorage
        )
    }
}