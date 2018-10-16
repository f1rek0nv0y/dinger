package app.seen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout

internal class SeenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            FrameLayout(context!!).also {
                it.background = ColorDrawable(Color.GREEN)
            }

    companion object {
        fun newInstance() = SeenFragment()
    }
}
