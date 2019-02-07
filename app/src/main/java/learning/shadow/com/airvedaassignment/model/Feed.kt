package learning.shadow.com.airvedaassignment.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "feed_table")
data class Feed(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var description: String,
    var imageUrl: String? = "",
    var name: String,
    var text: String? = "",
    var time: Long,
    var title: String,
    var showTime: Boolean = true,
    var isLiked: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeLong(time)
        parcel.writeString(title)
        parcel.writeByte(if (showTime) 1 else 0)
        parcel.writeByte(if (isLiked) 1 else 0)
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