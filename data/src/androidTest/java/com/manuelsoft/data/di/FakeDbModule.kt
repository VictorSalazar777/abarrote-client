package com.manuelsoft.data.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.manuelsoft.data.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DbModule::class]
)

object FakeDbModule {

    @Provides
    @Singleton
    fun provideFirebaseReference(): RoomDb {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(appContext, RoomDb::class.java).build()
    }

}