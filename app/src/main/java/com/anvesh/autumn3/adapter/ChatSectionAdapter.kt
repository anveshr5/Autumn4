package com.anvesh.autumn3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.ContactDetailsModel

class ChatSectionAdapter(val context: Context, private val contactDetailsList: ArrayList<ContactDetailsModel>): RecyclerView.Adapter<ChatSectionAdapter.ChatSectionViewHolder>() {

    class ChatSectionViewHolder(view: View): RecyclerView.ViewHolder(view){
        var txtContactRollNo: TextView = view.findViewById(R.id.txtContactRollNo)
    }

    override fun onCreateViewHolder(holder: ViewGroup, viewType: Int): ChatSectionViewHolder {
        val itemView = LayoutInflater.from(holder.context).inflate(R.layout.contact_details_recyclerview,holder,false)

        return ChatSectionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contactDetailsList.size
    }

    override fun onBindViewHolder(holder: ChatSectionViewHolder, position: Int) {
        val contactDetails = contactDetailsList[position]

        holder.txtContactRollNo.text = contactDetails.rollNo.toString()
    }
}