package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import java.io.File

class PostShoppingViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = PostShoppingRepository(application)

    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {


        return mrepo.sendData()


    }
    //************show credit deduct
    fun showcreditToDeduct():String {
        return mrepo.showcreditToDeduct()
    }
    //update zone
    fun sendDataDeliveryZone(): MutableLiveData<ArrayList<DeliveryZoneModel>> {


        return mrepo.sendDataDeliveryZone()


    }

//    fun getNewAddressPlus(): MutableLiveData<ArrayList<DeliveryZoneModel>> {
//
//        return mrepo.getaddAdress()
//
//    }
    //..........set the image of the dialog box............//

    fun profileImage(uri: File) {

        mrepo.setProfileImage(uri)
    }

    //get the data
    fun getdataDialogSucessful(): MutableLiveData<DialogModel> {
        return mrepo.getdatasucessful()
    }

    //error observer
    fun geterrorDialog(): MutableLiveData<String> {

        return mrepo.errorget()
    }


    //..................model come from the dialog to create the list

    fun ShoppingListData(
        model: List<PostShoppingModel>,
        isPrefilling: Boolean,
        deleteArray: String
    ) {
        mrepo.ShoppingListData(model,isPrefilling,deleteArray)

    }


    fun addNewAddress(deliveryZoneModel: DeliveryZoneModel) {
        return mrepo.addNewAddress(deliveryZoneModel)
    }


    fun UpdateData(deliveryZoneModel: DeliveryZoneModel, position: Int) {

        mrepo.UpdateData(deliveryZoneModel, position)
    }


    //update the shopping list at the same position

    fun editListUpdate(
        adapterShoppingModel: PostShoppingModel,
        adapterShoppingPosition: Int,
        isPrefilling: Boolean,
        nameEdited: String
    ) {
        mrepo.editListUpdate(adapterShoppingModel, adapterShoppingPosition,isPrefilling,nameEdited)
    }

    //set the data to the repository
    fun createShoppingList(
        listingname: String,
        categoryModelList: SellerCategoryModel,
        delivery_zone: String,
        delivery_days: String,
        pickup_details: String,
        minimumPurchaseAmount: String,
        commissionPer: Int,
        product_details: String,
        listId: ListParentModel,
        productShoppingList: java.util.ArrayList<PostShoppingModel>,
        paymentMetods: String,
        whichTypeData: String
    ) {
        mrepo.createShoppingList(
            listingname, categoryModelList, delivery_zone, delivery_days, pickup_details,
            minimumPurchaseAmount, commissionPer, product_details,listId,productShoppingList,paymentMetods,whichTypeData
        )

    }

    //send the shoppinglist
    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
        return mrepo.mSucessfulCreateShoppingList()
    }

    fun mFailureShoppingError(): MutableLiveData<String> {
        return mrepo.mFailureShoppingError()
    }

    fun deleteProduct(position: Int, list: java.util.ArrayList<PostShoppingModel>) {
        mrepo.deleteProduct(position,list)

    }

//    fun getList(list: ListParentModel) {
//
//        return mrepo.getListApi(list)
//    }


}
