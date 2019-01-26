package app.seen

import android.support.v7.widget.RecyclerView
import android.view.View
import domain.recommendation.DomainRecommendationUser
import kotlinx.android.synthetic.main.item_view_recommendation.view.name
import kotlinx.android.synthetic.main.item_view_recommendation.view.picture

class SeenRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bindTo(item: DomainRecommendationUser) {
    itemView.visibility = View.VISIBLE
    itemView.name.text = item.name
    itemView.picture.loadImage(item.photos.first().url)
    // TODO Set attributes to values and progress to GONE
  }

  fun clear() {
    itemView.visibility = View.GONE
    itemView.name.text = ""
    itemView.picture.clear()
    // TODO Set attributes to empty and progress to VISIBLE
  }
}