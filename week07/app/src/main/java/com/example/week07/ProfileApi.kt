package com.example.week07

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

// 1. Reqres API 응답 데이터 모델
data class ReqresResponse(
    val data: ReqresUser
)

data class ReqresUser(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

// 2. Retrofit API 인터페이스
interface ReqresApi {
    // 1. 내 프로필 정보 (1번 유저)
    @Headers("x-api-key: reqres_0d8cbe4ec5144f4e997b17ac49e99eae")
    @GET("api/users/1")
    suspend fun getUserProfile(): ReqresResponse

    // 2. 팔로잉 리스트 (유저 목록)
    @Headers("x-api-key: reqres_0d8cbe4ec5144f4e997b17ac49e99eae")
    @GET("api/users?page=1") // 1페이지 유저 목록 가져오기
    suspend fun getUserList(): ReqresListResponse
}

// 리스트 응답을 위한 데이터 모델
data class ReqresListResponse(
    val data: List<ReqresUser>
)

// 3. Retrofit 싱글톤 객체
object RetrofitClient {
    // ✨ baseUrl의 끝은 무조건 슬래시(/)로 끝나야 합니다.
    private const val BASE_URL = "https://reqres.in/"

    val api: ReqresApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReqresApi::class.java)
    }
}