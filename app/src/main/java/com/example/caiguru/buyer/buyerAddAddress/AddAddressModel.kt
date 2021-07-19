package com.example.caiguru.buyer.buyerAddAddress

import android.os.Parcel
import android.os.Parcelable

class AddAddressModel() :Parcelable {

    var lat: String = ""
    var long: String = ""
    var radius: String = ""
    var address : String = ""

    constructor(parcel: Parcel) : this() {
        lat = parcel.readString().toString()
        long = parcel.readString().toString()
        radius = parcel.readString().toString()
        address = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeString(radius)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddAddressModel> {
        override fun createFromParcel(parcel: Parcel): AddAddressModel {
            return AddAddressModel(parcel)
        }

        override fun newArray(size: Int): Array<AddAddressModel?> {
            return arrayOfNulls(size)
        }
    }


}