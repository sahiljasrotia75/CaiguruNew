package com.example.caiguru.commonScreens.chat.chatMessage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.caiguru.commonScreens.chat.ModelChat
import java.io.File

class ChatMessageViewModel(application: Application) : AndroidViewModel(application){

    var mRepo = ChatMessageRepository(application)

    fun getMessageList(): LiveData<List<MessageItem>> {
        return mRepo.getMessages()
    }

    fun sendMessage(messageText: String, time: Long) {
        mRepo.sendMessage(messageText,time)
    }

    fun getMessageFirstTime(model: ModelChat) {
        mRepo.getMessageFirstTime(model)
    }

    fun postSelectedImage(picturePath: File) {
        mRepo.uploadImage(picturePath)
    }

    fun getMessagesConinous(model: ModelChat) {

        mRepo.getMessagesConinous(model)
    }

    fun stopBackGroundService() {

        mRepo.stopBackGroundService()
    }


}