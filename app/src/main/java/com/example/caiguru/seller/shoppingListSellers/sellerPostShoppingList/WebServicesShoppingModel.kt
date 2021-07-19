package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.os.Parcel
import android.os.Parcelable

class WebServicesShoppingModel() : Parcelable {

    var name: String = ""
    var image: String = ""
    var unit: String = ""
    var qty:String=""
    var price: String = ""
    var listingname: String = ""
    var priceWithComission: String = ""
    var msg: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        unit = parcel.readString().toString()
        qty = parcel.readString().toString()
        price = parcel.readString().toString()
        listingname = parcel.readString().toString()
        priceWithComission = parcel.readString().toString()
        msg = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(unit)
        parcel.writeString(qty)
        parcel.writeString(price)
        parcel.writeString(listingname)
        parcel.writeString(priceWithComission)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebServicesShoppingModel> {
        override fun createFromParcel(parcel: Parcel): WebServicesShoppingModel {
            return WebServicesShoppingModel(parcel)
        }

        override fun newArray(size: Int): Array<WebServicesShoppingModel?> {
            return arrayOfNulls(size)
        }
    }


}
