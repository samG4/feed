package learning.shadow.com.airvedaassignment.repo

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import learning.shadow.com.airvedaassignment.dao.FeedDao
import learning.shadow.com.airvedaassignment.db.FeedDatabase
import learning.shadow.com.airvedaassignment.model.Feed

class FeedRepository(application: Application) {

    private val feedDatabase = FeedDatabase.getInstance(application)
    private val feedDao: FeedDao by lazy {
        feedDatabase.feedDao()
    }
    val feeds: LiveData<List<Feed>> by lazy {
        feedDao.feeds
    }

    fun insert(feed: List<Feed>) {
        InsertFeed(feedDao).execute(feed)
    }

    fun update(feed: List<Feed>) {
        UpdateFeed(feedDao).execute(feed)
    }

    fun deleteAll(){
        DeleteFeed(feedDao).execute()
    }

    companion object {
        private class InsertFeed(val feedDao: FeedDao) : AsyncTask<List<Feed>, Void, Void>() {
            override fun doInBackground(vararg params: List<Feed>?): Void? {
                params[0]?.let {
                    feedDao.insert(it)
                }
                return null
            }
        }

        private class UpdateFeed(val feedDao: FeedDao) : AsyncTask<List<Feed>, Void, Void>() {
            override fun doInBackground(vararg params: List<Feed>?): Void? {
                params[0]?.let {
                    if (it.size == 1) {
                        feedDao.singleFeed(it[0])
                    } else {
                        feedDao.update(it)
                    }
                }
                return null
            }
        }

        private class DeleteFeed(val feedDao: FeedDao) : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                feedDao.deleteAll()
                return null
            }
        }
    }
}
