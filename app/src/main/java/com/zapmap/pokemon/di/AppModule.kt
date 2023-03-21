package com.zapmap.pokemon.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zapmap.pokemon.BuildConfig
import com.zapmap.pokemon.api.datasource.NetworkDataSource
import com.zapmap.pokemon.api.datasource.NetworkDataSourceImpl
import com.zapmap.pokemon.api.service.ApiService
import com.zapmap.pokemon.repository.DefaultPokemonRepository
import com.zapmap.pokemon.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesPokemonListRepository(apiService: ApiService): PokemonRepository {
        return DefaultPokemonRepository(networkDataSource = NetworkDataSourceImpl(apiService = apiService))
    }

    @Provides
    @Singleton
    fun providesNetworkDataSource(apiService: ApiService): NetworkDataSource {
        return NetworkDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        moshi: Moshi,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_API)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .apply {
                addInterceptor(loggingInterceptor)
            }.build()
    }


}