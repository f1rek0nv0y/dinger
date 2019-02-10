package app.home.seen

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import domain.recommendation.DomainRecommendationUser

internal class SeenRecommendationsViewModelObserver(
    private val adapter: PagedListAdapter<DomainRecommendationUser, *>)
  : Observer<PagedList<DomainRecommendationUser>> {
  override fun onChanged(list: PagedList<DomainRecommendationUser>?) {
    adapter.submitList(list)
  }
}
