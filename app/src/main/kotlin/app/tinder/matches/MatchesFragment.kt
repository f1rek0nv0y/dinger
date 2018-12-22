package app.tinder.matches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout

internal class MatchesFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?) = FrameLayout(context!!)

  companion object {
    fun newInstance() = MatchesFragment().also {
      it.retainInstance = true
    }
  }
}
