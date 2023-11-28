package com.azaiskr.githubuserprofile.data.remote.api

import com.azaiskr.githubuserprofile.data.remote.response.GitHubResponse
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.data.remote.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("search/users")
    fun getGithubUser(
        @Query("q")
        q: String
    ): Call<GitHubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username")
        username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username")
        username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username")
        username: String
    ): Call<List<ItemsItem>>

}