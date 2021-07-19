package com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel

class ClosedListViewModel(application: Application) : AndroidViewModel(application) {


    var repo = ClosedListRepository(application)

    fun getSellerCloseList(page: String) {

        repo.getSellerCloseList(page)
    }

    //get response
    fun mSucessful(): MutableLiveData<ArrayList<ListParentModel>> {

        return repo.mSucessful()
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return repo.mFailure()
    }

    //open again api data send
    fun openAgainList(
        mData: ListParentModel,
        position: Int
    ) {
        repo.openAgainList(mData,position)
    }
    //open agin list recive data

    fun mSucessfulOpenAgainList(): MutableLiveData<String> {

        return repo.mSucessfulCloseList1()
    }

    fun mFailureOpenAgainList(): MutableLiveData<String> {

        return repo.mFailureOpenAgainList()
    }


}
