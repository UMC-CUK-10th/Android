package com.example.week02.repository

import com.example.week02.network.ReqResService
import com.example.week02.model.UserListResponse
import com.example.week02.model.UserResponse
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val reqResService: ReqResService
) : RemoteRepository {
    override suspend fun getUserProfile(userId : Int): Result<UserResponse>{
        return try {
            val response = reqResService.getUserProfile(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("유저 정보 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getUserList(page: Int): Result<UserListResponse> {
        return try {
            val response = reqResService.getUserList(page)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("유저 리스트 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}