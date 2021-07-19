package com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser

import android.os.Parcel
import android.os.Parcelable

class HomeViewMoreModel():Parcelable {

    val shared_by: String = ""
    val created_at: String = ""
    val shared_by_name: String = ""
    val shared_by_image: String = ""
    val shared_by_level: String = ""
    val shared_by_reputation: String = ""
    val type: String = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeViewMoreModel> {
        override fun createFromParcel(parcel: Parcel): HomeViewMoreModel {
            return HomeViewMoreModel(parcel)
        }

        override fun newArray(size: Int): Array<HomeViewMoreModel?> {
            return arrayOfNulls(size)
        }
    }


}




