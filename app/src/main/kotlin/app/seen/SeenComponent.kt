package app.seen

import app.home.HomeScreenScope
import dagger.Subcomponent

@Subcomponent(modules = [SeenModule::class])
@HomeScreenScope
internal interface SeenComponent {
  fun inject(target: SeenFragment)
}
