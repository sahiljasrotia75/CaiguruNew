package com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class OrderListModel() :Parcelable {

    var id: String = ""
    var address: String = ""
    var delivery_type: String = ""
    var isExpanded: Boolean = false
    var products = ArrayList<ShoppingListModel>()
    var suggest_products = ArrayList<String>()
    var credits: String = ""
    var amount: String = ""
    var listingname: String = ""
    var cat_id: String = ""
    var req_status: String = ""
    var created_at: String = ""
    var channel_id: String = ""
    var lat: String = ""
    var long: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        address = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        isExpanded = parcel.readByte() != 0.toByte()
        credits = parcel.readString().toString()
        amount = parcel.readString().toString()
        listingname = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        req_status = parcel.readString().toString()
        created_at = parcel.readString().toString()
        channel_id = parcel.readString().toString()
        lat = parcel.readString().toString()
        long = parcel.readString().toString()
        products = parcel.createTypedArrayList(ShoppingListModel.CREATOR)!!
        parcel.writeTypedList(products)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(address)
        parcel.writeString(delivery_type)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(credits)
        parcel.writeString(amount)
        parcel.writeString(listingname)
        parcel.writeString(cat_id)
        parcel.writeString(req_status)
        parcel.writeString(created_at)
        parcel.writeString(channel_id)
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeTypedList(products)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderListModel> {
        override fun createFromParcel(parcel: Parcel): OrderListModel {
            return OrderListModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderListModel?> {
            return arrayOfNulls(size)
        }
    }


}