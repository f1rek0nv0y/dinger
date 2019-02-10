package app.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.MenuItem
import org.stoyicker.dinger.R

internal class OnHomePageSelectedListener(private val homePager: ViewPager)
  : BottomNavigationView.OnNavigationItemSelectedListener {
  override fun onNavigationItemSelected(menuItem: MenuItem) = true.also {
    homePager.currentItem = when (menuItem.itemId) {
      R.id.navigation_item_matches -> 0
      R.id.navigation_item_seen -> 1
      R.id.navigation_item_me -> 2
      else -> throw IllegalStateException("Unexpected menu item index selected ${menuItem.itemId}")
    }
  }
}