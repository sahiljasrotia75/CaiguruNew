package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.os.Parcel
import android.os.Parcelable

class EarnedReferedFriendModel() : Parcelable {
    var req_id: String = ""
    var list_type: String = ""
    var credits: String = ""
    var checked = false
    var isExpanded = false
    var isCollapsed = true
    var delivery_date: String = ""
    var joined_on: String = ""
    var order_count: String = ""
    var reff_by: String = ""
    var listingname: String = ""
    var buyer_id: String = ""
    var buyer_name: String = ""
    var buyer_image: String = ""
    var full_address: String = ""
    var buyer_level: String = ""
    var reputation: String = ""
    var count: Int = 0

    constructor(parcel: Parcel) : this() {
        req_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
        credits = parcel.readString().toString()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        isCollapsed = parcel.readByte() != 0.toByte()
        delivery_date = parcel.readString().toString()
        joined_on = parcel.readString().toString()
        order_count = parcel.readString().toString()
        reff_by = parcel.readString().toString()
        listingname = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        buyer_name = parcel.readString().toString()
        buyer_image = parcel.readString().toString()
        full_address = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        reputation = parcel.readString().toString()
        count = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(req_id)
        parcel.writeString(list_type)
        parcel.writeString(credits)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeByte(if (isCollapsed) 1 else 0)
        parcel.writeString(delivery_date)
        parcel.writeString(joined_on)
        parcel.writeString(order_count)
        parcel.writeString(reff_by)
        parcel.writeString(listingname)
        parcel.writeString(buyer_id)
        parcel.writeString(buyer_name)
        parcel.writeString(buyer_image)
        parcel.writeString(full_address)
        parcel.writeString(buyer_level)
        parcel.writeString(reputation)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarnedReferedFriendModel> {
        override fun createFromParcel(parcel: Parcel): EarnedReferedFriendModel {
            return EarnedReferedFriendModel(parcel)
        }

        override fun newArray(size: Int): Array<EarnedReferedFriendModel?> {
            return arrayOfNulls(size)
        }
    }


}
