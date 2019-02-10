package app.home

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import app.settings.SettingsActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class HomeModule {
  @Provides
  @Singleton
  fun pagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter =
      HomeFragmentPagerAdapter(fragmentManager)

  @Provides
  @Singleton
  fun onHomePageChangeListener(bottomNavigationView: BottomNavigationView)
      : ViewPager.OnPageChangeListener = OnHomePageChangeListener(bottomNavigationView)

  @Provides
  @Singleton
  fun onHomePageSelectedListener(viewPager: ViewPager):
      BottomNavigationView.OnNavigationItemSelectedListener = OnHomePageSelectedListener(viewPager)

  @Provides
  @Singleton
  fun goToSettingsIntent(context: Context) = SettingsActivity.getCallingIntent(context)
}
