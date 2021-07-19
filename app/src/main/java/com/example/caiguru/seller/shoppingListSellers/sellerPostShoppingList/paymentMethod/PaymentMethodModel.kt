package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod

import android.os.Parcel
import android.os.Parcelable
import com.example.caiguru.R

class PaymentMethodModel() : Parcelable {

    var isSelected = false
    var paymentId: String = ""
    var paymentName: String = ""
    var SelectedDrawable:Int = R.drawable.ic_selectedcheckbox_yellow
    var UnSelectedDrawable:Int = R.drawable.radiobutton_self_pickup_selector

    constructor(parcel: Parcel) : this() {
        isSelected = parcel.readByte() != 0.toByte()
        paymentId = parcel.readString().toString()
        paymentName = parcel.readString().toString()
        SelectedDrawable = parcel.readInt()
        UnSelectedDrawable = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(paymentId)
        parcel.writeString(paymentName)
        parcel.writeInt(SelectedDrawable)
        parcel.writeInt(UnSelectedDrawable)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentMethodModel> {
        override fun createFromParcel(parcel: Parcel): PaymentMethodModel {
            return PaymentMethodModel(parcel)
        }

        override fun newArray(size: Int): Array<PaymentMethodModel?> {
            return arrayOfNulls(size)
        }
    }

}