package com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel

class DeliveryZoneModel() : Parcelable {

    var lat: String = ""
    var long: String = ""
    var radius: String = ""
    var address: String = ""
    var message:String=""
    var days = ArrayList<DaysParentModel>()

    constructor(parcel: Parcel) : this() {
        lat = parcel.readString().toString()
        long = parcel.readString().toString()
        radius = parcel.readString().toString()
        address = parcel.readString().toString()
        message = parcel.readString().toString()
        days = parcel.createTypedArrayList(DaysParentModel.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeString(radius)
        parcel.writeString(address)
        parcel.writeString(message)
        parcel.writeTypedList(days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeliveryZoneModel> {
        override fun createFromParcel(parcel: Parcel): DeliveryZoneModel {
            return DeliveryZoneModel(parcel)
        }

        override fun newArray(size: Int): Array<DeliveryZoneModel?> {
            return arrayOfNulls(size)
        }
    }


}