package com.example.caiguru.commonScreens.chat

import android.os.Parcel
import android.os.Parcelable

class ModelChat() : Parcelable{
    var channel_id = ""
    var user_id = ""
    var list_id = ""
    var list_type = ""
    var last_message = ""
    var type = ""
    var name = ""
    var image = ""
    var level=""
    var points = ""
    var updated_at = ""
    var msg_count = "0"
    var listingname = ""

    constructor(parcel: Parcel) : this() {
        channel_id = parcel.readString().toString()
        user_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
        last_message = parcel.readString().toString()
        type = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        points = parcel.readString().toString()
        updated_at = parcel.readString().toString()
        msg_count = parcel.readString().toString()
        listingname = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(channel_id)
        parcel.writeString(user_id)
        parcel.writeString(list_id)
        parcel.writeString(list_type)
        parcel.writeString(last_message)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(points)
        parcel.writeString(updated_at)
        parcel.writeString(msg_count)
        parcel.writeString(listingname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelChat> {
        override fun createFromParcel(parcel: Parcel): ModelChat {
            return ModelChat(parcel)
        }

        override fun newArray(size: Int): Array<ModelChat?> {
            return arrayOfNulls(size)
        }
    }
}