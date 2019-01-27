package app.seen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import domain.recommendation.DomainRecommendationUser
import domain.seen.SeenRecommendationsViewModel
import org.stoyicker.dinger.R

internal class SeenFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater,
                            parent: ViewGroup?,
                            savedInstanceState: Bundle?) =
      inflater.inflate(R.layout.fragment_seen, parent, false)!!

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val seenAdapter = SeenAdapter()
    ViewModelProviders.of(this).get(SeenRecommendationsViewModel::class.java).filter().observe(
        this@SeenFragment, Observer<PagedList<DomainRecommendationUser>> {
      seenAdapter.submitList(it)
    })
    (view as RecyclerView).apply {
      adapter = seenAdapter
      layoutManager = StaggeredGridLayoutManager(
          context.resources.getInteger(R.integer.seen_grid_span), StaggeredGridLayoutManager.VERTICAL).apply {
        gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
      }
    }
  }

  companion object {
    fun newInstance() = SeenFragment()
  }
}
