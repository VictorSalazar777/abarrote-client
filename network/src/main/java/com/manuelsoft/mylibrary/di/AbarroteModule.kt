package com.manuelsoft.mylibrary.di

import com.manuelsoft.mylibrary.AbarroteCategoryService
import com.manuelsoft.mylibrary.AbarroteProductService
import com.manuelsoft.mylibrary.ResultErrorHandlingAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AbarroteModule {

    private const val productsUrl = "http://192.168.1.3:8080/abarrote/"

    @Singleton
    @Provides
    fun provideAbarroteProductService(retrofit: Retrofit): AbarroteProductService {
        return retrofit.create(AbarroteProductService::class.java)
    }

    @Singleton
    @Provides
    fun provideAbarroteCategoryService(retrofit: Retrofit): AbarroteCategoryService {
        return retrofit.create(AbarroteCategoryService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(productsUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ResultErrorHandlingAdapter())
            .build()
    }

}
