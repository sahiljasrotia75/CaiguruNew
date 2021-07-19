package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.os.Parcel
import android.os.Parcelable

class ListParentModel() : Parcelable {

    var list: String = ""
    var checked = false
    var isExpanded = false
    var shoppingList: String = ""
    var productCommission: String = ""
    var message: String = ""
    var isCollapsed = true
    var childList = ArrayList<ListChildModel>()
    var id: String = ""
    var cat_id: String = ""
    var payment_methods: String = ""
    var listingname: String = ""
    var comission_per: String = ""
    var created_at: String = ""
    var delivery_days: String = ""
    var pickup_details: String = ""
    var minimum_purchase_amount: String = ""
    var delivery_zone: String = ""
    var product_details: String = ""
    var allow_modify: String = ""
    var credits: String = ""
    var amount: String = ""
    var count:Int=0
    var status = ""

    constructor(parcel: Parcel) : this() {
        list = parcel.readString().toString()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        shoppingList = parcel.readString().toString()
        productCommission = parcel.readString().toString()
        message = parcel.readString().toString()
        isCollapsed = parcel.readByte() != 0.toByte()
        id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        listingname = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        created_at = parcel.readString().toString()
        delivery_days = parcel.readString().toString()
        pickup_details = parcel.readString().toString()
        minimum_purchase_amount = parcel.readString().toString()
        delivery_zone = parcel.readString().toString()
        product_details = parcel.readString().toString()
        allow_modify = parcel.readString().toString()
        credits = parcel.readString().toString()
        amount = parcel.readString().toString()
        count = parcel.readInt()
        status = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(list)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(shoppingList)
        parcel.writeString(productCommission)
        parcel.writeString(message)
        parcel.writeByte(if (isCollapsed) 1 else 0)
        parcel.writeString(id)
        parcel.writeString(cat_id)
        parcel.writeString(payment_methods)
        parcel.writeString(listingname)
        parcel.writeString(comission_per)
        parcel.writeString(created_at)
        parcel.writeString(delivery_days)
        parcel.writeString(pickup_details)
        parcel.writeString(minimum_purchase_amount)
        parcel.writeString(delivery_zone)
        parcel.writeString(product_details)
        parcel.writeString(allow_modify)
        parcel.writeString(credits)
        parcel.writeString(amount)
        parcel.writeInt(count)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListParentModel> {
        override fun createFromParcel(parcel: Parcel): ListParentModel {
            return ListParentModel(parcel)
        }

        override fun newArray(size: Int): Array<ListParentModel?> {
            return arrayOfNulls(size)
        }
    }


}