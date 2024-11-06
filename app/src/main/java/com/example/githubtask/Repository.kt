package com.example.githubtask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class Repository(
    @PrimaryKey val id: Int,
    val name: String,
    val owner: Owner,
    val description: String?,
    val language: String?,
    val stargazers_count: Int
)

data class Owner(
    val login: String,
    val avatar_url: String
)
