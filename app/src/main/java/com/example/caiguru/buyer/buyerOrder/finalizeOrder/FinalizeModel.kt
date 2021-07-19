package com.example.caiguru.buyer.buyerOrder.finalizeOrder

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class FinalizeModel() : Parcelable {

    var id: String = ""
    var lat: String = ""
    var long: String = ""
    var address: String = ""
    var seller_id: String = ""
    var is_read: String = ""
    var created_at: String = ""
    var list_type: String = ""
    var req_status: String = ""
    var delivery_type: String = ""
    var cat_id: String = ""
    var seller_name: String = ""
    var seller_image: String = ""
    var seller_level: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        lat = parcel.readString().toString()
        long = parcel.readString().toString()
        address = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        is_read = parcel.readString().toString()
        created_at = parcel.readString().toString()
        list_type = parcel.readString().toString()
        req_status = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        seller_image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeString(address)
        parcel.writeString(seller_id)
        parcel.writeString(is_read)
        parcel.writeString(created_at)
        parcel.writeString(list_type)
        parcel.writeString(req_status)
        parcel.writeString(delivery_type)
        parcel.writeString(cat_id)
        parcel.writeString(seller_name)
        parcel.writeString(seller_image)
        parcel.writeString(seller_level)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinalizeModel> {
        override fun createFromParcel(parcel: Parcel): FinalizeModel {
            return FinalizeModel(parcel)
        }

        override fun newArray(size: Int): Array<FinalizeModel?> {
            return arrayOfNulls(size)
        }
    }


}


