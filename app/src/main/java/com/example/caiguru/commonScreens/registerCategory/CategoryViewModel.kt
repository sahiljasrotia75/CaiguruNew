package com.example.caiguru.commonScreens.registerCategory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.registerActivity.RegisterModel

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    fun setData(data: RegisterModel) {
        mrepo.setData(data)

    }


    var mrepo = CategoryRepository(application)

    //status true observer
    fun getdata(): MutableLiveData<CategoryModel> {
        return mrepo.getdata()
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {

        return mrepo.errorget()
    }

    fun update_categories(updateCategories: String) {
        mrepo.update_categories(updateCategories)
    }

    //status true observer
    fun getdataupdate(): MutableLiveData<CategoryModel> {
        return mrepo.getdataupdate()
    }

    //error observer
    fun errorgetupdate(): MutableLiveData<ErrorModel> {

        return mrepo.errorgett()
    }
}
