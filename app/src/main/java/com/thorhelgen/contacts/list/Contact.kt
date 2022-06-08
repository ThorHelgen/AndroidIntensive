package com.thorhelgen.contacts.list

data class Contact(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    val imageUrl: String
) {
    val id = idIterator.next()

    override fun equals(other: Any?): Boolean =
        other is Contact &&
        other.firstName == firstName &&
        other.lastName == lastName &&
        other.phoneNumber == phoneNumber &&
        other.imageUrl == imageUrl

    private companion object {
        // Id generator
        fun getId() = sequence<Long> {
            var id: Long = 0
            while(true)
            {
                yield(id++)
            }
        }
        val idIterator = getId().iterator()
    }
}
