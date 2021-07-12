package com.chuks.contact3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.chuks.contact3.activities.ContactFullViewActivity
import com.chuks.contact3.databinding.ActivityMainBinding
import com.chuks.contact3.models.Contact
import com.chuks.contact3.models.ContactAdapter
import com.chuks.contact3.models.ContactDatabase
import com.chuks.contact3.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var database: ContactDatabase
    private lateinit var viewModel:MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).allowMainThreadQueries().build()


        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getContact(database)


        contactAdapter = ContactAdapter(listOf<Contact>()) {
            val intent = Intent(this@MainActivity, ContactFullViewActivity::class.java)
            intent.run {
                putExtra("id", it.id)
                putExtra("FullName", it.fName)
                putExtra("phoneNumber", it.PhoneNo).toString()
            }
            startActivity(intent)
        }
        binding.contactRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = contactAdapter
        }


        viewModel.contactLiveData.observe(this, { contact ->
            contactAdapter.phone = contact
            contactAdapter.notifyDataSetChanged()
        })


        binding.clickBtn.setOnClickListener {
            val name = binding.fullNameInput.editText?.text.toString()
            val number = binding.phoneNumberInput.editText?.text.toString()

            if (name != null) {
                if (number != null) {
                    saveContact(name, number)
                }
            }
        }

    }

    private fun saveContact(name: String, number: String) {
        val contact = Contact(id = 0, name, number )
        viewModel.addContact(database, contact)

    }
}