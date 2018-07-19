package example.test.phong.coffeeapp.dagger

import dagger.Component
import dagger.Module
import dagger.Provides
import example.test.phong.coffeeapp.App
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        MainActivityModule::class,
        ViewModelMapModule::class,
        ViewModelProvidersModule::class
))
interface AppComponent {
    fun inject(app: App)
}

@Module
class ViewModelProvidersModule {

}

@Module
class ViewModelMapModule {

}

@Module
class MainActivityModule {

}

@Module
class AppModule(private val app: App) {
    @Provides
    fun providesApp() = app
}
