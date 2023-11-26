package com.manuelsoft.domain_impl.di

import com.manuelsoft.domain_impl.usecase.CrudCategoriesUseCaseImpl
import com.manuelsoft.domain_impl.usecase.CrudProductsUseCaseImpl
import com.manuelsoft.domain_impl.usecase.UpdaterUseCaseImpl
import com.manuelsoft.domain_repository.repository.CategoryRepository
import com.manuelsoft.domain_repository.repository.ProductRepository
import com.manuelsoft.domain_repository.repository.ProductWithCategoryRepository
import com.manuelsoft.domain_repository.repository.RepositoryUpdater
import com.manuelsoft.domain_usecases.usecases.CrudCategoriesUseCase
import com.manuelsoft.domain_usecases.usecases.CrudProductsUseCase
import com.manuelsoft.domain_usecases.usecases.UpdaterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun providesCategoriesUseCase(categoryRepository: CategoryRepository): CrudCategoriesUseCase {
        return CrudCategoriesUseCaseImpl(categoryRepository)
    }

    @Provides
    @Singleton
    fun providesProductsUseCase(
        productRepository: ProductRepository,
        productWithCategoryRepository: ProductWithCategoryRepository
    ): CrudProductsUseCase {
        return CrudProductsUseCaseImpl(productWithCategoryRepository, productRepository)
    }

    @Provides
    @Singleton
    fun providesUpdaterUsecase(repositoryUpdater: RepositoryUpdater): UpdaterUseCase {
        return UpdaterUseCaseImpl(repositoryUpdater)
    }

}