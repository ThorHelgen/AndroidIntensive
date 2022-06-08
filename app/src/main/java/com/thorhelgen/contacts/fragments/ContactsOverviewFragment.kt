package com.thorhelgen.contacts.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.thorhelgen.contacts.list.ContactsListAdapter
import com.thorhelgen.contacts.list.ContactsViewModel
import com.thorhelgen.contacts.R
import com.thorhelgen.contacts.databinding.FragmentContactsOverviewBinding

class ContactsOverviewFragment : Fragment() {

    private val viewModel: ContactsViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentContactsOverviewBinding
    private var isInSearch: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentContactsOverviewBinding.inflate(inflater)
        viewBinding.lifecycleOwner = this
        viewBinding.contactsListData = viewModel.contacts.value

        viewBinding.contactsList.adapter = ContactsListAdapter(::listItemClickListener, ::listItemLongClickListener)

        setHasOptionsMenu(true)

        return viewBinding.root
    }

    // Switch to details
    private fun listItemClickListener(itemId: Long) {
        val args = bundleOf("id" to itemId)
        findNavController().navigate(R.id.contactFragment, args)
    }

    // Create and inflate popup menu, delete contact
    private fun listItemLongClickListener(contactId: Long, anchorView: View): Boolean {
        val popupMenu = context?.let { PopupMenu(it, anchorView) }?.apply {
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.actionDelete -> {
                        // Search for the index of the contact in current view of contacts list
                        val currentIdx = viewBinding.contactsListData?.indexOfFirst {
                                contact -> contact.id == contactId
                        }
                        // Remove the contact from the viewed list
                        if (isInSearch) {
                            viewBinding.contactsListData?.removeAt(currentIdx!!)
                        }
                        // Remove the contact from the main list
                        viewModel.contacts.value!!.removeAll { contact ->
                            contact.id == contactId
                        }
                        // Refresh the list view
                        viewBinding.contactsList.adapter?.notifyItemRemoved(currentIdx!!)
                        true
                    }
                    else -> { true }
                }
            }
            inflate(R.menu.contact_item_popup_menu)
        }

        popupMenu?.show()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.contacts_list_option_menu, menu)

        // Search for contacts using SearchView
        val searchField = menu.findItem(R.id.searchField).actionView as SearchView
        searchField.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filter the contacts and bind the received list
                viewBinding.contactsListData = viewModel.contacts.value?.filter {
                    it.firstName == query || it.lastName == query
                }
                isInSearch = true
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        // Listen when the user exits SearchView
        menu.findItem(R.id.searchField).setOnActionExpandListener(
            object: MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    // Bind the main list
                    viewBinding.contactsListData = viewModel.contacts.value
                    isInSearch = false
                    return true
                }
        })
    }
}