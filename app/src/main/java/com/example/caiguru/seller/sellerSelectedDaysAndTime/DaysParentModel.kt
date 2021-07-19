package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.os.Parcel
import android.os.Parcelable

class DaysParentModel() : Parcelable {
    var weeks: String = ""
    var position: String = ""
    var day: String = ""
    var checked = false
    var isExpanded = false
    var isCollapsed = true
    var slotList = ArrayList<SlotModel>()
    //another parameter
    var value = ArrayList<SlotModel>()

    constructor(parcel: Parcel) : this() {
        weeks = parcel.readString().toString()
        position = parcel.readString().toString()
        day = parcel.readString().toString()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        isCollapsed = parcel.readByte() != 0.toByte()
        slotList = parcel.createTypedArrayList(SlotModel.CREATOR)!!
        value = parcel.createTypedArrayList(SlotModel.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(weeks)
        parcel.writeString(position)
        parcel.writeString(day)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeByte(if (isCollapsed) 1 else 0)
        parcel.writeTypedList(slotList)
        parcel.writeTypedList(value)
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