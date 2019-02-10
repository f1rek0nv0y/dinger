package app.home.me

import android.content.Context
import app.common.di.SchedulersModule
import app.home.HomeScreenScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [MeModule::class, SchedulersModule::class])
@HomeScreenScope
internal interface MeComponent {
  fun inject(target: MeFragment)
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    fun build(): MeComponent
  }
}
