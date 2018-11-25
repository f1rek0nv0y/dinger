package app.app.seen

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import domain.recommendation.DomainRecommendationUser

class SeenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(item: DomainRecommendationUser) {
        itemView.visibility = View.VISIBLE
        (itemView as TextView).text = item.name
        // TODO Set attributes to values and progress to GONE
    }

    fun clear() {
        itemView.visibility = View.GONE
        // TODO Set attributes to empty and progress to VISIBLE
    }
}
