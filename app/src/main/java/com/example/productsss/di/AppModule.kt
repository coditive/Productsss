package com.example.productsss.di

import android.content.Context
import androidx.room.Room
import com.example.productsss.BuildConfig
import com.example.productsss.data.local.ProductDB
import com.example.productsss.data.local.ProductListDao
import com.example.productsss.data.remote.ApiService
import com.example.productsss.data.repository.DataRepository
import com.example.productsss.data.repository.DataRepositorySource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDB =
        Room.databaseBuilder(
            context,
            ProductDB::class.java,
            "product-db"
        ).build()


    @Singleton
    @Provides
    fun provideProductListDao(productDB: ProductDB): ProductListDao = productDB.productListDao()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(HttpLoggingInterceptor())
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(httpLoggingInterceptor)
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideApiRequest(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ApiService = Retrofit.Builder()
        .baseUrl("https://app.getswipe.in/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDataRepository(
     productListDao: ProductListDao,
     apiService: ApiService
    ): DataRepositorySource = DataRepository(productListDao, apiService)

}