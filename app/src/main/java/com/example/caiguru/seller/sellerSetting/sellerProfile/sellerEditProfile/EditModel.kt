package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile

import android.os.Parcel
import android.os.Parcelable

class EditModel() :Parcelable {

    var id: String = " "
    var name: String = " "
    var hasselected: Boolean = false
    var msg:String=""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        name = parcel.readString().toString()
        hasselected = parcel.readByte() != 0.toByte()
        msg = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeByte(if (hasselected) 1 else 0)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EditModel> {
        override fun createFromParcel(parcel: Parcel): EditModel {
            return EditModel(parcel)
        }

        override fun newArray(size: Int): Array<EditModel?> {
            return arrayOfNulls(size)
        }
    }

}
