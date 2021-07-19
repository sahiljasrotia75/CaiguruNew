package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel

class ViewDetailModel() :Parcelable{
    var seller_id: String = ""
    var list_id: String = ""
    var name: String = ""
    var image: String = ""
    var level: String = ""
    var id: String = ""
    var cat_id: String = ""
    var listingname: String = ""
    var payment_methods: String = ""
    var pickup_city_id: String = ""
    var comission_per: String = ""
    var created_at: String = ""
    var minimum_purchase_amount: String = ""
    var delivery_type: String = ""
    var is_buy_btn_show: String = ""
    var profile_lat: String = ""
    var profile_long: String = ""
    var profile_address: String = ""
    var distance: String = ""
    //array
    var delivery_daytime=ArrayList<DaysParentModel>()
    var product_details=ArrayList<PostShoppingModel>()
    //obj
    var pickup_details=DeliveryZoneModel()

    constructor(parcel: Parcel) : this() {
        seller_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        name = parcel.readString().toString()
        image = parcel.readString().toString()
        level = parcel.readString().toString()
        id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        listingname = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        pickup_city_id = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        created_at = parcel.readString().toString()
        minimum_purchase_amount = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        is_buy_btn_show = parcel.readString().toString()
        profile_lat = parcel.readString().toString()
        profile_long = parcel.readString().toString()
        profile_address = parcel.readString().toString()
        distance = parcel.readString().toString()
        pickup_details = parcel.readParcelable(DeliveryZoneModel::class.java.classLoader)!!
        delivery_daytime = parcel.createTypedArrayList(DaysParentModel.CREATOR)!!
        product_details = parcel.createTypedArrayList(PostShoppingModel.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(seller_id)
        parcel.writeString(list_id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(level)
        parcel.writeString(id)
        parcel.writeString(cat_id)
        parcel.writeString(listingname)
        parcel.writeString(payment_methods)
        parcel.writeString(pickup_city_id)
        parcel.writeString(comission_per)
        parcel.writeString(created_at)
        parcel.writeString(minimum_purchase_amount)
        parcel.writeString(delivery_type)
        parcel.writeString(is_buy_btn_show)
        parcel.writeString(profile_lat)
        parcel.writeString(profile_long)
        parcel.writeString(profile_address)
        parcel.writeString(distance)
        parcel.writeParcelable(pickup_details, flags)
        parcel.writeTypedList(delivery_daytime)
        parcel.writeTypedList(product_details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViewDetailModel> {
        override fun createFromParcel(parcel: Parcel): ViewDetailModel {
            return ViewDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<ViewDetailModel?> {
            return arrayOfNulls(size)
        }
    }
}