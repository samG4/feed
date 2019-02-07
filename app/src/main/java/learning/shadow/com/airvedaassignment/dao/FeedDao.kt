package learning.shadow.com.airvedaassignment.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import learning.shadow.com.airvedaassignment.model.Feed

@Dao
interface FeedDao{

    @Insert
    fun insert(feed: List<Feed>)

    @Update
    fun update(feed: List<Feed>)

    @Update
    fun singleFeed(feed: Feed)

    @Query("DELETE FROM FEED_TABLE")
    fun deleteAll()

    @get:Query("SELECT * FROM FEED_TABLE ORDER BY time DESC")
    val feeds: LiveData<List<Feed>>
}