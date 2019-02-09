package app.tinder.me

import app.home.HomeScreenScope
import dagger.Subcomponent

@Subcomponent(modules = [MeModule::class])
@HomeScreenScope
internal interface MeComponent {
  fun inject(target: MeFragment)
}
