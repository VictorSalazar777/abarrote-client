package com.manuelsoft.repository.di

import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.repository.Repository
import com.manuelsoft.repository.impl.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun providesRepo(productEntityDao: ProductEntityDao): Repository {
        return RepositoryImpl(productEntityDao)
    }


}