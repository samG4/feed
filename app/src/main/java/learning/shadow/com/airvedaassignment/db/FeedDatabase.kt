package learning.shadow.com.airvedaassignment.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import learning.shadow.com.airvedaassignment.dao.FeedDao
import learning.shadow.com.airvedaassignment.model.Feed

@Database(entities = [Feed::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    companion object {

        private var feedDbInstance: FeedDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FeedDatabase {
            if (feedDbInstance == null) {
                feedDbInstance = Room
                    .databaseBuilder(context.applicationContext, FeedDatabase::class.java, "feed_databse")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return feedDbInstance!!
        }
    }
}