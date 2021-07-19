package com.example.caiguru.seller.sellerChooseCategory

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.R

class SellerCategoryModel() : Parcelable {

    var name: String = ""
    var purplebackground: Int = R.drawable.curve_rectangle_category_purple
    var greybackground: Int = R.drawable.curve_rectangle_category_grey
    var hasselected: Boolean = false
    var imagewhite: Int = 0
    var imageyellow: Int = 0
    var category_id: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        purplebackground = parcel.readInt()
        greybackground = parcel.readInt()
        hasselected = parcel.readByte() != 0.toByte()
        imagewhite = parcel.readInt()
        imageyellow = parcel.readInt()
        category_id = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(purplebackground)
        parcel.writeInt(greybackground)
        parcel.writeByte(if (hasselected) 1 else 0)
        parcel.writeInt(imagewhite)
        parcel.writeInt(imageyellow)
        parcel.writeString(category_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerCategoryModel> {
        override fun createFromParcel(parcel: Parcel): SellerCategoryModel {
            return SellerCategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<SellerCategoryModel?> {
            return arrayOfNulls(size)
        }
    }


}