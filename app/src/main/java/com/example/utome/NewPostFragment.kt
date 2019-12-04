package com.example.utome


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Integer.*
import android.widget.Spinner



// Енто класс фрагмента

class NewPostFragment : Fragment() {

    var _name_ = String()

    val database = FirebaseDatabase.getInstance()
    val base = database.getReference("UToMe")
    val db_User = base.child("Users")
    val fb_auth = FirebaseAuth.getInstance()
    var id = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_new_post, container, false)
        FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("post_ids").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot){ id = p0.value.toString()}
        })
        val btn = v.findViewById<Button>(R.id.add_new_post)

        db_User.child(fb_auth.uid.toString()).child("name").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) { _name_ = dataSnapshot.value.toString() }
        })

        val Desc = v.findViewById<TextInputEditText>(R.id.descript)
        val Address = v.findViewById<TextInputEditText>(R.id.edit_address)
        val Timeout = v.findViewById<TextInputEditText>(R.id.wait_text)
        val Price = v.findViewById<TextInputEditText>(R.id.cost)
        val Pay = v.findViewById<TextInputEditText>(R.id.cost_for_delivery)


        btn.setOnClickListener {
            if (Desc.text!!.isEmpty() || Address.text!!.isEmpty() || Timeout.text!!.isEmpty() || Price.text!!.isEmpty() || Pay.text!!.isEmpty())
                view?.let {
                    Snackbar.make(it, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .show()
                }
            else {
                val spinner = v.findViewById(R.id.spinner) as Spinner
                val selected = spinner.selectedItemPosition.toString()

                val ex = db_post()

                ex.id = parseInt(id)
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users")
                    .child(FirebaseAuth.getInstance().uid.toString()).child("post_ids")
                    .setValue((parseInt(id) + 1).toString())

                ex.uId = FirebaseAuth.getInstance().uid.toString()
                ex.name = _name_;
                ex.pay = parseInt(v.findViewById<TextInputEditText>(R.id.cost_for_delivery).text.toString())
                ex.descriprion = v.findViewById<TextInputEditText>(R.id.descript).text.toString()
                ex.category = selected
                ex.address = v.findViewById<TextInputEditText>(R.id.edit_address).text.toString()
                ex.price = parseInt(v.findViewById<TextInputEditText>(R.id.cost).text.toString())
                ex.deliverer_uId = "none"
                ex.status = 0
                db_User.child(fb_auth.uid.toString()).child("u_posts").child("post_$id").setValue(ex)


                val settingsFrag = MeFragment()

                val manager = activity?.supportFragmentManager

                val frag_transaction = manager?.beginTransaction()

                if (frag_transaction != null) {
                    frag_transaction.replace(R.id.frgmCont, settingsFrag)
                    frag_transaction.remove(settingsFrag)
                    frag_transaction.commit()
                }


            }
        }
        return v
    }
}
