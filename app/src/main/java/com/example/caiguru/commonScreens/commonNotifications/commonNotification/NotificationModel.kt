package com.example.caiguru.commonScreens.commonNotifications.commonNotification

import android.os.Parcel
import android.os.Parcelable

class NotificationModel() :Parcelable {

    var notification_id: String = ""
    var is_read: String = ""
    var source: String = ""
    var source_id: String = ""
    var action_taken: String = ""
    var msg: String = ""
    var msg_en: String = ""
    var msg_es: String = ""
    var created_at: String = ""
    var list_type: String = ""
    var sender_id: String = ""
    var listingname: String = ""
    var name: String = ""
    var image: String = ""
    var level: String = ""
    var reputation: String = ""

    constructor(parcel: Parcel) : this() {
        notification_id = parcel.readString().toString()
        is_read = parcel.readString().toString()
        source = parcel.readString().toString()
        source_id = parcel.readString().toString()
        action_taken = parcel.readString().toString()
        msg = parcel.readString().toString()
        msg_en = parcel.readString().toString()
        msg_es = parcel.readString().toString()
        created_at = parcel.readString().toString()
        list_type = parcel.readString().toString()
        sender_id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        reputation = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notification_id)
        parcel.writeString(is_read)
        parcel.writeString(source)
        parcel.writeString(source_id)
        parcel.writeString(action_taken)
        parcel.writeString(msg)
        parcel.writeString(msg_en)
        parcel.writeString(msg_es)
        parcel.writeString(created_at)
        parcel.writeString(list_type)
        parcel.writeString(sender_id)
        parcel.writeString(listingname)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(reputation)
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