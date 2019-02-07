package learning.shadow.com.airvedaassignment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.widget.FeedItemView

class FeedAdapter(
    var context: Context, var itemHandler: FeedItemView.FeedItemHandler
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var feedItems: ArrayList<Feed>

    init {
        setHasStableIds(true)
    }

    fun setFeedItems(feedItems: ArrayList<Feed>){
        this.feedItems = groupFeedList(feedItems)
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

        private val feedView: FeedItemView = itemView as FeedItemView
    }

    private fun groupFeedList(feedList: MutableList<Feed>): ArrayList<Feed> {
        val map = HashMap<Long, ArrayList<Feed>>()
        feedList.forEach {
            if (!map.containsKey(it.time)) {
                val list = ArrayList<Feed>()
                list.add(it)
                map[it.time] = list
            } else {
                map[it.time]?.add(it)
            }
        }

        map.forEach {
            if (it.value.size > 1) {
                it.value[0].showTime = true
                for (i in 1 until it.value.size) {
                    it.value[i].showTime = false
                }
            } else {
                it.value[0].showTime = true
            }
        }
        val finalList = ArrayList<Feed>()
        map.toSortedMap(compareByDescending { it }).values.forEach {
            finalList.addAll(it)
        }
        return finalList
    }

}