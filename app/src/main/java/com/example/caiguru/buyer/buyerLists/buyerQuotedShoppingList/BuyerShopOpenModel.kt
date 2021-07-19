package com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList

import android.os.Parcel
import android.os.Parcelable

class BuyerShopOpenModel() : Parcelable {
    var id: String = ""
    var seller_id: String = ""
    var is_read: String = ""
    var amount: String = ""
    var credits: String = ""
    var seller_name: String = ""
    var seller_image: String = ""
    var seller_level: String = ""
    var reputation: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        is_read = parcel.readString().toString()
        amount = parcel.readString().toString()
        credits = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        seller_image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        reputation = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(seller_id)
        parcel.writeString(is_read)
        parcel.writeString(amount)
        parcel.writeString(credits)
        parcel.writeString(seller_name)
        parcel.writeString(seller_image)
        parcel.writeString(seller_level)
        parcel.writeString(reputation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyerShopOpenModel> {
        override fun createFromParcel(parcel: Parcel): BuyerShopOpenModel {
            return BuyerShopOpenModel(parcel)
        }

        override fun newArray(size: Int): Array<BuyerShopOpenModel?> {
            return arrayOfNulls(size)
        }
    }


}
