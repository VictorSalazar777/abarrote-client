package com.manuelsoft.repository.di

import com.manuelsoft.data.CategoryEntityDao
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.data.ProductWithCategoryDao
import com.manuelsoft.data.RoomDb
import com.manuelsoft.domain_repository.repository.CategoryRepository
import com.manuelsoft.domain_repository.repository.ProductRepository
import com.manuelsoft.domain_repository.repository.ProductWithCategoryRepository
import com.manuelsoft.domain_repository.repository.RepositoryUpdater
import com.manuelsoft.mylibrary.AbarroteCategoryService
import com.manuelsoft.mylibrary.AbarroteProductService
import com.manuelsoft.repository.impl.CategoryRepositoryImpl
import com.manuelsoft.repository.impl.ProductRepositoryImpl
import com.manuelsoft.repository.impl.ProductWithCategoryRepositoryImpl
import com.manuelsoft.repository.impl.RepositoryUpdaterImpl
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
    fun providesProductRepo(
        productEntityDao: ProductEntityDao,
        abarroteProductService: AbarroteProductService
    ): ProductRepository {
        return ProductRepositoryImpl(productEntityDao, abarroteProductService)
    }

    @Provides
    @Singleton
    fun providesCategoryRepo(
        categoryEntityDao: CategoryEntityDao,
        abarroteCategoryService: AbarroteCategoryService
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryEntityDao, abarroteCategoryService)
    }

    @Provides
    @Singleton
    fun providesProductWithCategoryRepo(productWithCategoryDao: ProductWithCategoryDao):
            ProductWithCategoryRepository {
        return ProductWithCategoryRepositoryImpl(productWithCategoryDao)
    }

    @Provides
    @Singleton
    fun providesRepositoryUpdater(
        abarroteProductService: AbarroteProductService,
        abarroteCategoryService: AbarroteCategoryService,
        productEntityDao: ProductEntityDao,
        categoryEntityDao: CategoryEntityDao,
        productWithCategoryDao: ProductWithCategoryDao,
        roomDb: RoomDb
    ): RepositoryUpdater {
        return RepositoryUpdaterImpl(
            abarroteProductService, abarroteCategoryService, productEntityDao,
            categoryEntityDao, productWithCategoryDao, roomDb
        )
    }


}