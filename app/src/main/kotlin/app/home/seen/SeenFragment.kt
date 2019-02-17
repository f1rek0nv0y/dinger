package app.home.seen

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import domain.recommendation.DomainRecommendationUser
import domain.seen.SeenRecommendationsViewModel
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class SeenFragment : Fragment() {
  @Inject
  lateinit var viewModel: SeenRecommendationsViewModel
  @Inject
  lateinit var observer: Observer<PagedList<DomainRecommendationUser>>
  @Inject
  lateinit var seenAdapter: PagedListAdapter<DomainRecommendationUser, SeenRecommendationViewHolder>
  @Inject
  lateinit var layoutManager: RecyclerView.LayoutManager

  override fun onCreateView(inflater: LayoutInflater,
                            parent: ViewGroup?,
                            savedInstanceState: Bundle?) =
      inflater.inflate(R.layout.fragment_seen, parent, false)!!

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context != null) {
      inject(context)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.filter()
        .observe(this@SeenFragment, observer)
    (view as RecyclerView).apply {
      adapter = seenAdapter
      itemAnimator = null
      layoutManager = this@SeenFragment.layoutManager
    }
  }

  private fun inject(context: Context) =
      DaggerSeenComponent.builder()
          .fragment(this)
          .context(context)
          .build()
          .inject(this)

  companion object {
    fun newInstance() = SeenFragment()
  }
}
