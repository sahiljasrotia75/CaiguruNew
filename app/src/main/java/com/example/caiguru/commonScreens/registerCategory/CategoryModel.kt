package com.example.caiguru.commonScreens.registerCategory

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.R

class CategoryModel() :Parcelable {
    var name: String = ""
    var whitebackground: Int = R.drawable.curve_rectangle_category_white
    var yellowbackground: Int = R.drawable.curve_rectangle_category_orange
    var hasselected: Boolean = false
    var imagewhite: Int = 0
    var imageyellow: Int = 0
    var category_id: String = ""


    //
    var status: String = ""
    var msg: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        whitebackground = parcel.readInt()
        yellowbackground = parcel.readInt()
        hasselected = parcel.readByte() != 0.toByte()
        imagewhite = parcel.readInt()
        imageyellow = parcel.readInt()
        category_id = parcel.readString().toString()
        status = parcel.readString().toString()
        msg = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(whitebackground)
        parcel.writeInt(yellowbackground)
        parcel.writeByte(if (hasselected) 1 else 0)
        parcel.writeInt(imagewhite)
        parcel.writeInt(imageyellow)
        parcel.writeString(category_id)
        parcel.writeString(status)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryModel> {
        override fun createFromParcel(parcel: Parcel): CategoryModel {
            return CategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<CategoryModel?> {
            return arrayOfNulls(size)
        }
    }


}