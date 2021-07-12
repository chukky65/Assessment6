package com.chuks.contact3.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chuks.contact3.models.Contact
import com.chuks.contact3.models.ContactDatabase

class MainActivityViewModel : ViewModel() {

     val contactLiveData = MutableLiveData<List<Contact>>()

    fun getContact(database: ContactDatabase) {
        contactLiveData.postValue(database.contactDao().getAllContact())
    }

    fun addContact(database: ContactDatabase, contact: Contact) {
        database.contactDao().addContact(contact)
        getContact(database)
    }
}