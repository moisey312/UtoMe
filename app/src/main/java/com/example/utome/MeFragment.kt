package com.example.utome

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.row_type_text.view.*

class MeFragment : Fragment() {

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("UToMe")
    val db_User_lis = ref.child("Users")
    val fb_auth = FirebaseAuth.getInstance()

    var adapter: MeAdapter? = null

    var _name_ = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*var name_text = view!!.findViewById<TextView>(R.id.me_name)
        var name_text_from_profile = view!!.findViewById<TextInputEditText>(R.id.edit_name).text.toString()
        name_text.text = name_text_from_profile*/
        //В теории должно выводить имя
        db_User_lis.child(fb_auth.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _name_ = dataSnapshot.child("name").value.toString()
                view?.findViewById<TextView>(R.id.user_profile_short_bio)?.text  = dataSnapshot.child("raiting").value.toString()
                view?.findViewById<TextView>(R.id.me_name)?.text  = _name_
            }
        })

        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_me, container, false)

        var recycleViewMe_new : RecyclerView = v.findViewById(R.id.recycleView_me_new)
        var Posts_in_prog: ArrayList<Post>


        v.findViewById<ImageButton>(R.id.sign_out).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        db_User_lis.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var posts : ArrayList<db_post> = ArrayList()
                val d_f_id = dataSnapshot.child(fb_auth.uid.toString()).child("deliver_for_id")
                val d_f_post = dataSnapshot.child(fb_auth.uid.toString()).child("deliver_post")

                if(d_f_id.value.toString() != "none" && d_f_id.exists() && dataSnapshot.child(d_f_id.value.toString()).child("u_posts").child(d_f_post.value.toString()).exists()){
                    posts.add(dataSnapshot.child(d_f_id.value.toString()).child("u_posts").child(d_f_post.value.toString()).getValue(db_post::class.java)!!)
                    posts[0].deliverer_uId = "deliv"
                    posts[0].status = 1
                }

                for (snapshot in dataSnapshot.child(fb_auth.uid.toString()).child("u_posts").children) {
                    var post = snapshot.getValue(db_post::class.java)
                    if (post != null) {
                        posts.add(post)
                    }
                }
                Posts_in_prog = ShowPosts(posts)
                val recyclerView: RecyclerView = v.findViewById(R.id.recycleView_me_new)
                recycleViewMe_new.layoutManager = LinearLayoutManager(activity)
                adapter = MeAdapter(Posts_in_prog, v.context)
                recyclerView.adapter = adapter


            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        var btnEditProfile: ImageButton = v.findViewById(R.id.edit_profile)


        val onEditProfileClick = View.OnClickListener {
            val editProfileFragment = EditProfileFragment()
            val manager = fragmentManager
            val frag_transaction = manager?.beginTransaction()
            val bundle = Bundle()
            // тут кароче пишешь bundle.put<Тип данных>("key", "значение") и это передается во editprofilefragment
            editProfileFragment.arguments = bundle
            frag_transaction?.replace(R.id.frgmCont, editProfileFragment)
            frag_transaction?.commit()
        }

        btnEditProfile.setOnClickListener(onEditProfileClick)


        return v
    }
    fun ShowPosts(posts: ArrayList<db_post>) : ArrayList<Post> {
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
            o.status = posts[i].status
            o.id = posts[i].id
            o.deliverer_uId = posts[i].deliverer_uId
            o.uId = posts[i].uId
            a.add(o)
        }
        return a
    }
}
