package com.example.caiguru.seller.homeSeller

import android.os.Parcel
import android.os.Parcelable

class HomeSellerModel() :Parcelable{
    var req_id: String = ""
    var list_id: String = ""
    var cat_id: String = ""
    var created_at: String = ""
    var list_type: String = ""
    var buyer_id: String = ""
    var is_read: String = ""
    var user_id: String = ""
    var name: String = ""
    var image: String = ""
    var level: String = ""
    var reputation: String = ""
    var status: String = ""

    constructor(parcel: Parcel) : this() {
        req_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        created_at = parcel.readString().toString()
        list_type = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        is_read = parcel.readString().toString()
        user_id = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        reputation = parcel.readString().toString()
        status = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(req_id)
        parcel.writeString(list_id)
        parcel.writeString(cat_id)
        parcel.writeString(created_at)
        parcel.writeString(list_type)
        parcel.writeString(buyer_id)
        parcel.writeString(is_read)
        parcel.writeString(user_id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(reputation)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeSellerModel> {
        override fun createFromParcel(parcel: Parcel): HomeSellerModel {
            return HomeSellerModel(parcel)
        }

        override fun newArray(size: Int): Array<HomeSellerModel?> {
            return arrayOfNulls(size)
        }
    }
}