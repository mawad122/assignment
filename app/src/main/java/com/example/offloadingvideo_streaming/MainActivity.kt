import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.offloadingvideo_streaming.Contact
import com.example.offloadingvideo_streaming.R
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextNumber: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var buttonSave: Button
    private lateinit var listViewContacts: ListView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactList: MutableList<Contact>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialogUtil: ProgressDialogUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseReference = FirebaseDatabase.getInstance().getReference("contacts")

        editTextName = findViewById(R.id.editTextName)
        editTextNumber = findViewById(R.id.editTextNumber)
        editTextAddress = findViewById(R.id.editTextAddress)
        buttonSave = findViewById(R.id.buttonSave)
        listViewContacts = findViewById(R.id.listViewContacts)

        contactList = ArrayList()
        contactAdapter = ContactAdapter(this, contactList)
        listViewContacts.adapter = contactAdapter

        progressDialogUtil = ProgressDialogUtil(this)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)

        buttonSave.setOnClickListener {
            saveContact()
        }

        fetchContacts()
    }

    private fun saveContact() {
        val name = editTextName.text.toString().trim()
        val number = editTextNumber.text.toString().trim()
        val address = editTextAddress.text.toString().trim()

        if (name.isEmpty() || number.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        progressDialogUtil.showProgressDialog("Uploading...")

        progressDialog.show()

        val contactId = databaseReference.push().key

        val contact = Contact(contactId!!, name, number, address)


        databaseReference.child(contactId).setValue(contact)
            .addOnCompleteListener { task: Task<Void?> ->
                progressDialogUtil.dismissProgressDialog()
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Contact saved successfully", Toast.LENGTH_SHORT).show()
                    // Clear input fields
                    editTextName.setText("")
                    editTextNumber.setText("")
                    editTextAddress.setText("")
                } else {
                    Toast.makeText(this@MainActivity, "Failed to save contact", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchContacts() {
        val contactsRef = databaseReference.child("contacts")

        contactsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact = snapshot.getValue(Contact::class.java)
                if (contact != null) {
                    contactList.add(contact)
                    contactAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
