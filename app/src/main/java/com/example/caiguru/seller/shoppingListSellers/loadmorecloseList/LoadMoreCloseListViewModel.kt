package com.example.caiguru.seller.shoppingListSellers.loadmorecloseList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel

class LoadMoreCloseListViewModel(application: Application):AndroidViewModel(application) {
    var repo: LoadMoreCloseListRepository = LoadMoreCloseListRepository(application)
    fun getallcloselist(catId: String, page: Int) {
        repo.getallcloselist(catId,page.toString())
    }

    //get response
    fun mSucessful(): MutableLiveData<ArrayList<ListParentModel>> {

        return repo.mSucessful()
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return repo.mFailure()
    }
}
