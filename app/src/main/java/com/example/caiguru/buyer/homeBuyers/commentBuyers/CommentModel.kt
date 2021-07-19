package com.example.caiguru.buyer.homeBuyers.commentBuyers

import android.os.Parcel
import android.os.Parcelable

class CommentModel() : Parcelable {
    var comment: String = ""
    var comment_by: String = ""
    var created_at: String = ""
    var name: String = ""
    var image: String = ""
    var level: String = ""
    var reputation: String = ""
    var type: String = ""


    constructor(parcel: Parcel) : this() {
        comment = parcel.readString().toString()
        comment_by = parcel.readString().toString()
        created_at = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        reputation = parcel.readString().toString()
        type = parcel.readString().toString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(comment)
        parcel.writeString(comment_by)
        parcel.writeString(created_at)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(reputation)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommentModel> {
        override fun createFromParcel(parcel: Parcel): CommentModel {
            return CommentModel(parcel)
        }

        override fun newArray(size: Int): Array<CommentModel?> {
            return arrayOfNulls(size)
        }
    }
}