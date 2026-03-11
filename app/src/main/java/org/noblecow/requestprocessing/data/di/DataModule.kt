package org.noblecow.requestprocessing.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.noblecow.requestprocessing.data.MockRequestService
import org.noblecow.requestprocessing.data.RequestService

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindRequestService(mockRequestService: MockRequestService): RequestService
}
