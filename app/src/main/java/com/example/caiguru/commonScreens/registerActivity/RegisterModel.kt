package com.example.caiguru.commonScreens.registerActivity

import android.os.Parcel
import android.os.Parcelable

class RegisterModel() : Parcelable {


    var name: String = ""
    var email: String = ""
    var password: String = ""
    var social_type: String = "1"
    var social_id: String = ""
    var referral_code: String = ""
    var categories: String = ""
    var image: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        email = parcel.readString().toString()
        password = parcel.readString().toString()
        social_type = parcel.readString().toString()
        social_id = parcel.readString().toString()
        referral_code = parcel.readString().toString()
        categories = parcel.readString().toString()
        image = parcel.readString().toString()
    }

//    var obj_token: String = ""
//    var obj_type: String = ""
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(social_type)
        parcel.writeString(social_id)
        parcel.writeString(referral_code)
        parcel.writeString(categories)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegisterModel> {
        override fun createFromParcel(parcel: Parcel): RegisterModel {
            return RegisterModel(parcel)
        }

        override fun newArray(size: Int): Array<RegisterModel?> {
            return arrayOfNulls(size)
        }
    }


}
