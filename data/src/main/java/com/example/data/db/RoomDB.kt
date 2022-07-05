package com.example.data.db

import android.content.Context
import androidx.room.Room

fun provideDB(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME).build()

fun provideHistoryDao(database: AppDatabase) = database.historyDao()