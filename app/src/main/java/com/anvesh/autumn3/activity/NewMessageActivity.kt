package com.anvesh.autumn3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.adapter.NewMessageAdapter
import com.anvesh.autumn3.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewMessageActivity : AppCompatActivity() {

    lateinit var recyclerNewMessage: RecyclerView
    lateinit var newMessageAdapter : NewMessageAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    var userList = arrayListOf<User?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

        recyclerNewMessage = findViewById(R.id.recyclerNewMessage)
        layoutManager = LinearLayoutManager(this)

        //Add user list
        fetchUsers(this)
    }

    private fun fetchUsers(newMessageActivity: NewMessageActivity) {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //retrieves all data from user database and stores in snapshot
                Log.d("snap","$snapshot")
                snapshot.children.forEach {
                    val newUser = it.getValue(User::class.java)
                    Log.d("users", "$newUser")
                    userList.add(newUser)
                }
                Log.d("added user",userList.toString())
                newMessageAdapter = NewMessageAdapter(newMessageActivity,userList)

                recyclerNewMessage.adapter = newMessageAdapter
                recyclerNewMessage.layoutManager = layoutManager
                Log.d("userList",userList.toString())
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this@NewMessageActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}