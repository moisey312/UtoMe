package com.example.utome

import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseError
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging


class LoginActivity : AppCompatActivity() {

    var login: String = ""
    var password: String = ""

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private var ETemail: EditText? = null
    private var ETpassword: EditText? = null

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("UToMe")
    val db_Users_lis = ref.child("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        var auth = FirebaseAuth.getInstance()

        if (auth.getCurrentUser() != null) {
            val intent = Intent(this@LoginActivity, ListActivity::class.java)
            intent.putExtra("ref", "activity_login")
            startActivity(intent)
        }

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {}
            else {}
        }

        ETemail = findViewById(R.id.et_email)
        ETpassword = findViewById(R.id.et_password)
    }

    fun onClick(view:View) {
        if (view.id == R.id.btn_sign_in)
            signin(ETemail?.getText().toString(), ETpassword?.getText().toString())
        else if (view.id == R.id.btn_registration)
            registration(ETemail?.getText().toString(), ETpassword?.getText().toString())

    }

     fun signin(email:String, password:String) {
         mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this
         ) { task ->
             if (task.isSuccessful) {
                 Toast.makeText(this@LoginActivity, "Aвторизация успешна", Toast.LENGTH_SHORT).show()
                 val intent = Intent(this@LoginActivity, ListActivity::class.java)
                 intent.putExtra("ref", "activity_login")
                 startActivity(intent)
             } else
                 Toast.makeText(this@LoginActivity, "Неверный адрес почты или пароль" +  task.getException()?.message.toString(), Toast.LENGTH_SHORT).show()
         }
     }
     fun registration(email:String, password:String) {
         mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this, object: OnCompleteListener<AuthResult> {
             override fun onComplete(@NonNull task: Task<AuthResult>) {
                 if (task.isSuccessful)
                 {
                     val ex_u = User()


                     ex_u.name = " "
                     ex_u.u_posts = ArrayList<Post>()
                     //ex_u.u_posts.add(ex_p)

                     db_Users_lis.child(mAuth!!.currentUser?.uid.toString()).setValue(ex_u)
                     db_Users_lis.child(mAuth!!.currentUser?.uid.toString()).child("post_ids").setValue(0)
                     db_Users_lis.child(FirebaseAuth.getInstance().uid.toString()).child("deliver_for_id").setValue("none")
                     db_Users_lis.child(FirebaseAuth.getInstance().uid.toString()).child("raiting").setValue(0)
                     db_Users_lis.child(FirebaseAuth.getInstance().uid.toString()).child("address").setValue(" ")
                     db_Users_lis.child(FirebaseAuth.getInstance().uid.toString()).child("number").setValue("+7")

                     Toast.makeText(this@LoginActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                     var intent = Intent(this@LoginActivity, ListActivity::class.java)
                     intent.putExtra("ref", "register")
                     startActivity(intent)
                 }
                 else
                     Toast.makeText(this@LoginActivity, "Неверный адрес почты или пароль", Toast.LENGTH_SHORT).show()
             }
         })
    }
}
