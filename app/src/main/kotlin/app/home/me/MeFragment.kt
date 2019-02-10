package app.home.me

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_me.logout
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class MeFragment : Fragment() {
  @Inject
  lateinit var logoutCoordinator: LogoutCoordinator

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context != null) {
      inject(context)
    }
  }

  override fun onDetach() {
    super.onDetach()
    logoutCoordinator.actionCancel()
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View =
      inflater.inflate(R.layout.fragment_me, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    logout.setOnClickListener { logoutCoordinator.actionRun() }
  }

  private fun inject(context: Context) = DaggerMeComponent.builder()
      .context(context)
      .build()
      .inject(this)

  companion object {
    fun newInstance() = MeFragment()
  }
}
