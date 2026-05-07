package com.example.week02.repository

import com.example.week02.model.UserListResponse
import com.example.week02.model.UserResponse

interface RemoteRepository {
    suspend fun getUserProfile(userId: Int): Result<UserResponse>
    suspend fun getUserList(page: Int): Result<UserListResponse>
}