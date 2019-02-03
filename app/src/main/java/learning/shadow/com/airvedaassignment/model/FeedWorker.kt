package learning.shadow.com.airvedaassignment.model

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class FeedWorker(val context: Context, val feedHandler: FeedHandler){
    val compositeDisposable = CompositeDisposable()

    companion object {
        const val URL = "https://api.myjson.com/"
    }
    fun getFeed(){
        //create retrofit instance
        val requestInterface = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GetFeed::class.java)
        //send back the list of data
        compositeDisposable?.add(
            requestInterface.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(feedHandler::handleResponse)
        )
    }

    interface FeedHandler{
        fun handleResponse(feeds: List<Feed>)
    }
}

interface GetFeed{
    @GET("bins/bpnll")
    fun getData() : Observable<List<Feed>>
}