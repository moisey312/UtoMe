package com.example.utome

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.me_post.view.*
import java.sql.DriverManager.println
import java.util.*
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity(), ListRecyclerViewAdapter.ItemClickListener {
    var adapter: ListRecyclerViewAdapter? = null

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("UToMe")
    val db_User_lis = ref.child("Users")

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_post_list -> {
                // Тут запускается фрагмент с помощью транзакции

                //Это объект класса
                val settingsFrag = MeFragment()

                val manager = supportFragmentManager

                val frag_transaction = manager.beginTransaction()

                adapter?.isClickable = true

                frag_transaction.replace(R.id.frgmCont, settingsFrag)
                frag_transaction.remove(settingsFrag)
                frag_transaction.commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_me -> {


                    val meFragment = MeFragment()

                    val manager = supportFragmentManager

                    val frag_transaction = manager.beginTransaction()

                    adapter?.isClickable = false

                    frag_transaction.replace(R.id.frgmCont, meFragment)
                    frag_transaction.commit()

                    return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_new_post -> {
                // Тут запускается фрагмент с помощью транзакции

                //Это объект класса

                val newPostFrag = NewPostFragment()

                val manager = supportFragmentManager

                val frag_transaction = manager.beginTransaction()

                adapter?.isClickable = false

                frag_transaction.replace(R.id.frgmCont, newPostFrag)
                frag_transaction.commit()


                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        var Posts: ArrayList<Post>

        val posts = ArrayList<db_post>()

        val con = this




        db_User_lis.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                posts.clear()

                for (snapshot in dataSnapshot.children) {
                    for(ss in snapshot.child("u_posts").children) {
                        var post = ss.getValue(db_post::class.java)
                        if (post != null) {
                            post.name = snapshot.child("name").value.toString()
                            posts.add(post)
                        }
                    }
                }
                Posts = ShowPosts(posts)
                val recyclerView: RecyclerView = findViewById(R.id.RVList)
                recyclerView.layoutManager = LinearLayoutManager(con)
                adapter = ListRecyclerViewAdapter(con, Posts)
                adapter!!.setClickListener(con)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


        var intent = getIntent()

        if(intent.getStringExtra("ref") == "activity_login")
            navView.selectedItemId = R.id.navigation_post_list
        else if(intent.getStringExtra("ref") == "register") {
            val editProfileFragment = EditProfileFragment()

            val manager = supportFragmentManager

            val frag_transaction = manager?.beginTransaction()

            val bundle = Bundle()
            bundle.putInt("REGISTER_STATUS", 1)
            editProfileFragment.arguments = bundle
            frag_transaction?.replace(R.id.frgmCont, editProfileFragment)
            frag_transaction?.commit()
        }
    }

    override fun onItemClick(view: View, position: Int) {
        val postInfoFragment = PostInfoFragment()

        val manager = supportFragmentManager

        val frag_transaction = manager.beginTransaction()

        adapter?.isClickable = false

        val bundle = Bundle()
        bundle.putParcelable("post", adapter?.getItem(position))
        postInfoFragment.arguments = bundle
        frag_transaction.replace(R.id.frgmCont, postInfoFragment)
        frag_transaction.commit()
    }

    fun ShowPosts(posts : ArrayList<db_post>) : ArrayList<Post> {
        var a : ArrayList<Post> = ArrayList()
        for(i in 0 until posts.size){
            var o = Post()
            o.name = posts[i].name.toString()
            o.pay = posts[i].pay
            o.category = posts[i].category.toString()
            o.info = posts[i].descriprion
            o.address = posts[i].address
            o.price = posts[i].price
            o.distance = posts[i].distance
            o.uId = posts[i].uId
            o.deliverer_uId = posts[i].deliverer_uId
            o.status = posts[i].status
            o.id = posts[i].id
            a.add(o)
        }
        return a
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.findFragmentById(R.id.post_infos)?.isResumed!!){
            val settingsFrag = MeFragment()

            val manager = supportFragmentManager

            val frag_transaction = manager.beginTransaction()

            adapter?.isClickable = true

            frag_transaction.replace(R.id.frgmCont, settingsFrag)
            frag_transaction.remove(settingsFrag)
            frag_transaction.commit()
        }
    }
}
