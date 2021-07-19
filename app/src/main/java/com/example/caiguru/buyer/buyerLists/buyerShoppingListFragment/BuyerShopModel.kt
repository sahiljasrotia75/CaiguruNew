package com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment

import android.os.Parcel
import android.os.Parcelable

class BuyerShopModel() : Parcelable {

    var id: String = ""
    var listingname: String = ""
    var modify: String = ""
    var best_quote: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        modify = parcel.readString().toString()
        best_quote = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(listingname)
        parcel.writeString(modify)
        parcel.writeString(best_quote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyerShopModel> {
        override fun createFromParcel(parcel: Parcel): BuyerShopModel {
            return BuyerShopModel(parcel)
        }

        override fun newArray(size: Int): Array<BuyerShopModel?> {
            return arrayOfNulls(size)
        }
    }
}