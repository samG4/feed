package learning.shadow.com.airvedaassignment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import learning.shadow.com.airvedaassignment.adapter.ConnectivityReceiver
import learning.shadow.com.airvedaassignment.adapter.FeedAdapter
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.model.FeedWorker
import learning.shadow.com.airvedaassignment.util.isNetworkAvailable
import learning.shadow.com.airvedaassignment.util.showSnack
import learning.shadow.com.airvedaassignment.widget.FeedItemView
import java.io.File
import java.io.FileInputStream


class MainActivity(var file: File? = null) : AppCompatActivity(), FeedWorker.FeedHandler, FeedItemView.FeedItemHandler, ConnectivityReceiver.ConnectivityReceiverListener {
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected, rvFeed)
    }

    private lateinit var feedAdapter: FeedAdapter
    private val dir: File by lazy{
        File(this.filesDir, "FEED")
    }
    private var feedList = ArrayList<Feed>() as MutableList<Feed>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dir.mkdirs()
        file = File(dir, "Records.json")
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        rvFeed.layoutManager= layoutManager
        val feedContent = readFile()
        try {
            feedContent?.let {
                if (!TextUtils.isEmpty(it) && it.toCharArray().size>2) {
                    val classType = object : TypeToken<List<Feed>>() {
                    }.type
                    feedList = Gson().fromJson(feedContent, classType)
                    groupFeedList(feedList)
                    feedAdapter = feedList?.let {
                        FeedAdapter(this, this, it as ArrayList<Feed>)
                    }
                    rvFeed.adapter = feedAdapter
                }
                else{
                    if(this.isNetworkAvailable())
                        FeedWorker(this, this).getFeed()
                    else{
                        showSnack(false,rvFeed)
                    }
                }
            }
        }catch (e: Exception){
            Toast.makeText(this,"Something Wrong Happended", Toast.LENGTH_LONG).show()
        }

    }

    override fun handleResponse(feeds: List<Feed>) {
        feedList = ArrayList(feeds)
        groupFeedList(feedList)
        writeToFile(Gson().toJson(feeds))
        feedAdapter = feedList?.let {
            FeedAdapter(this, this, it as ArrayList<Feed>)
        }
        rvFeed.adapter = feedAdapter
    }

    private fun groupFeedList(feedList: MutableList<Feed>):List<Feed> {
        val map= HashMap<Long, ArrayList<Feed>>()
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
            if(it.value.size>1){
                it.value[0].showTime = true
                for(i in 1 until it.value.size){
                    it.value[i].showTime= false
                }
            }
            else{
                it.value[0].showTime = true
            }
        }
        val finalList = ArrayList<Feed>()
        map.values.forEach {
            finalList.addAll(it)
        }
        return finalList
    }

    override fun itemTapped(feed: Feed) {
        val intent = Intent(this, FeedDetailActivity::class.java)
        intent.putExtra("FEED",feed)
        startActivity(intent)
    }

    private fun writeToFile(fileContents: String) {
        Observable.fromCallable<Any> {
            file?.writeText(fileContents)
            0
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun onResume() {
        super.onResume()
        MainApplication.getInstance().setConnectivityListener(this)
    }

    private fun readFile(): String? {
        file?.let {
            if(it.exists())
                return FileInputStream(file).bufferedReader().use { it.readText() }
        }
        return null

    }

    override fun onStop() {
        super.onStop()
        writeToFile(Gson().toJson(feedList))
    }
}
