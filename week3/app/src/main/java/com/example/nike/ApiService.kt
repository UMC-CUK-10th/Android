package com.example.nike

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 서버 응답을 담을 데이터 클래스
data class UserResponse(val data: UserData)
data class UserListResponse(val data: List<UserData>)
data class UserData(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

interface ApiService {
    // 1번 유저 정보 가져오기 (api/users/1)
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: Int): UserResponse

    // 전체 유저 리스트 가져오기 (api/users?page=1)
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int = 1): UserListResponse
}