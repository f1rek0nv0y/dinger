package app.home.seen

import android.content.Context
import android.support.v4.app.Fragment
import app.home.HomeScreenScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [SeenModule::class])
@HomeScreenScope
internal interface SeenComponent {
  fun inject(target: SeenFragment)
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    @BindsInstance
    fun fragment(fragment: Fragment): Builder
    fun build(): SeenComponent
  }
}
