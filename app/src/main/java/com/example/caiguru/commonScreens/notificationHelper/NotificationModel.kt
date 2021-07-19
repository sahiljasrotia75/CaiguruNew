package com.example.caiguru.commonScreens.notificationHelper

import android.os.Parcel
import android.os.Parcelable

class NotificationModel() : Parcelable {
    var source_id = ""
    var list_type = ""
    var source = ""
    var name = ""
    var image = ""
    var level = ""
    var listingname = ""
    var reputation = ""
    var created_at = ""
    var notification_id = ""
    var sender_id = ""

    constructor(parcel: Parcel) : this() {
        source_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
        source = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        listingname = parcel.readString().toString()
        reputation = parcel.readString().toString()
        created_at = parcel.readString().toString()
        notification_id = parcel.readString().toString()
        sender_id = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source_id)
        parcel.writeString(list_type)
        parcel.writeString(source)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(listingname)
        parcel.writeString(reputation)
        parcel.writeString(created_at)
        parcel.writeString(notification_id)
        parcel.writeString(sender_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationModel> {
        override fun createFromParcel(parcel: Parcel): NotificationModel {
            return NotificationModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationModel?> {
            return arrayOfNulls(size)
        }
    }
}