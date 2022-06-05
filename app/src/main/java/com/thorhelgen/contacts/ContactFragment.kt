package com.thorhelgen.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thorhelgen.contacts.databinding.FragmentContactBinding
import com.thorhelgen.contacts.databinding.FragmentContactsOverviewBinding

class ContactFragment : Fragment() {

    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val position = arguments?.getInt("position")
        if (position != null && viewModel.contacts.value != null) {
            val contact = viewModel.contacts.value!![position]
            binding.contact = contact

            binding.contactSaveBtn.setOnClickListener {
                with(viewModel.contacts.value!![position]) {
                    firstName = binding.firstNameEdt.text.toString()
                    lastName = binding.lastNameEdt.text.toString()
                    phoneNumber = binding.phoneNumberEdt.text.toString()
                }
                findNavController().popBackStack()
            }
        }


        return binding.root
    }
}