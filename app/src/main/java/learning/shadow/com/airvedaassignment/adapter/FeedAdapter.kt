package learning.shadow.com.airvedaassignment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.widget.FeedItemView

class FeedAdapter(
    var context: Context, var itemHandler: FeedItemView.FeedItemHandler,
    var feedItems: ArrayList<Feed>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        setHasStableIds(true)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FeedViewHolder(
            FeedItemView(context).apply {
                this.layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun getItemCount(): Int {
        return feedItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeedViewHolder).bind(feedItems[position], itemHandler)
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(feed: Feed, itemHandler: FeedItemView.FeedItemHandler) {
            feedView.initialize(feed,itemHandler)
        }

        val feedView: FeedItemView = itemView as FeedItemView
    }

}