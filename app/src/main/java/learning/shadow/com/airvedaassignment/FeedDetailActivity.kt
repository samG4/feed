package learning.shadow.com.airvedaassignment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_feed_detail.*
import kotlinx.android.synthetic.main.feed.*
import kotlinx.android.synthetic.main.feed.view.*
import learning.shadow.com.airvedaassignment.adapter.ConnectivityReceiver
import learning.shadow.com.airvedaassignment.model.Feed
import learning.shadow.com.airvedaassignment.util.gone
import learning.shadow.com.airvedaassignment.util.show
import learning.shadow.com.airvedaassignment.util.showSnack

class FeedDetailActivity : AppCompatActivity(), View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private val feed : Feed by lazy{
        intent.extras.get(MainActivity.FEED_DATA) as Feed
    }
    private val id : Int by lazy{
        intent.extras.getInt(MainActivity.FEED_ID, -1)
    }

    private var isChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null){
            feed.isLiked = savedInstanceState.getBoolean("LIKED_STATUS")
            isChanged = savedInstanceState.getBoolean("CHANGED_STATUS")
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)
        includedFeed.btnUnlike.setOnClickListener(this)
        includedFeed.btnLike.setOnClickListener(this)
        decideViews()
        includedFeed.show()
        tvDescription.text = feed.description
    }

    private fun decideViews(){
        if(!feed.imageUrl.isNullOrEmpty() && !feed.text.isNullOrEmpty()){
            includedFeed.profileContainer.show()
            Picasso.get().load(feed.imageUrl).into(includedFeed.imgProfile)
            includedFeed.tvProfile.text = feed.text
            includedFeed.imgPlace.gone()
            includedFeed.tvQuote.gone()
        }
        else if(feed.text.isNullOrEmpty()){
            includedFeed.imgPlace.show()
            includedFeed.profileContainer.gone()
            includedFeed.tvQuote.gone()
            Picasso.get().load(feed.imageUrl).into(includedFeed.imgPlace)
        }
        else{
            includedFeed.imgPlace.gone()
            includedFeed.profileContainer.gone()
            includedFeed.tvQuote.show()
            includedFeed.tvQuote.text = feed.text
        }
        includedFeed.tvTime.gravity = Gravity.CENTER
        includedFeed.tvTime.text = feed.title
        includedFeed.tvTitle.gone()
        if(feed.isLiked){
            includedFeed.btnUnlike.show()
            includedFeed.btnLike.gone()
        }
        else{
            includedFeed.btnLike.show()
            includedFeed.btnUnlike.gone()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected,detail)
    }

    override fun onResume() {
        super.onResume()
        MainApplication.getInstance().setConnectivityListener(this)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean("LIKED_STATUS", feed.isLiked)
        savedInstanceState.putBoolean("CHANGED_STATUS", isChanged)
    }

    override fun onClick(v: View?) {
        when(v){
            includedFeed.btnUnlike->{
                btnLike.show()
                btnUnlike.gone()
                feed.isLiked = false
            }
            includedFeed.btnLike->{
                btnLike.gone()
                btnUnlike.show()
                feed.isLiked = true
            }
        }
        isChanged=!isChanged
    }

    override fun onBackPressed() {
        if(isChanged){
            val returnIntent = Intent()
            returnIntent.putExtra(MainActivity.FEED_DATA, feed)
            returnIntent.putExtra(MainActivity.FEED_ID,id)
            setResult(Activity.RESULT_OK, returnIntent)
        }
        super.onBackPressed()
        finish()
    }
}
