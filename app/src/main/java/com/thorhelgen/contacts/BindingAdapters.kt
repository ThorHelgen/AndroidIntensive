package com.thorhelgen.contacts

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Contact>) {
    val adapter = recyclerView.adapter as ContactsListAdapter
    adapter.submitList(data)
}