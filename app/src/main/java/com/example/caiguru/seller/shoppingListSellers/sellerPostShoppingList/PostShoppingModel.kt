package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.os.Parcel
import android.os.Parcelable

class PostShoppingModel() : Parcelable {


    var page: Int=0
    var setProductPosition=0
    var name: String = ""
    var usedProds: String = ""
    var searchCaseShowBtn: String = ""
    var cat_id: String = ""
    var list_id: String = ""
    var status: String = ""
    var total: String = ""//static used only
    var unit: String = ""
    var price: String = ""
    var image: String = ""
    var LocalTotal: String = ""
    var qty: String = ""
    var id: String = ""
    var saved_product_id: String = ""
    var product_id: String = ""
    var isProductSelected = false
    var partialComission: String = ""
    var priceWithComission: String = ""
    var PriceComission: String=""
    var isUploadedImage:Boolean = false
    var progressBar:Boolean = false
    var duplicateProduct:Boolean = false
    var count: Int = 0
    constructor(parcel: Parcel) : this() {
        page = parcel.readInt()
        setProductPosition = parcel.readInt()
        name = parcel.readString().toString()
        usedProds = parcel.readString().toString()
        searchCaseShowBtn = parcel.readString().toString()
        cat_id = parcel.readString().toString()
        list_id = parcel.readString().toString()
        status = parcel.readString().toString()
        total = parcel.readString().toString()
        unit = parcel.readString().toString()
        price = parcel.readString().toString()
        image = parcel.readString().toString()
        LocalTotal = parcel.readString().toString()
        qty = parcel.readString().toString()
        id = parcel.readString().toString()
        saved_product_id = parcel.readString().toString()
        product_id = parcel.readString().toString()
        isProductSelected = parcel.readByte() != 0.toByte()
        partialComission = parcel.readString().toString()
        priceWithComission = parcel.readString().toString()
        PriceComission = parcel.readString().toString()
        isUploadedImage = parcel.readByte() != 0.toByte()
        progressBar = parcel.readByte() != 0.toByte()
        duplicateProduct = parcel.readByte() != 0.toByte()
        count = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(page)
        parcel.writeInt(setProductPosition)
        parcel.writeString(name)
        parcel.writeString(usedProds)
        parcel.writeString(searchCaseShowBtn)
        parcel.writeString(cat_id)
        parcel.writeString(list_id)
        parcel.writeString(status)
        parcel.writeString(total)
        parcel.writeString(unit)
        parcel.writeString(price)
        parcel.writeString(image)
        parcel.writeString(LocalTotal)
        parcel.writeString(qty)
        parcel.writeString(id)
        parcel.writeString(saved_product_id)
        parcel.writeString(product_id)
        parcel.writeByte(if (isProductSelected) 1 else 0)
        parcel.writeString(partialComission)
        parcel.writeString(priceWithComission)
        parcel.writeString(PriceComission)
        parcel.writeByte(if (isUploadedImage) 1 else 0)
        parcel.writeByte(if (progressBar) 1 else 0)
        parcel.writeByte(if (duplicateProduct) 1 else 0)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostShoppingModel> {
        override fun createFromParcel(parcel: Parcel): PostShoppingModel {
            return PostShoppingModel(parcel)
        }

        override fun newArray(size: Int): Array<PostShoppingModel?> {
            return arrayOfNulls(size)
        }
    }


}