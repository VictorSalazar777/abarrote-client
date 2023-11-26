package com.manuelsoft.data.di

import android.content.Context
import androidx.room.Room
import com.manuelsoft.data.CategoryEntityDao
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.data.ProductWithCategoryDao
import com.manuelsoft.data.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): RoomDb {
//        return Room.inMemoryDatabaseBuilder(context, RoomDb::class.java).build()
        return Room.databaseBuilder(
            context = context,
            klass = RoomDb::class.java,
            name = "ProductsDb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: RoomDb): ProductEntityDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: RoomDb): CategoryEntityDao {
        return db.categoryDao()
    }

    @Provides
    @Singleton
    fun provideProductWithCategoryDao(db: RoomDb): ProductWithCategoryDao {
        return db.productWithCategoryDao()
    }

}