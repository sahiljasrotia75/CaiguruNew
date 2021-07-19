package com.example.caiguru.commonScreens.chat.archiveChat.archiveChatMessage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.MessageItem

class ArchiveChatMessageViewModel(application: Application) : AndroidViewModel(application){
    fun getMessageList(): LiveData<List<MessageItem>> {
        return mRepo.getMessages()
    }

    fun postModel(model: ModelChat) {
        mRepo.postModel(model)
    }

    var mRepo = ArchiveChatMessageRepository(application)

}