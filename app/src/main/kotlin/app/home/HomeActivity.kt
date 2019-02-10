package app.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.include_home_pager.home_pager
import kotlinx.android.synthetic.main.include_navigation_bar.navigation_bar
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class HomeActivity : AppCompatActivity() {
  @Inject
  lateinit var pagerAdapter: FragmentStatePagerAdapter
  @Inject
  lateinit var viewPagerOnPageChangeListener: ViewPager.OnPageChangeListener
  @Inject
  lateinit var onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
  @Inject
  lateinit var goToSettingsIntent: Intent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setSupportActionBar(toolbar)
    inject()
    setContentPager()
  }

  override fun onCreateOptionsMenu(menu: Menu) = super.onCreateOptionsMenu(menu).also {
    menuInflater.inflate(R.menu.activity_home, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_settings -> true.also { startActivity(goToSettingsIntent) }
    else -> super.onOptionsItemSelected(item)
  }

  private fun setContentPager() {
    home_pager.adapter = pagerAdapter
    home_pager.addOnPageChangeListener(viewPagerOnPageChangeListener)
    navigation_bar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
  }

  private fun inject() = DaggerHomeComponent.builder()
      .fragmentManager(supportFragmentManager)
      .bottomNavigationView(navigation_bar)
      .viewPager(home_pager)
      .context(this)
      .build()
      .inject(this)

  companion object {
    fun getCallingIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }
}
