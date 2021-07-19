package com.example.caiguru.buyer.homeBuyers.homeBuyer

//products = parcel.createTypedArrayList(DialogModel.CREATOR)
//parcel.writeTypedList(products)
import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel

class HomeModel() : Parcelable {

    var post_id: String = ""
    var is_like: String = ""
    var req_id: String = ""
    var list_id: String = ""
    var cat_id: String = ""
    var created_at: String = ""
    var list_type: String = ""
    var seller_id: String = ""
    var likes: String = ""
    var comments: String = ""
    var is_shared: String = ""
    var shared_by: String = ""
    var shared_to: String = ""
    var is_processing: String = ""
    var listingname: String = ""
    var payment_methods: String = ""
    var is_view: String = ""
    var is_tag: String = ""
    var seller_name: String = ""
    var seller_image: String = ""
    var seller_level: String = ""
    var seller_reputation: String = ""
    var delivery_type: String = ""
    var comission_per: String = ""
    var is_buy_btn_show: String = ""
    var is_comment: String = ""
    var imagewhite: Int = 0
    var imagepurple: Int = 0
    var hasSelected: Boolean = false
    var shared_by_name: String = ""
    var shared_by_image: String = ""
    var shared_by_level: String = ""
    var distance: String = ""
    var s_count: String = ""
    var shared_by_reputation: String = ""
    var products = ArrayList<DialogModel>()
    var shared_at = ""


    constructor(parcel: Parcel) : this() {
        post_id = parcel.readString().toString()
        is_like = parcel.readString().toString()
        req_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        created_at = parcel.readString().toString()
        list_type = parcel.readString().toString()
        seller_id = parcel.readString().toString()
        likes = parcel.readString().toString()
        comments = parcel.readString().toString()
        is_shared = parcel.readString().toString()
        shared_by = parcel.readString().toString()
        shared_to = parcel.readString().toString()
        is_processing = parcel.readString().toString()
        listingname = parcel.readString().toString()
        payment_methods = parcel.readString().toString()
        is_view = parcel.readString().toString()
        is_tag = parcel.readString().toString()
        seller_name = parcel.readString().toString()
        seller_image = parcel.readString().toString()
        seller_level = parcel.readString().toString()
        seller_reputation = parcel.readString().toString()
        delivery_type = parcel.readString().toString()
        comission_per = parcel.readString().toString()
        is_buy_btn_show = parcel.readString().toString()
        is_comment = parcel.readString().toString()
        imagewhite = parcel.readInt()
        imagepurple = parcel.readInt()
        hasSelected = parcel.readByte() != 0.toByte()
        shared_by_name = parcel.readString().toString()
        shared_by_image = parcel.readString().toString()
        shared_by_level = parcel.readString().toString()
        distance = parcel.readString().toString()
        s_count = parcel.readString().toString()
        shared_by_reputation = parcel.readString().toString()
        shared_at = parcel.readString().toString()
        products = parcel.createTypedArrayList(DialogModel.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(post_id)
        parcel.writeString(is_like)
        parcel.writeString(req_id)
        parcel.writeString(list_id)
        parcel.writeString(cat_id)
        parcel.writeString(created_at)
        parcel.writeString(list_type)
        parcel.writeString(seller_id)
        parcel.writeString(likes)
        parcel.writeString(comments)
        parcel.writeString(is_shared)
        parcel.writeString(shared_by)
        parcel.writeString(shared_to)
        parcel.writeString(is_processing)
        parcel.writeString(listingname)
        parcel.writeString(payment_methods)
        parcel.writeString(is_view)
        parcel.writeString(is_tag)
        parcel.writeString(seller_name)
        parcel.writeString(seller_image)
        parcel.writeString(seller_level)
        parcel.writeString(seller_reputation)
        parcel.writeString(delivery_type)
        parcel.writeString(comission_per)
        parcel.writeString(is_buy_btn_show)
        parcel.writeString(is_comment)
        parcel.writeInt(imagewhite)
        parcel.writeInt(imagepurple)
        parcel.writeByte(if (hasSelected) 1 else 0)
        parcel.writeString(shared_by_name)
        parcel.writeString(shared_by_image)
        parcel.writeString(shared_by_level)
        parcel.writeString(distance)
        parcel.writeString(s_count)
        parcel.writeString(shared_by_reputation)
        parcel.writeString(shared_at)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeModel> {
        override fun createFromParcel(parcel: Parcel): HomeModel {
            return HomeModel(parcel)
        }

        override fun newArray(size: Int): Array<HomeModel?> {
            return arrayOfNulls(size)
        }
    }


}















