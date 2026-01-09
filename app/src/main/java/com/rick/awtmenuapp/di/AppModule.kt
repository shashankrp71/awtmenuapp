package com.rick.awtmenuapp.di

import com.rick.awtmenuapp.data.remote.ApiConstants
import com.rick.awtmenuapp.data.remote.ApiService
import com.rick.awtmenuapp.data.remote.repository.NavigationRepositoryImpl
import com.rick.awtmenuapp.domain.repository.NavigationRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigationRepository(apiService: ApiService): NavigationRepository{
        return NavigationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService{
        return retrofit
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(moshi: Moshi): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi{
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
}