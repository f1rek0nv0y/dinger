package app.tinder.me

import app.di.PerActivity
import dagger.Subcomponent

@Subcomponent(modules = [MeModule::class])
@PerActivity
internal interface MeComponent {
  fun inject(target: MeFragment)
}
