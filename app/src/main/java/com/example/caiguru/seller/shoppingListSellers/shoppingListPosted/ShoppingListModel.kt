package com.example.caiguru.seller.shoppingListSellers.shoppingListPosted

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel

class ShoppingListModel() : Parcelable {

    var saved_product_id: String = ""
    var usedProds: String = ""
    var product_id: String = ""
    var name: String = ""
    var id: String = ""
    var cat_id: String = ""
    var image: String = ""
    var unit: String = ""
    var priceWithComission: String = ""
    var price: String = ""
    var qty: String = ""
    var listingname: String = ""
    var isExpanded = false
    var isProductSelected = false
    var count: Int = 0
    var page = 1
    var product_details = ArrayList<DialogModel>()
    var products = ArrayList<PostShoppingModel>()

    constructor(parcel: Parcel) : this() {
        saved_product_id = parcel.readString().toString()
        usedProds = parcel.readString().toString()
        product_id = parcel.readString().toString()
        name = parcel.readString().toString()
        id = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        image = parcel.readString().toString()
        unit = parcel.readString().toString()
        priceWithComission = parcel.readString().toString()
        price = parcel.readString().toString()
        qty = parcel.readString().toString()
        listingname = parcel.readString().toString()
        isExpanded = parcel.readByte() != 0.toByte()
        isProductSelected = parcel.readByte() != 0.toByte()
        count = parcel.readInt()
        product_details = parcel.createTypedArrayList(DialogModel)!!
        products = parcel.createTypedArrayList(PostShoppingModel)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(saved_product_id)
        parcel.writeString(usedProds)
        parcel.writeString(product_id)
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(cat_id)
        parcel.writeString(image)
        parcel.writeString(unit)
        parcel.writeString(priceWithComission)
        parcel.writeString(price)
        parcel.writeString(qty)
        parcel.writeString(listingname)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeByte(if (isProductSelected) 1 else 0)
        parcel.writeInt(count)
        parcel.writeTypedList(product_details)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShoppingListModel> {
        override fun createFromParcel(parcel: Parcel): ShoppingListModel {
            return ShoppingListModel(parcel)
        }

        override fun newArray(size: Int): Array<ShoppingListModel?> {
            return arrayOfNulls(size)
        }
    }


}