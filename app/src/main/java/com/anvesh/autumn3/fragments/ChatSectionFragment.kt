package com.anvesh.autumn3.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.NewMessageActivity
import com.anvesh.autumn3.activity.RegisterActivity
import com.anvesh.autumn3.model.ChatMessage
import com.anvesh.autumn3.model.LatestMessageRow
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatSectionFragment : Fragment() {

    lateinit var recyclerLatestMessage: RecyclerView
    lateinit var rlNoMsgsYet: RelativeLayout

    val latestMessageAdapter = GroupAdapter<GroupieViewHolder>()

    private val timeComparator = Comparator<ChatMessage>{ msg1, msg2 ->
        msg1.timestamp.compareTo(msg2.timestamp)
    }

    companion object{
        var currentUser: User? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_section, container, false)

        rlNoMsgsYet = view.findViewById(R.id.rlNoMsgsYet)

        recyclerLatestMessage = view.findViewById(R.id.recyclerLatestMessage)
        recyclerLatestMessage.adapter = latestMessageAdapter

        setHasOptionsMenu(true)

        fetchCurrentUser()

        listenForLatestMessages()

        return view
    }

    private fun fetchCurrentUser() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun listenForLatestMessages() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/${uid}")

        //Log.d("pos", latestMessageAdapter.getAdapterPosition(LatestMessageRow(this,)).toString())

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshLatestMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshLatestMessages()
                //   var notification = NotificationCompat.Builder(this@LatestMessagesActivity,"Messages")
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun refreshLatestMessages() {
        latestMessageAdapter.clear()
        latestMessagesMap.values.forEach{
            latestMessageAdapter.add(LatestMessageRow(activity as Context,it))
        }
        if (latestMessagesMap.isEmpty()){
            rlNoMsgsYet.visibility = View.VISIBLE
        } else {
            rlNoMsgsYet.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.latest_messages_activity_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.newMessage -> {
                val intent = Intent(activity as Context, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity as Context, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}