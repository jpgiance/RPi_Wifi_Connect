package com.jorgegiance.rpiwificonnect.di

import android.content.Context
import com.jorgegiance.rpiwificonnect.ble.BLEController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideBleController(@ApplicationContext context: Context): BLEController{
        return BLEController(context)
    }
}