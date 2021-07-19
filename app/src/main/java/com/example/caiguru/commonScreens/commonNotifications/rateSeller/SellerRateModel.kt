package com.example.caiguru.commonScreens.commonNotifications.rateSeller

import android.os.Parcel
import android.os.Parcelable

class SellerRateModel() :Parcelable{

    var rateImageSelected: Int = 0
    var rateImageUnSelected: Int = 0
    var rateStatusText: String = ""
    var hasSelected: Boolean = false
    var rating: String = ""

    constructor(parcel: Parcel) : this() {
        rateImageSelected = parcel.readInt()
        rateImageUnSelected = parcel.readInt()
        rateStatusText = parcel.readString().toString()
        hasSelected = parcel.readByte() != 0.toByte()
        rating = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rateImageSelected)
        parcel.writeInt(rateImageUnSelected)
        parcel.writeString(rateStatusText)
        parcel.writeByte(if (hasSelected) 1 else 0)
        parcel.writeString(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerRateModel> {
        override fun createFromParcel(parcel: Parcel): SellerRateModel {
            return SellerRateModel(parcel)
        }

        override fun newArray(size: Int): Array<SellerRateModel?> {
            return arrayOfNulls(size)
        }
    }
}