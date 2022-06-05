package com.thorhelgen.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thorhelgen.contacts.databinding.ContactViewItemBinding

class ContactsListAdapter(private val clickListener: (Int) -> Unit) :
    ListAdapter<Contact, ContactsListAdapter.ContactViewHolder>(DiffCallback) {

    class ContactViewHolder(
        private val binding: ContactViewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, clickListener: (Int) -> Unit, position: Int) {
            binding.contactItemLayout.setOnClickListener { clickListener(position) }
            binding.contact = contact
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact: Contact = getItem(position)
        holder.bind(contact, clickListener, position)
    }
}