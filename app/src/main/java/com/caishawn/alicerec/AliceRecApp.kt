package com.caishawn.alicerec

import android.app.Application
import com.caishawn.alicerec.di.appModule
import org.koin.core.context.startKoin

class AliceRecApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AliceRecApp)
            modules(appModule)
        }
    }
}
