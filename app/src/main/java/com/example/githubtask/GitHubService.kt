package com.example.githubtask

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("user/repos")
    fun getUserRepos(): Call<List<Repository>>
    @GET("users/{username}/repos")
    suspend fun listRepositories(@Query("username") username: String): List<Repository>
    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): SearchResponse
}
