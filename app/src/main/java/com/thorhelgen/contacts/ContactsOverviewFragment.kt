package com.thorhelgen.contacts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thorhelgen.contacts.databinding.FragmentContactsOverviewBinding
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File

class ContactsOverviewFragment : Fragment() {

    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentContactsOverviewBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.contactsList.adapter = ContactsListAdapter(::listItemClickListener)

        return binding.root
    }

    private fun listItemClickListener(itemPosition: Int) {
        val args = bundleOf("position" to itemPosition)
        findNavController().navigate(R.id.contactFragment, args)
    }

}