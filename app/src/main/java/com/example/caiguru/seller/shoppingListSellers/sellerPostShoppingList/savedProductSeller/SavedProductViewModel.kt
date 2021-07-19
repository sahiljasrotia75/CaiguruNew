package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class SavedProductViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = SavedProductRepository(application)

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }

    fun mSucessfulSavedlist(): MutableLiveData<ArrayList<ShoppingListModel>> {

        return mrepo.mSucessfulSavedlist()
    }

    fun getSavedProductData(categoryData: SellerCategoryModel?, page: Int) {
        mrepo.getSavedProductData(categoryData,page)

    }


}
