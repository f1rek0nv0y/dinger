package app.home.seen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import app.home.HomeScreenScope
import dagger.Module
import dagger.Provides
import domain.recommendation.DomainRecommendationUser
import domain.seen.SeenRecommendationsViewModel
import org.stoyicker.dinger.R

@Module
internal class SeenModule {
  @Provides
  @HomeScreenScope
  fun viewModel(fragment: Fragment) =
      ViewModelProviders.of(fragment).get(SeenRecommendationsViewModel::class.java)

  @Provides
  @HomeScreenScope
  fun seenAdapter(): PagedListAdapter<DomainRecommendationUser, SeenRecommendationViewHolder> =
      SeenAdapter()

  @Provides
  @HomeScreenScope
  fun observer(adapter: PagedListAdapter<DomainRecommendationUser, SeenRecommendationViewHolder>):
      Observer<PagedList<DomainRecommendationUser>> = SeenRecommendationsViewModelObserver(adapter)

  @Provides
  @HomeScreenScope
  fun layoutManager(context: Context): RecyclerView.LayoutManager = StaggeredGridLayoutManager(
      context.resources.getInteger(R.integer.seen_grid_span), StaggeredGridLayoutManager.VERTICAL).apply {
    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
  }
}
