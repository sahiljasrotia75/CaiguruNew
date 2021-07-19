package com.example.caiguru.commonScreens.chat.archiveChat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.caiguru.commonScreens.chat.ChatParent
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class ArchiveChatViewModel(application: Application) : AndroidViewModel(application){
    var mRepo = ArchiveChatRepository(application)

    fun observeChatList(): LiveData<List<ChatParent>> {
       return mRepo.observeChats()
    }

    fun observeError(): LiveData<ErrorModel> {
       return mRepo.observeError()
    }

    fun getChannels() {
        mRepo.getChannels()
    }

}