package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.os.Parcel
import android.os.Parcelable

class EarnedShoppingListModel() :Parcelable {
    var lists = ArrayList<EarnedReferedFriendModel>()
    var count:Int=0
    var checked = false
    var isExpanded = false
    var isCollapsed = true

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        isCollapsed = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeByte(if (isCollapsed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarnedShoppingListModel> {
        override fun createFromParcel(parcel: Parcel): EarnedShoppingListModel {
            return EarnedShoppingListModel(parcel)
        }

        override fun newArray(size: Int): Array<EarnedShoppingListModel?> {
            return arrayOfNulls(size)
        }
    }
}
