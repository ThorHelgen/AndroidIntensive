package com.thorhelgen.contacts

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thorhelgen.contacts.list.Contact
import com.thorhelgen.contacts.list.ContactsListAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Contact>) {
    val adapter = recyclerView.adapter as ContactsListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}
