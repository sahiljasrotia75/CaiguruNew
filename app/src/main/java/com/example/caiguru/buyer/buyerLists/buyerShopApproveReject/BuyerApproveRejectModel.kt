package com.example.caiguru.buyer.buyerLists.buyerShopApproveReject

import android.os.Parcel
import android.os.Parcelable

class BuyerApproveRejectModel() :Parcelable {

    var id: String = ""
    var created_at: String = ""
    var products: String = ""
    var amount: String = ""
    var credits: String = ""
    var listingname: String = ""
    var cat_id: String = ""
    var delivery_daytime: String = ""
    var delivery_location: String = ""
    var delivery_type: String = ""
    var seller_id: String = ""
    var seller_name: String = ""
    var payment_methods: String = ""
    var type: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        created_at = parcel.readString().toString()
        products = parcel.readString().toString()
        amount = parcel.readString().toString()
        credits = parcel.readString().toString()
        listingname = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        delivery_daytime = parcel.readString().toString()
        delivery_location = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(created_at)
        parcel.writeString(products)
        parcel.writeString(amount)
        parcel.writeString(credits)
        parcel.writeString(listingname)
        parcel.writeString(cat_id)
        parcel.writeString(delivery_daytime)
        parcel.writeString(delivery_location)
        parcel.writeString(delivery_type)
        parcel.writeString(seller_id)
        parcel.writeString(seller_name)
        parcel.writeString(payment_methods)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyerApproveRejectModel> {
        override fun createFromParcel(parcel: Parcel): BuyerApproveRejectModel {
            return BuyerApproveRejectModel(parcel)
        }

        override fun newArray(size: Int): Array<BuyerApproveRejectModel?> {
            return arrayOfNulls(size)
        }
    }


}