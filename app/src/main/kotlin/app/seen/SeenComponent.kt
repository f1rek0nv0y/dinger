package app.seen

import android.support.v4.app.Fragment
import app.home.HomeScreenScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SeenModule::class])
@HomeScreenScope
internal interface SeenComponent {
  fun inject(target: SeenFragment)
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance fun fragment(fragment: Fragment): Builder
    fun build(): SeenComponent
  }
}
