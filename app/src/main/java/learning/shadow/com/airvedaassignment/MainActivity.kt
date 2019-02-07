package learning.shadow.com.airvedaassignment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import learning.shadow.com.airvedaassignment.adapter.ConnectivityReceiver
import learning.shadow.com.airvedaassignment.adapter.FeedAdapter
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.model.FeedViewModel
import learning.shadow.com.airvedaassignment.model.FeedWorker
import learning.shadow.com.airvedaassignment.util.isNetworkAvailable
import learning.shadow.com.airvedaassignment.util.showSnack
import learning.shadow.com.airvedaassignment.widget.FeedItemView


class MainActivity : AppCompatActivity(), FeedWorker.FeedHandler, FeedItemView.FeedItemHandler,
    ConnectivityReceiver.ConnectivityReceiverListener {

    companion object {
        const val VALUE_UPDATED = 1
        const val FEED_ID = "FeedId"
        const val FEED_DATA = "Feed"
    }

    private var isUpdateRequired = false;

    private lateinit var feedAdapter: FeedAdapter

    private var feedList = ArrayList<Feed>()

    private lateinit var feedViewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        feedViewModel.allFeeds.observe(this, Observer<List<Feed>> {
            feedList = it as ArrayList<Feed>
            if (feedList.isNullOrEmpty()) {
                isUpdateRequired = true
                if(this.isNetworkAvailable())
                    FeedWorker(this, this).getFeed()
                else
                    showSnack(false, rvFeed)
            } else {
                isUpdateRequired = false
                feedAdapter.setFeedItems(feedItems = feedList)
                rvFeed.adapter = feedAdapter
            }
        }
        )
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this)
        feedAdapter = FeedAdapter(this, this)
        rvFeed.layoutManager = layoutManager

    }


    override fun handleResponse(feeds: List<Feed>) {
        feedList.clear()
        feedViewModel.deleteAll()
        feedList = ArrayList(feeds)
        feedViewModel.insert(feedList)
        feedAdapter.setFeedItems(feedItems = feedList)
        rvFeed.adapter = feedAdapter
    }

    override fun itemTapped(feed: Feed) {
        val intent = Intent(this, FeedDetailActivity::class.java)
        intent.putExtra(FEED_DATA, feed)
        intent.putExtra(FEED_ID, feed.id)
        startActivityForResult(intent, VALUE_UPDATED)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStop() {
        super.onStop()
        feedViewModel.update(feedList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VALUE_UPDATED && resultCode == Activity.RESULT_OK) {
            val feed = data?.extras?.get(FEED_DATA) as Feed
            feed.id = data.extras?.getInt(FEED_ID, -1) ?: -1
            feedViewModel.update(listOf(feed))
            feedAdapter.notifyItemChanged(feedList.indexOf(feed))
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!this.isNetworkAvailable())
            showSnack(isConnected, rvFeed)
        else if(isUpdateRequired){
            FeedWorker(this, this).getFeed()
        }
    }

}
