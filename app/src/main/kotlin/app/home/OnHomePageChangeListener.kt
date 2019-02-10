package app.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager

internal class OnHomePageChangeListener(private val bottomNavigationView: BottomNavigationView)
  : ViewPager.OnPageChangeListener {
  override fun onPageScrollStateChanged(state: Int) {}

  override fun onPageScrolled(position: Int,
                              positionOffset: Float,
                              positionOffsetPixels: Int) {}

  override fun onPageSelected(position: Int) {
    bottomNavigationView.menu.getItem(position).isChecked = true
  }
}