package com.example.caiguru.commonScreens.chat.archiveChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import kotlinx.android.synthetic.main.activity_chat.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.archiveChat.archiveChatMessage.ArchiveChatMessagesActivity
import com.example.caiguru.commonScreens.networkError.NetworkErrorActivity


class ArchiveChatActivity : AppCompatActivity(), ArchiveChatAdapter.ChatListener, ArchiveChatParentAdapter.ParentClickListener {

    private var clickedParentItem: Int = -1
    private lateinit var mChatAdapter: ArchiveChatParentAdapter
    lateinit var mViewModel: ArchiveChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_chat)
        settingUpToolbar()
        mViewModel = ViewModelProviders.of(this)[ArchiveChatViewModel::class.java]
        setChatAdapter()

        mViewModel.observeChatList().observe(this, Observer {
            if (clickedParentItem > -1){
                it[clickedParentItem].isExpanded = true
            }
            mChatAdapter.update(it)
            progressBar.visibility = View.INVISIBLE
        })


        mViewModel.observeError().observe(this, Observer {
            val intent = Intent(this, NetworkErrorActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            progressBar.visibility = View.INVISIBLE
        })
    }


    private fun setChatAdapter() {
        mChatAdapter = ArchiveChatParentAdapter(this)
        val manager  = LinearLayoutManager(this)
        chatList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        chatList.layoutManager = manager
        chatList.adapter = mChatAdapter
    }
    //.........setup tool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        val text = findViewById<TextView>(R.id.toolbartittle)
        text.text = getString(R.string.archive_chat_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onChatItemClick(model: ModelChat) {
        val intent = Intent(this, ArchiveChatMessagesActivity::class.java)
        intent.putExtra("model", model)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!!.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        mViewModel.getChannels()
    }

    override fun onParentClick(expandedPosition: Int) {
        clickedParentItem = expandedPosition
    }
}
