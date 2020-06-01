package com.pichu.nanamare.di

import com.pichu.nanamare.BuildConfig
import com.pichu.nanamare.PichuApplication.Companion.TAG_APPLICATION
import com.pichu.nanamare.utils.Logger
import com.pichu.nanamare.utils.PrefUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val DI_RETROFIT = "DI_RETROFIT"
const val DI_RETROFIT_NO_AUTH = "DI_RETROFIT_NO_AUTH"

const val DI_RETROFIT_AUTH_OVERRIDE = "DI_RETROFIT_AUTH_OVERRIDE"
const val DI_RETROFIT_NO_AUTH_OVERRIDE = "DI_RETROFIT_NO_AUTH_OVERRIDE"

val networkModule: Module = module {

    single {
        GsonConverterFactory.create() as Converter.Factory
    }
    single {
        RxJava2CallAdapterFactory.create() as CallAdapter.Factory
    }

    single(named(DI_RETROFIT_AUTH_OVERRIDE)) {
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder().apply {
                    (get(named(DI_PREF_UTILS)) as PrefUtils).loadAccessToken().let {
                        Logger.d(TAG_APPLICATION, "contain access token $it")
                        if (it.isNotEmpty()) {
                            addHeader("Authorization", "Bearer $it")
                        }
                    }
                    addHeader("PICHU-APP-VERSION", BuildConfig.VERSION_NAME)
                }.method(original.method(), original.body()).build()
                it.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .connectTimeout(30_000L, TimeUnit.MILLISECONDS)
            .readTimeout(30_000L, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000L, TimeUnit.MILLISECONDS)
            .build()
    }

    single(named(DI_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(get(named(DI_RETROFIT_AUTH_OVERRIDE)))
            .addConverterFactory(get())
            .addCallAdapterFactory(get())
            .build()
    }


    single(named(DI_RETROFIT_NO_AUTH_OVERRIDE)) {
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder().apply {
                    // TODO add header
                }.method(original.method(), original.body()).build()
                it.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .connectTimeout(30_000L, TimeUnit.MILLISECONDS)
            .readTimeout(30_000L, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000L, TimeUnit.MILLISECONDS)
            .build()
    }

    single(named(DI_RETROFIT_NO_AUTH)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(get(named(DI_RETROFIT_NO_AUTH_OVERRIDE)))
            .addConverterFactory(get())
            .addCallAdapterFactory(get())
            .build()
    }

}
