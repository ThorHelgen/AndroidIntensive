package com.thorhelgen.contacts.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thorhelgen.contacts.databinding.ContactViewItemBinding

class ContactsListAdapter(private val clickListener: (Long) -> Unit,
                          private val longClickListener: (Long, View) -> Boolean) :
    ListAdapter<Contact, ContactsListAdapter.ContactViewHolder>(DiffCallback) {

    class ContactViewHolder(
        private val binding: ContactViewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact,
                 clickListener: (Long) -> Unit,
                 longClickListener: (Long, View) -> Boolean) {
            binding.contactItemLayout.setOnClickListener { clickListener(contact.id) }
            binding.contactItemLayout.setOnLongClickListener {
                longClickListener(contact.id, binding.contactItemLayout)
            }
            binding.contact = contact
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact: Contact = getItem(position)
        holder.bind(contact, clickListener, longClickListener)
    }
}