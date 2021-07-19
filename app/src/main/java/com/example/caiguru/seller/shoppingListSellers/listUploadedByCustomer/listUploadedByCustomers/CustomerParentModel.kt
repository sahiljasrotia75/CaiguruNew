package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.os.Parcel
import android.os.Parcelable

class CustomerParentModel() : Parcelable {

    var list: String = ""
    var checked = false
    var isExpanded = false
    var shoppingList: String = ""
    var productCommission: String = ""
    var message: String = ""
    var isCollapsed = true
    var showText: String = ""
    var cat_id: String = ""
    var lists = ArrayList<CustomerChildModel>()
    var more: Int = 0

    constructor(parcel: Parcel) : this() {
        list = parcel.readString().toString()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        shoppingList = parcel.readString().toString()
        productCommission = parcel.readString().toString()
        message = parcel.readString().toString()
        isCollapsed = parcel.readByte() != 0.toByte()
        cat_id = parcel.readString().toString().toString()
        more = parcel.readInt()
        showText = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(list)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(shoppingList)
        parcel.writeString(productCommission)
        parcel.writeString(message)
        parcel.writeByte(if (isCollapsed) 1 else 0)
        parcel.writeString(cat_id)
        parcel.writeInt(more)
        parcel.writeString(showText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerParentModel> {
        override fun createFromParcel(parcel: Parcel): CustomerParentModel {
            return CustomerParentModel(parcel)
        }

        override fun newArray(size: Int): Array<CustomerParentModel?> {
            return arrayOfNulls(size)
        }
    }


}