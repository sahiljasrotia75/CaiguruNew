package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel

class PayCreditsResultModel() :Parcelable {

    var req_ids: String = ""
    var seller_name: String = ""
    var seller_image: String = ""
    var seller_level: String = ""
    var seller_reputation: String = ""
    var seller_id: String = ""
    var list_type: String = ""
    var listings=ArrayList<ChooseSellerShoppingModel>()

    constructor(parcel: Parcel) : this() {
        req_ids = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        seller_image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        seller_reputation = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(req_ids)
        parcel.writeString(seller_name)
        parcel.writeString(seller_image)
        parcel.writeString(seller_level)
        parcel.writeString(seller_reputation)
        parcel.writeString(seller_id)
        parcel.writeString(list_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PayCreditsResultModel> {
        override fun createFromParcel(parcel: Parcel): PayCreditsResultModel {
            return PayCreditsResultModel(parcel)
        }

        override fun newArray(size: Int): Array<PayCreditsResultModel?> {
            return arrayOfNulls(size)
        }
    }


}