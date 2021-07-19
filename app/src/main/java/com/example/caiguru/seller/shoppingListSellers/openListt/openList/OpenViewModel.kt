package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel

class OpenViewModel(application: Application):AndroidViewModel(application) {

    var repo : OpenListRepository = OpenListRepository(application)

    fun getSellerShoppingList(page: String) {
        return repo.getSeller(page)
    }

    fun sendList(): MutableLiveData<ArrayList<ListParentModel>> {
        return repo.sendListData()

    }
    fun sendListDataFailure(): MutableLiveData<ErrorModel> {
        return repo.sendListDataFailure()

    }



    fun closeList(mData: java.util.ArrayList<ListParentModel>, position: Int) {

        repo.closeList(mData,position)
    }


    fun mSucessfullcloseList(): MutableLiveData<String> {

        return repo. mSucessfullcloseList()
    }

    fun mFailureCloseList(): MutableLiveData<String> {

        return repo. mFailureCloseList()
    }

    fun showOpenList(position: Int) {

        repo.showOpenList(position)
    }

    fun SetPlayPauseApi(list: DialogModel) {

        repo.SetPlayPauseApi(list)
    }

    fun mSucessfulPlayPause(): MutableLiveData<String> {
        return repo.mSucessfulPlayPause()
    }

}
