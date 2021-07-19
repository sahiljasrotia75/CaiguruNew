package com.example.caiguru.commonScreens.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class ChatViewModel(application: Application) : AndroidViewModel(application){
    var mRepo = ChatRepository(application)

    fun observeChatList(): LiveData<List<ChatParent>> {
       return mRepo.observeChats()
    }

    fun observeLatestConnections(): LiveData<List<ModelChat>> {
       return mRepo.observeLatestConnections()
    }

    fun observeError(): LiveData<ErrorModel> {
       return mRepo.observeError()
    }

    fun getChannels() {
        mRepo.getChannels()
    }

}