package com.thorhelgen.contacts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.thorhelgen.contacts.list.ContactsViewModel
import com.thorhelgen.contacts.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContactBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val id = arguments?.getLong("id")
        with(viewModel.contacts.value) {
            if (id != null && this != null) {
                val contactIdx = this.indexOfFirst { contact -> contact.id == id }
                binding.contact = this[contactIdx]

                // Save the contact details and transact to the list fragment
                binding.contactSaveBtn.setOnClickListener {
                    with(this[contactIdx]) {
                        firstName = binding.firstNameEdt.text.toString()
                        lastName = binding.lastNameEdt.text.toString()
                        phoneNumber = binding.phoneNumberEdt.text.toString()
                    }
                    findNavController().popBackStack()
                }
            }
        }


        return binding.root
    }
}