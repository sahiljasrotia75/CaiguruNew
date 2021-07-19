package com.example.caiguru.buyer.buyerProfile.follower

import android.os.Parcel
import android.os.Parcelable

class FollowUnfollowModel() :Parcelable {

var type:String=""
var name:String=""
var image:String=""
var seller_level:String=""
var buyer_level:String=""
var buyer_points:String=""
var seller_points:String=""
var user_id:String=""
var isFollowed:String=""
var reputation:String=""

    constructor(parcel: Parcel) : this() {
        type = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        buyer_points = parcel.readString().toString()
        seller_points = parcel.readString().toString()
        user_id = parcel.readString().toString()
        isFollowed = parcel.readString().toString()
        reputation = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(seller_level)
        parcel.writeString(buyer_level)
        parcel.writeString(buyer_points)
        parcel.writeString(seller_points)
        parcel.writeString(user_id)
        parcel.writeString(isFollowed)
        parcel.writeString(reputation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FollowUnfollowModel> {
        override fun createFromParcel(parcel: Parcel): FollowUnfollowModel {
            return FollowUnfollowModel(parcel)
        }

        override fun newArray(size: Int): Array<FollowUnfollowModel?> {
            return arrayOfNulls(size)
        }
    }

}
