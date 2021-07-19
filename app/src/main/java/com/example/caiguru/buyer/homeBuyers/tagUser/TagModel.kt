package com.example.caiguru.buyer.homeBuyers.tagUser

import android.os.Parcel
import android.os.Parcelable

class TagModel() :Parcelable {

    var type: String = ""
    var name: String = ""
    var image: String = ""
    var buyer_level: String = ""
    var buyer_points: String = ""
    var user_id: String = ""
    var is_tagged: String = ""
    var reputation: String = ""
    var hasTagged:Boolean = false



    constructor(parcel: Parcel) : this() {
        type = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        buyer_points = parcel.readString().toString()
        user_id = parcel.readString().toString()
        is_tagged = parcel.readString().toString()
        reputation = parcel.readString().toString()
        hasTagged = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(buyer_level)
        parcel.writeString(buyer_points)
        parcel.writeString(user_id)
        parcel.writeString(is_tagged)
        parcel.writeString(reputation)
        parcel.writeByte(if (hasTagged) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TagModel> {
        override fun createFromParcel(parcel: Parcel): TagModel {
            return TagModel(parcel)
        }

        override fun newArray(size: Int): Array<TagModel?> {
            return arrayOfNulls(size)
        }
    }


}