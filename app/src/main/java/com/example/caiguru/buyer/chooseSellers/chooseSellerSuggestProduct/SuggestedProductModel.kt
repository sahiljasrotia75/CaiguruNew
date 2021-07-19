package com.example.caiguru.buyer.chooseSellers.chooseSellerSuggestProduct

import android.os.Parcel
import android.os.Parcelable

class SuggestedProductModel() : Parcelable {
    var suggestedProductName: String = ""
    var position: String = ""

    constructor(parcel: Parcel) : this() {
        suggestedProductName = parcel.readString().toString()
        position = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(suggestedProductName)
        parcel.writeString(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SuggestedProductModel> {
        override fun createFromParcel(parcel: Parcel): SuggestedProductModel {
            return SuggestedProductModel(parcel)
        }

        override fun newArray(size: Int): Array<SuggestedProductModel?> {
            return arrayOfNulls(size)
        }
    }

}