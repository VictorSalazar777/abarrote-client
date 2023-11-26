package com.manuelsoft.app.di

import com.manuelsoft.domain_impl.di.UseCasesModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module(
    includes = [
        UseCasesModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {


}