package domain.seen

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import domain.recommendation.DomainRecommendationUser

class SeenRecommendationsViewModel() : ViewModel() {
    private lateinit var list: LiveData<PagedList<DomainRecommendationUser>>

    fun filter() = list

    private fun filterInternal(filter: String) {
        list = LivePagedListBuilder(
                SeenRecommendationsHolder.seenRecommendations.filter(filter),
                PAGE_SIZE)
                .build()
    }
    // TODO Continue here: https://developer.android.com/topic/libraries/architecture/paging/#ex-observe-livedata
}

private const val PAGE_SIZE = 25
