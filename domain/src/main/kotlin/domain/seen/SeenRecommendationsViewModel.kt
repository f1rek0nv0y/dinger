package domain.seen

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder

class SeenRecommendationsViewModel : ViewModel() {
  fun filter(filter: String = "") = LivePagedListBuilder(
      SeenRecommendationsHolder.seenRecommendations.filter(filter), PAGE_SIZE)
      .build()
}

private const val PAGE_SIZE = 25
