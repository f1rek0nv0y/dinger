package app.home

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [HomeModule::class])
@Singleton
internal interface HomeComponent {
  fun inject(target: HomeActivity)
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun fragmentManager(fragmentManager: FragmentManager): Builder
    @BindsInstance
    fun bottomNavigationView(bottomNavigationView: BottomNavigationView): Builder
    @BindsInstance
    fun viewPager(viewPager: ViewPager): Builder
    @BindsInstance
    fun context(context: Context): Builder
    fun build(): HomeComponent
  }
}
