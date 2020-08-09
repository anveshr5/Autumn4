package com.anvesh.autumn3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.ChatSectionFragment
import com.anvesh.autumn3.model.ChatMessage
import com.anvesh.autumn3.model.MessageFromItem
import com.anvesh.autumn3.model.MessageToItem
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatLogActivity : AppCompatActivity() {

    lateinit var recyclerChatLog: RecyclerView
    lateinit var etNewMessage: EditText
    lateinit var btnSendMessage: Button

    val chatLogAdapter = GroupAdapter<GroupieViewHolder>()

    val fromUserUid = FirebaseAuth.getInstance().currentUser?.uid!!
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        if (intent != null){
            toUser = intent.getParcelableExtra("ToUser")
            supportActionBar?.title = toUser?.username ?: "Error"
        }

        recyclerChatLog = findViewById(R.id.recyclerChatLog)
        etNewMessage = findViewById(R.id.etNewMessage)
        btnSendMessage = findViewById(R.id.btnSendMessage)

        recyclerChatLog.layoutManager = LinearLayoutManager(this@ChatLogActivity)
        recyclerChatLog.adapter = chatLogAdapter

        listenForMessages()
        recyclerChatLog.scrollToPosition(chatLogAdapter.itemCount - 1)
        btnSendMessage.setOnClickListener {
            if (etNewMessage.text.toString() != "") {
                performSendMessage(fromUserUid)
            }
        }
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/${toUser?.uid}/${fromUserUid}")

        ref.addChildEventListener(object: ChildEventListener  {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("chatMessage", chatMessage.text)
                    if (chatMessage.fromId == toUser?.uid){
                        chatLogAdapter.add(MessageFromItem(this@ChatLogActivity,chatMessage.text, toUser!!))
                        recyclerChatLog.scrollToPosition(chatLogAdapter.itemCount -  1)
                    } else {
                        chatLogAdapter.add(MessageToItem(this@ChatLogActivity,chatMessage.text,MainActivity.currentUser!!))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage(fromUserUid: String) {
        val text = etNewMessage.text.toString()

        val selfRef = FirebaseDatabase.getInstance().getReference("/user-messages/${fromUserUid}/${toUser?.uid}").push()

        val friendRef = FirebaseDatabase.getInstance().getReference("/user-messages/${toUser?.uid}/${fromUserUid}").push()

        val chatMessage = ChatMessage(selfRef.key!!,text,fromUserUid ,toUser?.uid!!,System.currentTimeMillis()/1000)

        selfRef.setValue(chatMessage).addOnSuccessListener {
            Log.d("messaged","Text Sent ${chatMessage.text}")
            etNewMessage.text.clear()
            recyclerChatLog.scrollToPosition(chatLogAdapter.itemCount - 1)
        }

        friendRef.setValue(chatMessage)

        val latestSelfRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${fromUserUid}/${toUser?.uid}")

        latestSelfRef.setValue(chatMessage)

        val latestFriendRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${toUser?.uid}/${fromUserUid}")

        latestFriendRef.setValue(chatMessage)

    }
}