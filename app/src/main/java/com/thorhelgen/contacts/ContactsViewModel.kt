package com.thorhelgen.contacts

import androidx.lifecycle.*
import java.util.LinkedList

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<LinkedList<Contact>>()
    val contacts: LiveData<LinkedList<Contact>> = _contacts

    init {
        _contacts.value = LinkedList<Contact>()
        with(_contacts.value) {
            this?.add(Contact("Taras", "Petrenko", "+380504763998"))
            this?.add(Contact("Maksym", "Kravchenko", "+380509488168"))
            this?.add(Contact("Volodymyr", "Tkachenko", "+380508202597"))
        }
    }
}