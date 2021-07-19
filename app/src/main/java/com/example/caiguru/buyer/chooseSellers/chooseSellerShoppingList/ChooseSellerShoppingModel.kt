package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel

class ChooseSellerShoppingModel() : Parcelable {
    var cashOnDelivery: String = ""
    var name: String = ""
    var image: String = ""
    var payment_methods: String = ""
    var level: String = ""
    var id: String = ""
    var cat_id: String = ""
    var list_id: String = ""
    var listingname: String = ""
    var comission_per: String = ""
    var seller_id: String = ""
    var created_at: String = ""
    var distance: String = ""
    var minimum_purchase_amount: String = ""
    var PartialComission: String = ""
    var delivery_type: Int = 0
    var isExpanded = false

    //static data
    var total: String = ""
    var credits: String = ""
    var amount: String = ""
    var product_details = ArrayList<PostShoppingModel>()
    var delivery_daytime = ArrayList<DaysParentModel>() //type 2
    var days = ArrayList<DaysParentModel>() //type 2
    var products = ArrayList<PostShoppingModel>()
    var pickup_details = DeliveryZoneModel()//pickup type 1

    constructor(parcel: Parcel) : this() {
        cashOnDelivery = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        level = parcel.readString().toString()
        id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        created_at = parcel.readString().toString()
        distance = parcel.readString().toString()
        minimum_purchase_amount = parcel.readString().toString()
        PartialComission = parcel.readString().toString()
        delivery_type = parcel.readInt()
        isExpanded = parcel.readByte() != 0.toByte()
        total = parcel.readString().toString()
        credits = parcel.readString().toString()
        amount = parcel.readString().toString()
        product_details = parcel.createTypedArrayList(PostShoppingModel.CREATOR)!!
        delivery_daytime = parcel.createTypedArrayList(DaysParentModel.CREATOR)!!
        days = parcel.createTypedArrayList(DaysParentModel.CREATOR)!!
        products = parcel.createTypedArrayList(PostShoppingModel.CREATOR)!!
        pickup_details = parcel.readParcelable(DeliveryZoneModel::class.java.classLoader)!!
    }

    //    constructor(parcel: Parcel) : this() {
//        cashOnDelivery = parcel.readString()
//        name = parcel.readString()
//        image = parcel.readString()
//        payment_methods = parcel.readString()
//        level = parcel.readString()
//        id = parcel.readString()
//        cat_id = parcel.readString()
//        list_id = parcel.readString()
//        listingname = parcel.readString()
//        comission_per = parcel.readString()
//        seller_id = parcel.readString()
//        created_at = parcel.readString()
//        distance = parcel.readString()
//        minimum_purchase_amount = parcel.readString()
//        PartialComission = parcel.readString()
//        delivery_type = parcel.readInt()
//        product_details = parcel.createTypedArrayList(PostShoppingModel.CREATOR)
//        delivery_daytime = parcel.createTypedArrayList(DaysParentModel.CREATOR)
//        days = parcel.createTypedArrayList(DaysParentModel.CREATOR)
//        products = parcel.createTypedArrayList(PostShoppingModel.CREATOR)
//        pickup_details = parcel.readParcelable(DeliveryZoneModel::class.java.classLoader)
//        isExpanded = parcel.readByte() != 0.toByte()
//        total = parcel.readString()
//        credits = parcel.readString()
//        amount = parcel.readString()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(cashOnDelivery)
//        parcel.writeString(name)
//        parcel.writeString(image)
//        parcel.writeString(payment_methods)
//        parcel.writeString(level)
//        parcel.writeString(id)
//        parcel.writeString(cat_id)
//        parcel.writeString(list_id)
//        parcel.writeString(listingname)
//        parcel.writeString(comission_per)
//        parcel.writeString(seller_id)
//        parcel.writeString(created_at)
//        parcel.writeString(distance)
//        parcel.writeString(minimum_purchase_amount)
//        parcel.writeString(PartialComission)
//        parcel.writeInt(delivery_type)
//        parcel.writeTypedList(product_details)
//        parcel.writeTypedList(delivery_daytime)
//        parcel.writeTypedList(days)
//        parcel.writeTypedList(products)
//        parcel.writeParcelable(pickup_details, flags)
//        parcel.writeByte(if (isExpanded) 1 else 0)
//        parcel.writeString(total)
//        parcel.writeString(credits)
//        parcel.writeString(amount)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<ChooseSellerShoppingModel> {
//        override fun createFromParcel(parcel: Parcel): ChooseSellerShoppingModel {
//            return ChooseSellerShoppingModel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<ChooseSellerShoppingModel?> {
//            return arrayOfNulls(size)
//        }
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cashOnDelivery)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(payment_methods)
        parcel.writeString(level)
        parcel.writeString(id)
        parcel.writeString(cat_id)
        parcel.writeString(list_id)
        parcel.writeString(listingname)
        parcel.writeString(comission_per)
        parcel.writeString(seller_id)
        parcel.writeString(created_at)
        parcel.writeString(distance)
        parcel.writeString(minimum_purchase_amount)
        parcel.writeString(PartialComission)
        parcel.writeInt(delivery_type)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeString(total)
        parcel.writeString(credits)
        parcel.writeString(amount)
        parcel.writeTypedList(product_details)
        parcel.writeTypedList(delivery_daytime)
        parcel.writeTypedList(days)
        parcel.writeTypedList(products)
        parcel.writeParcelable(pickup_details, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChooseSellerShoppingModel> {
        override fun createFromParcel(parcel: Parcel): ChooseSellerShoppingModel {
            return ChooseSellerShoppingModel(parcel)
        }

        override fun newArray(size: Int): Array<ChooseSellerShoppingModel?> {
            return arrayOfNulls(size)
        }
    }


}