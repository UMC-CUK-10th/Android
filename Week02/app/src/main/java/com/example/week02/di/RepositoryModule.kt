package com.example.week02.di

import android.content.Context
import com.example.week02.repository.ProductDataManager
import com.example.week02.repository.LocalRepository
import com.example.week02.repository.LocalRepositoryImpl
import com.example.week02.repository.RemoteRepository
import com.example.week02.repository.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 1. 일반 클래스를 제공하는 모듈 (@Provides 사용)
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideProductDataManager(
        @ApplicationContext context: Context // Hilt가 알아서 Context를 넣어줍니다!
    ): ProductDataManager {
        return ProductDataManager(context)
    }
}

// 2. 인터페이스와 구현체를 연결하는 모듈 (@Binds 사용)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository
}