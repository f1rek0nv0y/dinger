package app.seen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.app.seen.SeenAdapter
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
      layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
        it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
      }
      itemAnimator = DefaultItemAnimator()
    }
  }

  companion object {
    fun newInstance() = SeenFragment().also {
      it.retainInstance = true
    }
  }
}
