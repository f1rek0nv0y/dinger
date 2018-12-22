package app.home

import app.di.SchedulerModule
import app.tinder.me.MeComponent
import app.tinder.me.MeModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [MeModule::class, SchedulerModule::class])
@Singleton
internal interface HomeComponent {
  fun newMeComponent(): MeComponent
}
