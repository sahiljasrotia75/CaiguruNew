package com.example.caiguru.commonScreens.blockedUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class BlockedUserViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = BlockedUserRepository(application)


    fun getallBlockedSerData(page: String) {
        mrepo.get_block_users(page)

    }
    //********get all data block user
    fun mSucessfulgetAllDataBlockedUser(): MutableLiveData<ArrayList<BlockedUserModel>> {

        return  mrepo.mSucessfulgetAllDataBlockedUser()
    }

    fun mFailureAllDataBlockedUser(): MutableLiveData<String> {

        return  mrepo.mFailureAllDataBlockedUser()
    }
    //*********unblocked user
    //get data
    fun getSucessfulBlockedUser(): MutableLiveData<String> {
        return  mrepo.getSucessfulBlockedUser()
    }

    //error observer
    fun errorgetBlockedUser(): MutableLiveData<String> {

        return  mrepo.errorgetBlockedUser()
    }

    fun blockedUser(list: BlockedUserModel) {

        mrepo.blockedUser(list)
    }
}
