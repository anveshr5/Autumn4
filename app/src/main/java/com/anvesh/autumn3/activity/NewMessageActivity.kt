package com.anvesh.autumn3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.adapter.NewMessageAdapter
import com.anvesh.autumn3.model.User
import com.anvesh.autumn3.model.UserDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class NewMessageActivity : AppCompatActivity() {

    lateinit var recyclerNewMessage: RecyclerView
    lateinit var newMessageAdapter : NewMessageAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    var userList = arrayListOf<User>()
    var searchList = arrayListOf<User>()
    var posArray: ArrayList<UserDetails> = arrayListOf()

    val nameComparator = Comparator<UserDetails> { name1, name2 ->
        if (name1.pos == name2.pos) {
            name1.user.username.compareTo(name2.user.username, false)
        } else {
            name1.pos.compareTo(name2.pos)
        }
    }

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
                    userList.add(newUser!!)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu, menu)

        val searchItem = menu?.findItem(R.id.searchView)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null){
                    getSearch(query)
                    Log.d("submit", query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {
                    getSearch(newText)
                    Log.d("change", newText)
                }
                return true
            }
        })
        return true
    }

    fun getSearch(searchText: String) {
        refreshLists()
        val search = searchText.toLowerCase().capitalize().trimEnd()
        Log.d("getSearch",search)
        for (name in userList) {
            if (name.username.contains(search,true)){
                posArray.add(UserDetails(name, name.username.substringBefore(search).length))
                Log.d("names", name.toString())
            }
        }
        Collections.sort(posArray,nameComparator)
        for (name in posArray){
            searchList.add(name.user)
        }
        newMessageAdapter = NewMessageAdapter(NewMessageActivity(),searchList)
        recyclerNewMessage.adapter = newMessageAdapter
    }

    private fun refreshLists() {
        newMessageAdapter = NewMessageAdapter(NewMessageActivity(),userList)
        recyclerNewMessage.adapter = newMessageAdapter
        posArray.removeAll(posArray)
        searchList.removeAll(searchList)
    }
}