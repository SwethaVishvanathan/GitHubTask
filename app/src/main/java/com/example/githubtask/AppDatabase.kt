package com.example.githubtask

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Repository::class], version = 1, exportSchema = false)
@TypeConverters(OwnerTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitHubRepoDao(): RepositoryDao
}
