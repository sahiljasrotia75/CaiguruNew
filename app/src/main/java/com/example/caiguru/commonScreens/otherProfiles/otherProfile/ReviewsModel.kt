package com.example.caiguru.commonScreens.otherProfiles.otherProfile

import android.os.Parcel
import android.os.Parcelable

class ReviewsModel() : Parcelable {
    var isExpanded: Boolean = false
    var level: String = ""
    var comment: String = ""
    var list_type: String = ""
    var user_id: String = ""
    var reputation: String = ""
    var req_id: String = ""
    var points: String = ""
    var image: String = ""
    var name: String = ""
    var rating: String = ""
    var created_at: String = ""
    var type: String = ""

    constructor(parcel: Parcel) : this() {
        isExpanded = parcel.readByte() != 0.toByte()
        level = parcel.readString().toString()
        comment = parcel.readString().toString()
        list_type = parcel.readString().toString()
        user_id = parcel.readString().toString()
        reputation = parcel.readString().toString()
        req_id = parcel.readString().toString()
        points = parcel.readString().toString()
        image = parcel.readString().toString()
        name = parcel.readString().toString()
        rating = parcel.readString().toString()
        created_at = parcel.readString().toString()
        type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(level)
        parcel.writeString(comment)
        parcel.writeString(list_type)
        parcel.writeString(user_id)
        parcel.writeString(reputation)
        parcel.writeString(req_id)
        parcel.writeString(points)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(rating)
        parcel.writeString(created_at)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReviewsModel> {
        override fun createFromParcel(parcel: Parcel): ReviewsModel {
            return ReviewsModel(parcel)
        }

        override fun newArray(size: Int): Array<ReviewsModel?> {
            return arrayOfNulls(size)
        }
    }


}