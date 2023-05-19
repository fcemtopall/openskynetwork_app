package com.fcemtopall.openskynetwork.data

import com.fcemtopall.openskynetwork.BuildConfig
import com.fcemtopall.openskynetwork.data.api.ApiService
import com.fcemtopall.openskynetwork.data.remote.RemoteDataSource
import com.fcemtopall.openskynetwork.utils.StringUtils
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier


@Module
@InstallIn(ActivityRetainedComponent::class)
class NetworkModule {

    @Provides
    fun provideApiService(@NoAuthRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }



    @Provides
    @NoAuthRetrofit
    fun provideRetrofit(
        noAuthOkHttpClient: NoAuthOkHttpClient,
        gson: Gson,
        endPoint: EndPoint
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endPoint.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(noAuthOkHttpClient.okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): NoAuthOkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        return provideNoAuthOkHttpClient(builder.build())
    }


    @Provides
    fun provideGson(): Gson {
        return Gson()
    }


    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService,
    ): RemoteDataSource {
        return RemoteDataSource(apiService)
    }


    @Provides
    fun provideEndPoint(): EndPoint {
        return EndPoint(StringUtils.getValueFromLocalProperties("base_url"))
    }

    private fun provideNoAuthOkHttpClient(okHttpClient: OkHttpClient): NoAuthOkHttpClient {
        return NoAuthOkHttpClient(okHttpClient)
    }
}

data class EndPoint(val url: String)


data class NoAuthOkHttpClient(val okHttpClient: OkHttpClient)


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthRetrofit


