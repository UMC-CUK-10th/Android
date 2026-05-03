package com.example.week02.network

import com.example.week02.model.UserListResponse
import com.example.week02.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReqResService {

    // 1. 단일 유저 정보 조회 (마이페이지 상단 내 정보)
    @GET("api/users/{id}")
    suspend fun getUserProfile(
        @Path("id") userId: Int
    ): Response<UserResponse>

    // 2. 유저 목록 조회 (마이페이지 하단 팔로잉 리스트)
    @GET("api/users")
    suspend fun getUserList(
        @Query("page") page: Int = 1
    ): Response<UserListResponse>
}