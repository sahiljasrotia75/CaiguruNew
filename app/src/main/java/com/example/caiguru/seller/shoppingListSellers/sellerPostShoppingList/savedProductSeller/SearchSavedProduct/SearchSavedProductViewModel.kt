package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SearchSavedProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import java.io.File

class SearchSavedProductViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = SearchSavedProductRepository(application)

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }

    fun mSucessfulSavedlist(): MutableLiveData<ArrayList<PostShoppingModel>> {

        return mrepo.mSucessfulSavedlist()
    }



    fun getSearchData(Search: String, categoryData: SellerCategoryModel?, page: Int) {
        mrepo.getSearchData(Search,categoryData,page)
    }


}
