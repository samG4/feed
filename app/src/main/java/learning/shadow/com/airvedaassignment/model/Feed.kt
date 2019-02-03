package learning.shadow.com.airvedaassignment.model

import android.os.Parcel
import android.os.Parcelable

data class Feed(
    var description: String,
    var imageUrl: String?="",
    var name: String,
    var text: String?="",
    var time: Long,
    var title: String,
    var showTime: Boolean = true,
    var isLiked: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeLong(time)
        parcel.writeString(title)
        parcel.writeValue(showTime)
        parcel.writeValue(isLiked)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Feed> {
        override fun createFromParcel(parcel: Parcel): Feed {
            return Feed(parcel)
        }

        override fun newArray(size: Int): Array<Feed?> {
            return arrayOfNulls(size)
        }
    }
}