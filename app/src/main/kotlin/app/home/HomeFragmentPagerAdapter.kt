package app.home

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import app.seen.SeenFragment
import app.tinder.matches.MatchesFragment
import app.tinder.me.MeFragment

internal class HomeFragmentPagerAdapter(supportFragmentManager: FragmentManager)
  : FragmentStatePagerAdapter(supportFragmentManager) {
  override fun getItem(position: Int) = when (position) {
    0 -> MatchesFragment.newInstance()
    1 -> SeenFragment.newInstance()
    2 -> MeFragment.newInstance()
    else -> throw IllegalStateException("Unrecognized index for home pager $position")
  }

  override fun getCount() = CHILD_AMOUNT
}

private const val CHILD_AMOUNT = 3
