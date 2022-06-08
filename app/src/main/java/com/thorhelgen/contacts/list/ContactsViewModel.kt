package com.thorhelgen.contacts.list

import androidx.lifecycle.*
import java.util.LinkedList
import kotlin.random.Random

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData<LinkedList<Contact>>()
    val contacts: LiveData<LinkedList<Contact>> = _contacts

    init {
        // Initialize and generate the contacts list
        _contacts.value = LinkedList<Contact>().apply {
            val firstNames: Array<String> = arrayOf("Olga", "Vsevolod", "Stanislav", "Yaroslav",
                "Mykola", "Danylko", "Liliya", "Marinka", "Sveta", "Sergei");
            val lastNames: Array<String> = arrayOf("Petrenko", "Kravchenko", "Tkachenko", "Novik",
                "Rudenko", "Moroz", "Koval", "Kostas", "Eoin", "Boyko");
            for (i in 0 until 102) {
                add(
                    Contact(firstNames[Random.nextInt(firstNames.size)],
                    lastNames[Random.nextInt(lastNames.size)],
                    "+380${(0..9).map {Random.nextInt(10)}.joinToString("") }",
                    "https://picsum.photos/id/${i}/500")
                )
            }
        }


    }
}