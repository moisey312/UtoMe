package com.example.utome


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import android.R.id.message
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_me.*


// Енто класс фрагмента

class EditProfileFragment : Fragment() {
    var name_ = String()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_post_list -> {
                view?.let {
                    Snackbar.make(it, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .show()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_me -> {
                view?.let {
                    Snackbar.make(it, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .show()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_new_post -> {
                view?.let {
                    Snackbar.make(it, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .show()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v =  inflater.inflate(R.layout.fragment_edit_profile, container, false)

        var a: Int? = arguments?.getInt("REGISTER_STATUS")
        if(a == 1) {

            val navView: BottomNavigationView = activity!!.findViewById(R.id.nav_view)

            navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

       //var edit = v.findViewById<TextInputEditText>(R.id.text_edit_name)
        var editTextName: TextInputEditText = v.findViewById(R.id.text_edit_name)
        var editTextAddress: TextInputEditText = v.findViewById(R.id.text_edit_address)
        var editTextPhone: TextInputEditText = v.findViewById(R.id.text_edit_phone)


        val ref = FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                editTextName.setText(dataSnapshot.child("name").value.toString())
                editTextAddress.setText(dataSnapshot.child("address").value.toString())
                editTextPhone.setText(dataSnapshot.child("number").value.toString())
            }
        })


         v.findViewById<Button>(R.id.edit_profile_save).setOnClickListener{

             FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("name").setValue(editTextName.text.toString());
             FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("address").setValue(editTextAddress.text.toString());
             FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("number").setValue(editTextPhone.text.toString());
            name_ = editTextName.text.toString()

            val meFragment = MeFragment()



            val manager = fragmentManager
            if(editTextName.text!!.trim().isNotEmpty() && editTextAddress.text!!.trim().isNotEmpty() && editTextPhone.text!!.trim().isNotEmpty()) {
                val settingsFrag = MeFragment()

                val manager = fragmentManager

                val intent = Intent(
                    activity!!.baseContext,
                    ListActivity::class.java
                )
                activity!!.startActivity(intent)

                val frag_transaction = manager?.beginTransaction()

                frag_transaction?.replace(R.id.frgmCont, settingsFrag)
                frag_transaction?.remove(settingsFrag)
                frag_transaction?.commit()

            }
            else
                view?.let {
                    Snackbar.make(it, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .show()
                }
        }


        return v
    }
    

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

}