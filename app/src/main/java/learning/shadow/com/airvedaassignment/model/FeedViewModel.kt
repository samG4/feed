package learning.shadow.com.airvedaassignment.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import learning.shadow.com.airvedaassignment.repo.FeedRepository

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FeedRepository by lazy {
        FeedRepository(application)
    }
    val allFeeds: LiveData<List<Feed>> by lazy {
        repository.feeds
    }

    fun insert(feed: List<Feed>) {
        repository.insert(feed)
    }

    fun update(feed: List<Feed>) {
        repository.update(feed)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

}