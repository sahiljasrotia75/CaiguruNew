package com.example.caiguru.buyer.homeBuyers.tagUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class TagViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: TagRepository = TagRepository(application)

    //****************get_tag_users api list************//
    fun getTagUser(postId: String, search: String, page: Int) {

        mRepo.getTagUser(postId, search, page.toString())
    }

    fun mSuccessfulgetTagUser(): MutableLiveData<ArrayList<TagModel>> {


        return mRepo.mSuccessfulgetTagUser()
    }

    fun mError(): MutableLiveData<String> {

        return mRepo.mError()
    }


    //*******************post_tag_user***************//
    fun postTaggedUser(tagUserId: String, postId: String, status: String) {

        mRepo.postTaggedUser(tagUserId, postId,status)
    }

    fun mSucessfulPostTag(): MutableLiveData<String> {

        return  mRepo.mSucessfulPostTag()
    }
    fun mErrorPostTag(): MutableLiveData<String> {

        return  mRepo.mErrorPostTag()
    }

}
