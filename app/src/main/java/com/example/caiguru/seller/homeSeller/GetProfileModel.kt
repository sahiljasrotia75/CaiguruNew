package com.example.caiguru.seller.homeSeller

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.ViewDetailModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.ReviewsModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class GetProfileModel() : Parcelable {

    var count: Int = 0
    var finalizeCount: String = ""
    var status: String = ""
    var unreadcount: String = ""
    var unreadcount_seller: String = ""
    var name: String = ""
    var email: String = ""
    var image: String = ""
    var credits: String = ""
    var earned_credits: String = ""
    var reff_code: String = ""
    var categories: String = ""
    var seller_level: String = ""
    var seller_points: String = ""
    var buyer_points: String = ""
    var seller_active_status: String = ""
    var sharelink: String = ""
    var buyer_level: String = ""
    var seller_maximum_commission: String = ""
    var seller_minimum_commission: String = ""
    var per_product_credits: String = ""
    var sel_cities = ArrayList<ChooseSellerShoppingModel>()
    var buyer_reputation: String = ""
    var seller_reputation: String = ""
    var full_address: String = ""
    var lat: String = ""
    var long: String = ""
    var read_notifications: String = ""
    var followings: String = ""
    var followers: String = ""
    var msg: String = ""
    var points: String = ""
    var level: String = ""
    var isFollowed: String = ""
    var reputation: String = ""
    var type: String = ""
    var completed_orders: String = ""
    var follow_status: String = ""
    var user_id: String = ""
    var redeem_credit_limit: String = ""
    var plateform_commission: String = ""
    var enable_seller_commission: String = ""
    var sharelink_android: String = ""
    var is_seller_first_time: String = ""
    var listcount: String = ""
    var is_purchase_button_show = ""
    var is_purchase_disable = ""
    var social_type = ""
    var lists = ArrayList<ShoppingListModel>()
    var reviews = ArrayList<ReviewsModel>()
    var products = ArrayList<ShoppingListModel>()
    var listInfo = ViewDetailModel()

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        finalizeCount = parcel.readString().toString()
        status = parcel.readString().toString()
        unreadcount = parcel.readString().toString()
        unreadcount_seller = parcel.readString().toString()
        name = parcel.readString().toString()
        email = parcel.readString().toString()
        image = parcel.readString().toString()
        credits = parcel.readString().toString()
        earned_credits = parcel.readString().toString()
        reff_code = parcel.readString().toString()
        categories = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        seller_points = parcel.readString().toString()
        buyer_points = parcel.readString().toString()
        seller_active_status = parcel.readString().toString()
        sharelink = parcel.readString().toString()
        buyer_level = parcel.readString().toString()
        seller_maximum_commission = parcel.readString().toString()
        seller_minimum_commission = parcel.readString().toString()
        per_product_credits = parcel.readString().toString()
        buyer_reputation = parcel.readString().toString()
        seller_reputation = parcel.readString().toString()
        full_address = parcel.readString().toString()
        lat = parcel.readString().toString()
        long = parcel.readString().toString()
        read_notifications = parcel.readString().toString()
        followings = parcel.readString().toString()
        followers = parcel.readString().toString()
        msg = parcel.readString().toString()
        points = parcel.readString().toString()
        level = parcel.readString().toString()
        isFollowed = parcel.readString().toString()
        reputation = parcel.readString().toString()
        type = parcel.readString().toString()
        completed_orders = parcel.readString().toString()
        follow_status = parcel.readString().toString()
        user_id = parcel.readString().toString()
        redeem_credit_limit = parcel.readString().toString()
        plateform_commission = parcel.readString().toString()
        enable_seller_commission = parcel.readString().toString()
        sharelink_android = parcel.readString().toString()
        is_seller_first_time = parcel.readString().toString()
        listcount = parcel.readString().toString()
        is_purchase_button_show = parcel.readString().toString()
        is_purchase_disable = parcel.readString().toString()
        social_type = parcel.readString().toString()
        lists = parcel.createTypedArrayList(ShoppingListModel)!!
        reviews = parcel.createTypedArrayList(ReviewsModel)!!
        products = parcel.createTypedArrayList(ShoppingListModel)!!
        listInfo = parcel.readParcelable(ViewDetailModel::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeString(finalizeCount)
        parcel.writeString(status)
        parcel.writeString(unreadcount)
        parcel.writeString(unreadcount_seller)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(image)
        parcel.writeString(credits)
        parcel.writeString(earned_credits)
        parcel.writeString(reff_code)
        parcel.writeString(categories)
        parcel.writeString(seller_level)
        parcel.writeString(seller_points)
        parcel.writeString(buyer_points)
        parcel.writeString(seller_active_status)
        parcel.writeString(sharelink)
        parcel.writeString(buyer_level)
        parcel.writeString(seller_maximum_commission)
        parcel.writeString(seller_minimum_commission)
        parcel.writeString(per_product_credits)
        parcel.writeString(buyer_reputation)
        parcel.writeString(seller_reputation)
        parcel.writeString(full_address)
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeString(read_notifications)
        parcel.writeString(followings)
        parcel.writeString(followers)
        parcel.writeString(msg)
        parcel.writeString(points)
        parcel.writeString(level)
        parcel.writeString(isFollowed)
        parcel.writeString(reputation)
        parcel.writeString(type)
        parcel.writeString(completed_orders)
        parcel.writeString(follow_status)
        parcel.writeString(user_id)
        parcel.writeString(redeem_credit_limit)
        parcel.writeString(plateform_commission)
        parcel.writeString(enable_seller_commission)
        parcel.writeString(sharelink_android)
        parcel.writeString(is_seller_first_time)
        parcel.writeString(listcount)
        parcel.writeString(is_purchase_button_show)
        parcel.writeString(is_purchase_disable)
        parcel.writeString(social_type)
        parcel.writeTypedList(lists)
        parcel.writeTypedList(reviews)
        parcel.writeTypedList(products)
        parcel.writeParcelable(listInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetProfileModel> {
        override fun createFromParcel(parcel: Parcel): GetProfileModel {
            return GetProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<GetProfileModel?> {
            return arrayOfNulls(size)
        }
    }


}