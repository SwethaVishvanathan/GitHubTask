package com.example.githubtask

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM Repositories")
    suspend fun getAllRepositories(): List<Repository>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<Repository>)
}
