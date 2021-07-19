package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.os.Parcel
import android.os.Parcelable

class DialogModel() : Parcelable {


    var name: String = ""
    var id: String = ""
    var unit:String=" "
    var price:String=" "
    var image:String=""
    var message:String=""
    var qty: String=""
    var highlight: String=""
    var status: String=""
    var priceWithComission: String=""
    var list_id: String=""
    var saved_product_id: String=""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        id = parcel.readString().toString()
        unit = parcel.readString().toString()
        price = parcel.readString().toString()
        image = parcel.readString().toString()
        message = parcel.readString().toString()
        qty = parcel.readString().toString()
        highlight = parcel.readString().toString()
        status = parcel.readString().toString()
        priceWithComission = parcel.readString().toString()
        list_id = parcel.readString().toString()
        saved_product_id = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(unit)
        parcel.writeString(price)
        parcel.writeString(image)
        parcel.writeString(message)
        parcel.writeString(qty)
        parcel.writeString(highlight)
        parcel.writeString(status)
        parcel.writeString(priceWithComission)
        parcel.writeString(list_id)
        parcel.writeString(saved_product_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogModel> {
        override fun createFromParcel(parcel: Parcel): DialogModel {
            return DialogModel(parcel)
        }

        override fun newArray(size: Int): Array<DialogModel?> {
            return arrayOfNulls(size)
        }
    }


}