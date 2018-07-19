package example.test.phong.coffeeapp

import android.app.Application
import example.test.phong.coffeeapp.dagger.AppComponent
import example.test.phong.coffeeapp.dagger.AppModule
import example.test.phong.coffeeapp.dagger.DaggerAppComponent

class App: Application() {
    private lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        // todo use the declare in newer dagger
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        component.inject(this)
    }
}
