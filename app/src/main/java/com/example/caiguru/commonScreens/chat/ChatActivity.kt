package com.example.caiguru.commonScreens.chat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import kotlinx.android.synthetic.main.activity_chat.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.chat.archiveChat.ArchiveChatActivity
import com.example.caiguru.commonScreens.chat.chatMessage.ChatMessagesActivity
import com.example.caiguru.commonScreens.networkError.NetworkErrorActivity
import java.lang.Exception


class ChatActivity : AppCompatActivity(), ChatAdapter.ChatListener, ChatParentAdapter.ParentClickListener {

    private var clickedParentItem: Int = -1
    private lateinit var mChatAdapter: ChatParentAdapter
    private lateinit var mConnectionsAdapter: ConnectionsAdapter
    lateinit var mViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        settingUpToolbar()
        //mViewModel = ViewModelProviders.of(this)[ChatViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        setConnectionsAdapter()
        setChatAdapter()


        mViewModel.observeChatList().observe(this, Observer {

        try {
             if (clickedParentItem > -1){
                 it[clickedParentItem].isExpanded = true
             }
             mChatAdapter.update(it)

         }catch (e:Exception){
             e.printStackTrace()
         }
        })

        mViewModel.observeLatestConnections().observe(this, Observer {
            progressBar.visibility = View.INVISIBLE
            mConnectionsAdapter.update(it)
        })

        mViewModel.observeError().observe(this, Observer {
            progressBar.visibility = View.INVISIBLE
            val intent = Intent(this, NetworkErrorActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        })
    }
    private fun setConnectionsAdapter() {

        mConnectionsAdapter = ConnectionsAdapter(this)
        val manager  = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        connectionsList.layoutManager = manager
        connectionsList.adapter = mConnectionsAdapter

    }

    private fun setChatAdapter() {
        mChatAdapter = ChatParentAdapter(this)
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
        text.text = getString(R.string.chat_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onChatItemClick(model: ModelChat) {
        val intent = Intent(this, ChatMessagesActivity::class.java)
        intent.putExtra("model", model)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!!.itemId == android.R.id.home){
            val intent=Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        if (item.itemId == R.id.archiveChat){
            startActivity(Intent(this,  ArchiveChatActivity::class.java))
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

    override fun onDestroy() {
        super.onDestroy()
        val intent=Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.parent_chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
