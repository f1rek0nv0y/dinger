package app.home.seen

import android.support.v7.widget.RecyclerView
import android.view.View
import domain.recommendation.DomainRecommendationUser
import kotlinx.android.synthetic.main.item_view_recommendation.view.name
import kotlinx.android.synthetic.main.item_view_recommendation.view.picture
import kotlinx.android.synthetic.main.item_view_recommendation.view.teaser
import org.stoyicker.dinger.R

class SeenRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bindTo(item: DomainRecommendationUser) {
    itemView.name.text = item.name
    itemView.teaser.apply {
      val desc = item.teaser.description
      if (desc.isEmpty()) {
        visibility = View.GONE
      } else {
        visibility = View.VISIBLE
        text = desc
      }
    }
    item.photos.firstOrNull()?.url?.let { itemView.picture.loadImage(it, R.drawable.ic_no_profile) }
  }
}