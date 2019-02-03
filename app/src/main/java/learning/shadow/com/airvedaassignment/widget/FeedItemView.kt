package learning.shadow.com.airvedaassignment.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feed.view.*
import learning.shadow.com.airvedaassignment.R
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.util.convertDpToPx
import learning.shadow.com.airvedaassignment.util.gone
import learning.shadow.com.airvedaassignment.util.show

class FeedItemView(context: Context): FrameLayout(context), View.OnClickListener{

    init {
        LayoutInflater.from(context).inflate(R.layout.feed, this, true)
    }
    private lateinit var feedData  : Feed
    private var itemHandler: FeedItemHandler ?= null
    private val imgPlaceHeight = convertDpToPx(context, 200F)
    fun initialize(data: Feed, itemHandler: FeedItemHandler? ){
        this.feedData = data
        this.itemHandler = itemHandler
        setData()
        btnLike.setOnClickListener(this)
        btnUnlike.setOnClickListener(this)
        this.setOnClickListener {
            itemHandler?.itemTapped(feedData)
        }
    }
    private fun setData(){
        if(feedData.showTime){
            tvTime.text =feedData.time.toString()
            tvTime.show()
        }else{
            tvTime.gone()
        }
        tvTitle.text = feedData.title
        if(!feedData.imageUrl.isNullOrEmpty() && !feedData.text.isNullOrEmpty()){
            Log.d("***1","${feedData.imageUrl}")
            tvQuote.gone()
            imgPlace.gone()
            profileContainer.show()
            tvProfile.text = feedData.text
            Picasso.get()
                .load(feedData.imageUrl)
                .into(imgProfile)
        }
        else if(!feedData.imageUrl.isNullOrEmpty()){
            profileContainer.gone()
            tvQuote.gone()
            imgPlace.show()
            Log.d("***2","${feedData.imageUrl}")
            Picasso.get()
                .load(feedData.imageUrl)
                .resize(imgPlaceHeight.toInt(),imgPlaceHeight.toInt())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(imgPlace)
        }
        else{
            profileContainer.gone()
            tvQuote.show()
            imgPlace.gone()
            tvQuote.text = feedData.text
            imgPlace.setImageDrawable(null)
            imgProfile.setImageDrawable(null)
        }
        tvFrom.text = (context.getString(R.string.From)+ " " + feedData.name)
        if(feedData.isLiked){
            btnUnlike.show()
            btnLike.gone()
        }
        else{
            btnLike.show()
            btnUnlike.gone()
        }

    }
    override fun onClick(v: View?) {
        when(v){
            btnLike->{
                btnLike.gone()
                btnUnlike.show()
                feedData.isLiked = true
            }
            btnUnlike->{
                btnLike.show()
                btnUnlike.gone()
                feedData.isLiked = false
            }
        }
    }

    interface FeedItemHandler{
        fun itemTapped(feed: Feed)
    }

}