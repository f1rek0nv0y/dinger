package app.seen

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import app.home.HomeScreenScope
import dagger.Module
import dagger.Provides
import domain.recommendation.DomainRecommendationUser
import org.stoyicker.dinger.R

@Module
internal class SeenModule {
  @Provides
  @HomeScreenScope
  fun seenAdapter(): PagedListAdapter<DomainRecommendationUser, SeenRecommendationViewHolder> =
      SeenAdapter()

  @Provides
  @HomeScreenScope
  fun layoutManager(context: Context): RecyclerView.LayoutManager = StaggeredGridLayoutManager(
      context.resources.getInteger(R.integer.seen_grid_span), StaggeredGridLayoutManager.VERTICAL).apply {
    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
  }
}
