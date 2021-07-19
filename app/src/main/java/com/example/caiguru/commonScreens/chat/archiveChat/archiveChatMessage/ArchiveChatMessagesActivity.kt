package com.example.caiguru.commonScreens.chat.archiveChat.archiveChatMessage

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.MessageAdapter
import kotlinx.android.synthetic.main.activity_chat_messages.*


class ArchiveChatMessagesActivity : AppCompatActivity() {

    private lateinit var mAdapter: MessageAdapter
    private lateinit var mViewModelArchive: ArchiveChatMessageViewModel
    private lateinit var model: ModelChat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_chat_messages)
       // mViewModelArchive = ViewModelProviders.of(this)[ArchiveChatMessageViewModel::class.java]
        mViewModelArchive = ViewModelProvider(this).get(ArchiveChatMessageViewModel::class.java)

        model = intent.getParcelableExtra("model")!!
        mViewModelArchive.postModel(model)
        settingUpToolbar()
        setMessageAdapter()

        mViewModelArchive.getMessageList().observe(this, Observer {
            mAdapter.update(it)
            // if(isFirst) {
            messageList.scrollToPosition(it.size - 1)
            //   isFirst = false
            // }
        })

    }


    private fun setMessageAdapter() {
        mAdapter = MessageAdapter(this, model)
        messageList.layoutManager = LinearLayoutManager(this)
        messageList.adapter = mAdapter
    }

    //.........setup tool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        val text = findViewById<TextView>(R.id.toolbartittle)
        //text.text = model.listingname
        text.text = getString(R.string.chat)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
