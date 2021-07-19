package com.example.caiguru.buyer.buyerProfile.buyerMyOrder

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.buyer.homeBuyers.commentBuyers.CommentModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class OrderModel() : Parcelable {
    var req_id: String = ""
    var list_id: String = ""
    var list_type: String = ""
    var id: String = ""
    var address: String = ""
    var delivery_type: String = ""
    var amount: String = ""
    var created_at: String = ""
    var status: String = ""
    var listingname: String = ""
    var completed_date: String = ""
    var payment_methods: String = ""
    var seller_id: String = ""
    var buyer_id: String = ""
    var seller_name: String = ""
    var seller_image: String = ""
    var seller_level: String = ""
    var reputation: String = ""
    var credits: String = ""
    var reason: String = ""
    var cat_id: String = ""
    var type: String = ""
    var comission_per: String = ""
    var level: String = ""
    var picking_detail = DeliveryZoneModel()

    // var picking_detail = ArrayList<ShoppingListModel>()
    var channel_id: String = ""
    var comment: String = ""
    var contact_name: String = ""
    var contact_number: String = ""
    var dispute_date: String = ""
    var refute_reason: String = ""
    var refute_date: String = ""
    var is_refuted: String = ""
    var products = ArrayList<ShoppingListModel>()
    var comments = ArrayList<CommentModel>()

    constructor(parcel: Parcel) : this() {
        req_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        list_type = parcel.readString().toString()
        id = parcel.readString().toString()
        address = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        amount = parcel.readString().toString()
        created_at = parcel.readString().toString()
        status = parcel.readString().toString()
        listingname = parcel.readString().toString()
        completed_date = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        buyer_id = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        seller_image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        reputation = parcel.readString().toString()
        credits = parcel.readString().toString()
        reason = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        type = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        level = parcel.readString().toString()
        picking_detail = parcel.readParcelable(DeliveryZoneModel::class.java.classLoader)!!
        channel_id = parcel.readString().toString()
        comment = parcel.readString().toString()
        contact_name = parcel.readString().toString()
        contact_number = parcel.readString().toString()
        dispute_date = parcel.readString().toString()
        refute_reason = parcel.readString().toString()
        refute_date = parcel.readString().toString()
        is_refuted = parcel.readString().toString()
        products = parcel.createTypedArrayList(ShoppingListModel)!!
        comments = parcel.createTypedArrayList(CommentModel)!!
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(req_id)
        parcel.writeString(list_id)
        parcel.writeString(list_type)
        parcel.writeString(id)
        parcel.writeString(address)
        parcel.writeString(delivery_type)
        parcel.writeString(amount)
        parcel.writeString(created_at)
        parcel.writeString(status)
        parcel.writeString(listingname)
        parcel.writeString(completed_date)
        parcel.writeString(payment_methods)
        parcel.writeString(seller_id)
        parcel.writeString(buyer_id)
        parcel.writeString(seller_name)
        parcel.writeString(seller_image)
        parcel.writeString(seller_level)
        parcel.writeString(reputation)
        parcel.writeString(credits)
        parcel.writeString(reason)
        parcel.writeString(cat_id)
        parcel.writeString(type)
        parcel.writeString(comission_per)
        parcel.writeString(level)
        parcel.writeParcelable(picking_detail, flags)
        parcel.writeString(channel_id)
        parcel.writeString(comment)
        parcel.writeString(contact_name)
        parcel.writeString(contact_number)
        parcel.writeString(dispute_date)
        parcel.writeString(refute_reason)
        parcel.writeString(refute_date)
        parcel.writeString(is_refuted)
        parcel.writeTypedList(products)
        parcel.writeTypedList(comments)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }


}