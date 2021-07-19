package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.os.Parcel
import android.os.Parcelable

class CustomerChildModel() : Parcelable {
    var id: String = ""
    var list_id: String = ""
    var cat_id: String = ""
    var buyer_id: String = ""
    var totals: String = ""
    var seller_id: String = ""
    var listingname: String = ""
    var level: String = ""
    var name: String = ""
    var image: String = ""
    var reputation: String = ""
    var created_at: String = ""
    var distance: String = ""
    var picking_detail: String = ""
    var payment_methods: String = ""
   // var more: Int = 0
    //strings that are array
    var delivery_location: String = ""
    var delivery_daytime: String = ""
    var product_details: String = ""
    var quote_requested: String = ""
    var comission_per: String = ""
    var amount: String = ""
    var credits: String = ""
    var isExpanded = false
//static data not by web service
    var comission: String = "0"
    var total_price: String = ""
    var delivery_type: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        totals = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        level = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        reputation = parcel.readString().toString()
        created_at = parcel.readString().toString()
        distance = parcel.readString().toString()
        picking_detail = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        delivery_location = parcel.readString().toString()
        delivery_daytime = parcel.readString().toString()
        product_details = parcel.readString().toString()
        quote_requested = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        amount = parcel.readString().toString()
        credits = parcel.readString().toString()
        isExpanded = parcel.readByte() != 0.toByte()
        comission = parcel.readString().toString()
        total_price = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(list_id)
        parcel.writeString(cat_id)
        parcel.writeString(buyer_id)
        parcel.writeString(totals)
        parcel.writeString(seller_id)
        parcel.writeString(listingname)
        parcel.writeString(level)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(reputation)
        parcel.writeString(created_at)
        parcel.writeString(distance)
        parcel.writeString(picking_detail)
        parcel.writeString(payment_methods)
        parcel.writeString(delivery_location)
        parcel.writeString(delivery_daytime)
        parcel.writeString(product_details)
        parcel.writeString(quote_requested)
        parcel.writeString(comission_per)
        parcel.writeString(amount)
        parcel.writeString(credits)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(comission)
        parcel.writeString(total_price)
        parcel.writeString(delivery_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerChildModel> {
        override fun createFromParcel(parcel: Parcel): CustomerChildModel {
            return CustomerChildModel(parcel)
        }

        override fun newArray(size: Int): Array<CustomerChildModel?> {
            return arrayOfNulls(size)
        }
    }
}

