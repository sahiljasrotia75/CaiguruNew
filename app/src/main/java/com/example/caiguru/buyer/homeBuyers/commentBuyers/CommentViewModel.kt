package com.example.caiguru.buyer.homeBuyers.commentBuyers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CommentViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: CommentRepository = CommentRepository(application)


    fun getCommentFeed(postId: String, page: Int) {

        mRepo.getCommentFeed(postId, page.toString())
    }

    fun mSuccessfulgetFeedComment(): MutableLiveData<ArrayList<CommentModel>> {


        return mRepo.mSuccessfulgetFeedComment()
    }

    fun mError(): MutableLiveData<String> {

        return mRepo.mError()
    }


    //**************************set feed comment**********//
    fun setFeedComment(postId: String, comment: String) {
        mRepo.setFeedComment(postId, comment)

    }



}
