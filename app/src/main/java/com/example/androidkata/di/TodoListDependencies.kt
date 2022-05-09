package com.example.androidkata.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidkata.repository.RealTodoListRepository
import com.example.androidkata.repository.TodoListRepository
import com.example.androidkata.storage.AppDatabase
import com.example.androidkata.storage.TodoDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TodoListModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoListBindings {
    @Binds
    abstract fun bindTodoListRepository(
        real: RealTodoListRepository
    ): TodoListRepository
}