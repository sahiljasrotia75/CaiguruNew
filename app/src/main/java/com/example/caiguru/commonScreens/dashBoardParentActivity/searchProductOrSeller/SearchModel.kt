package com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller

import android.os.Parcel
import android.os.Parcelable

class SearchModel() : Parcelable {

    var searchName = ""

    constructor(parcel: Parcel) : this() {
        searchName = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(searchName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchModel> {
        override fun createFromParcel(parcel: Parcel): SearchModel {
            return SearchModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<SearchModel?> {
            return arrayOfNulls(size)
        }
    }
}