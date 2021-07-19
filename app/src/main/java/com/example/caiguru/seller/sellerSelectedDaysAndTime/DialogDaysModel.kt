package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.os.Parcel
import android.os.Parcelable

class DialogDaysModel():Parcelable {
    var daysPosition = ""
    var dayName = ""
    var isSelected: Boolean = false
    var slotList = ArrayList<SlotModel>()


    constructor(parcel: Parcel) : this() {
        slotList = parcel.createTypedArrayList(SlotModel.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(slotList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DaysParentModel> {
        override fun createFromParcel(parcel: Parcel): DaysParentModel {
            return DaysParentModel(parcel)
        }

        override fun newArray(size: Int): Array<DaysParentModel?> {
            return arrayOfNulls(size)
        }
    }
}

