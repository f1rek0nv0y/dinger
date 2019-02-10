package app.home

import android.content.Context
import app.common.di.SchedulerModule
import app.seen.SeenComponent
import app.tinder.me.MeComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [SchedulerModule::class])
@Singleton
internal interface HomeComponent {
  fun newSeenComponentBuilder(): SeenComponent.Builder
  fun newMeComponent(): MeComponent
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    fun build(): HomeComponent
  }
}
