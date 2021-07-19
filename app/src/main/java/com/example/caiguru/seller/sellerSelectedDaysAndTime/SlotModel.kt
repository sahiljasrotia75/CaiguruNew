package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.os.Parcel
import android.os.Parcelable

class SlotModel() :Parcelable {

    var from: String = "From"
    var to: String = "To"

    constructor(parcel: Parcel) : this() {
        from = parcel.readString().toString()
        to = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(from)
        parcel.writeString(to)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SlotModel> {
        override fun createFromParcel(parcel: Parcel): SlotModel {
            return SlotModel(parcel)
        }

        override fun newArray(size: Int): Array<SlotModel?> {
            return arrayOfNulls(size)
        }
    }


}



