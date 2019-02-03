package app.seen

import android.support.v7.widget.RecyclerView
import android.view.View
import domain.recommendation.DomainRecommendationUser
import kotlinx.android.synthetic.main.item_view_recommendation.view.name
import kotlinx.android.synthetic.main.item_view_recommendation.view.picture

class SeenRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bindTo(item: DomainRecommendationUser) {
    itemView.name.text = item.name
    itemView.picture.loadImage(item.photos.firstOrNull()?.url)
  }

  fun clear() {
    itemView.picture.cancelRequest()
  }
}