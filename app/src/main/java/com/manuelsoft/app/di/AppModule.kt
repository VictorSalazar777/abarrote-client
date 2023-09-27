package com.manuelsoft.app.di

import com.manuelsoft.app.CrudProductsUseCase
import com.manuelsoft.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCrudProductsUseCase(repository: Repository): CrudProductsUseCase {
        return CrudProductsUseCase(repository)
    }

}