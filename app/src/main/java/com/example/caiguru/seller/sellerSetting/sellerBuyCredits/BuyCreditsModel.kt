package com.example.caiguru.seller.sellerSetting.sellerBuyCredits

import android.os.Parcel
import android.os.Parcelable

class BuyCreditsModel() :Parcelable{
    var hasselected: Boolean = false
    var credits: String = ""
    var total: String = ""
    var totalWithoutText: String = ""
    var mseeage: String = ""

    constructor(parcel: Parcel) : this() {
        hasselected = parcel.readByte() != 0.toByte()
        credits = parcel.readString().toString()
        total = parcel.readString().toString()
        mseeage = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasselected) 1 else 0)
        parcel.writeString(credits)
        parcel.writeString(total)
        parcel.writeString(mseeage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyCreditsModel> {
        override fun createFromParcel(parcel: Parcel): BuyCreditsModel {
            return BuyCreditsModel(parcel)
        }

        override fun newArray(size: Int): Array<BuyCreditsModel?> {
            return arrayOfNulls(size)
        }
    }
}