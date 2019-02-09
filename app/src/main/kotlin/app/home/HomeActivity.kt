package app.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import app.settings.SettingsActivity
import kotlinx.android.synthetic.main.include_home_pager.home_pager
import kotlinx.android.synthetic.main.include_navigation_bar.navigation_bar
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import org.stoyicker.dinger.R

internal class HomeActivity : AppCompatActivity() {
  val homeComponent: HomeComponent by lazy { DaggerHomeComponent.builder()
      .context(this)
      .build() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setSupportActionBar(toolbar)
    setContentPager()
  }

  override fun onCreateOptionsMenu(menu: Menu) = super.onCreateOptionsMenu(menu).also {
    menuInflater.inflate(R.menu.activity_home, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_settings -> true.also { startActivity(SettingsActivity.getCallingIntent(this)) }
    else -> super.onOptionsItemSelected(item)
  }

  private fun setContentPager() {
    home_pager.adapter = HomeFragmentPagerAdapter(supportFragmentManager)
    home_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {}

      override fun onPageScrolled(position: Int,
                                  positionOffset: Float,
                                  positionOffsetPixels: Int) {
      }

      override fun onPageSelected(position: Int) {
        navigation_bar.menu.getItem(position).isChecked = true
      }
    })
    navigation_bar.setOnNavigationItemSelectedListener {
      home_pager.currentItem = when (it.itemId) {
        R.id.navigation_item_matches -> 0
        R.id.navigation_item_seen -> 1
        R.id.navigation_item_me -> 2
        else -> throw IllegalStateException(
            "Unexpected menu item index selected ${it.itemId}")
      }
      true
    }
  }

  companion object {
    fun getCallingIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }
}
