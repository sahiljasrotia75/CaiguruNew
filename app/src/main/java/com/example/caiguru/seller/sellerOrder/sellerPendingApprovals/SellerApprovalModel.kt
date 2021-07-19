package com.example.caiguru.seller.sellerOrder.sellerPendingApprovals

import android.os.Parcel
import android.os.Parcelable

class SellerApprovalModel() :Parcelable {

    var id:String=""
    var buyer_id:String=""
    var is_read:String=""
    var buyer_name:String=""
    var buyer_image:String=""
    var buyer_level:String=""
    var cat_id:String=""
    var list_type:String=""
    var req_status:String=""
    var created_at:String=""
    var delivery_type:String=""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        is_read = parcel.readString().toString()
        buyer_name = parcel.readString().toString()
        buyer_image = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
        req_status = parcel.readString().toString()
        created_at = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(buyer_id)
        parcel.writeString(is_read)
        parcel.writeString(buyer_name)
        parcel.writeString(buyer_image)
        parcel.writeString(buyer_level)
        parcel.writeString(cat_id)
        parcel.writeString(list_type)
        parcel.writeString(req_status)
        parcel.writeString(created_at)
        parcel.writeString(delivery_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerApprovalModel> {
        override fun createFromParcel(parcel: Parcel): SellerApprovalModel {
            return SellerApprovalModel(parcel)
        }

        override fun newArray(size: Int): Array<SellerApprovalModel?> {
            return arrayOfNulls(size)
        }
    }


}