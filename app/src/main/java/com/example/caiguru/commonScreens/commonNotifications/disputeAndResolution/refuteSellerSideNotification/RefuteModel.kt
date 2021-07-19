package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class RefuteModel() :Parcelable {

    var id: String = ""
    var address: String = ""
    var delivery_type: String = ""
    var credits: String = ""
    var amount: String = ""
    var created_at: String = ""
    var status: String = ""
    var reason: String = ""
    var completed_date: String = ""
    var seller_id: String = ""
    var buyer_id: String = ""
    var listingname: String = ""
    var list_type: String = ""
    var channel_id: String = ""
    var buyer_name: String = ""
    var buyer_image: String = ""
    var buyer_level: String = ""
    var reputation: String = ""
    var comment: String = ""
    var contact_name: String = ""
    var contact_number: String = ""
    var dispute_date: String = ""
    var comission_per: String = ""

    //array
   // var suggest_products = ArrayList<String>()
    var products=ArrayList<ShoppingListModel>()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        address = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        credits = parcel.readString().toString()
        amount = parcel.readString().toString()
        created_at = parcel.readString().toString()
        status = parcel.readString().toString()
        reason = parcel.readString().toString()
        completed_date = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        list_type = parcel.readString().toString()
        channel_id = parcel.readString().toString()
        buyer_name = parcel.readString().toString()
        buyer_image = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        reputation = parcel.readString().toString()
        comment = parcel.readString().toString()
        contact_name = parcel.readString().toString()
        contact_number = parcel.readString().toString()
        dispute_date = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        products = parcel.createTypedArrayList(ShoppingListModel)!!

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(address)
        parcel.writeString(delivery_type)
        parcel.writeString(credits)
        parcel.writeString(amount)
        parcel.writeString(created_at)
        parcel.writeString(status)
        parcel.writeString(reason)
        parcel.writeString(completed_date)
        parcel.writeString(seller_id)
        parcel.writeString(buyer_id)
        parcel.writeString(listingname)
        parcel.writeString(list_type)
        parcel.writeString(channel_id)
        parcel.writeString(buyer_name)
        parcel.writeString(buyer_image)
        parcel.writeString(buyer_level)
        parcel.writeString(reputation)
        parcel.writeString(comment)
        parcel.writeString(contact_name)
        parcel.writeString(contact_number)
        parcel.writeString(dispute_date)
        parcel.writeString(comission_per)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RefuteModel> {
        override fun createFromParcel(parcel: Parcel): RefuteModel {
            return RefuteModel(parcel)
        }

        override fun newArray(size: Int): Array<RefuteModel?> {
            return arrayOfNulls(size)
        }
    }
}