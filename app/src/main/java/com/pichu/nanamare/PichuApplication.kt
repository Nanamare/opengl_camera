package com.pichu.nanamare

import android.app.Application
import com.google.android.play.core.missingsplits.MissingSplitsManagerFactory
import com.google.firebase.FirebaseApp
import com.pichu.nanamare.base.ui.BaseExceptionHandler
import com.pichu.nanamare.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class PichuApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (MissingSplitsManagerFactory.create(this).disableAppIfMissingRequiredSplits()) {
            // Skip app initialization.
            return
        }


        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PichuApplication)
            fragmentFactory()
            modules(
                listOf(
                    networkModule,
                    utilModule,
                    dataSourceModel,
                    viewModelModule,
                    apiModule,
                    cameraModule
                )
            )
        }
        FirebaseApp.initializeApp(this)
        registerActivityLifecycleCallbacks(PichuActivityLifecycleCallback)

        setDefaultHandler()
    }

    private fun setDefaultHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }

        val fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            BaseExceptionHandler(this, defaultExceptionHandler, fabricExceptionHandler)
        )
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(PichuActivityLifecycleCallback)
        super.onTerminate()
    }

    companion object {
        @JvmStatic
        val TAG_APPLICATION: String = PichuApplication::class.java.simpleName
        private val BUILD_TYPE = "debug"
    }

}