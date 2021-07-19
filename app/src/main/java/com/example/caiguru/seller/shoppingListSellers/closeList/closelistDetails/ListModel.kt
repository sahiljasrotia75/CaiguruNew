package com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel

class ListModel() :Parcelable{

    var cat_id: String = ""
    var lists = ArrayList<ListParentModel>()
    var count:Int=0
    var checked = false
    var isExpanded = false
    var isCollapsed = true

    constructor(parcel: Parcel) : this() {
        cat_id = parcel.readString().toString()
        count = parcel.readInt()
        checked = parcel.readByte() != 0.toByte()
        isExpanded = parcel.readByte() != 0.toByte()
        isCollapsed = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cat_id)
        parcel.writeInt(count)
        parcel.writeByte(if (checked) 1 else 0)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeByte(if (isCollapsed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListModel> {
        override fun createFromParcel(parcel: Parcel): ListModel {
            return ListModel(parcel)
        }

        override fun newArray(size: Int): Array<ListModel?> {
            return arrayOfNulls(size)
        }
    }


}
