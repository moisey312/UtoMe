package com.example.utome

import android.content.Context
import android.renderscript.Sampler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MeAdapter(posts: ArrayList<Post>, context: Context) : RecyclerView.Adapter<MeAdapter.ViewHolder>(){

    var Posts: ArrayList<Post>

    var con: Context? = null
    private var mInflater: LayoutInflater? = null

    init {
        mInflater = LayoutInflater.from(context)
        this.Posts = posts
        con = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.me_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = Posts[position].name
        holder.id = Posts[position].id
        holder.address.text = Posts[position].address
        holder.info.text = Posts[position].info

        holder.catg_img.setBackgroundResource(
            con?.resources?.getIdentifier(
                "category_" + Posts[position].category,
                "drawable",
                con?.packageName
            )!!
        )
        if(Posts[position].status == 0) holder.status_img.setBackgroundResource(R.drawable.ic_access_time_black_24dp)
        else if(Posts[position].status == 1) holder.status_img.setBackgroundResource(R.drawable.ic_directions_run_black_24dp)
        else if(Posts[position].status == 2) holder.status_img.setBackgroundResource(R.drawable.ic_send_black_24dp)

        if(Posts[position].deliverer_uId == "none" || Posts[position].deliverer_uId == "deliv")
            holder.acc_but.visibility = View.GONE
        else {
            holder.acc_but.visibility = View.VISIBLE
        }
        var rat: String = String()
        holder.acc_but.setOnClickListener{

            FirebaseDatabase.getInstance().getReference("UToMe").child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
                var t : Int = 0
                override fun onCancelled(p0: DatabaseError){}

                override fun onDataChange(p0: DataSnapshot) {
                    t = Integer.parseInt(p0.child(Posts[position].deliverer_uId).child("raiting").value.toString())
                    t+=5
                    rat = t.toString()
                    FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(Posts[position].deliverer_uId).child("raiting").setValue(rat)
                }
            })

            FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("u_posts").child("post_${holder.id}").removeValue()
        }
        holder.del_but.setOnClickListener{
            if(Posts[position].status != 1)
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("u_posts").child("post_${holder.id}").removeValue()
            else{
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
                    var t : Int = 0
                    override fun onCancelled(p0: DatabaseError){}

                    override fun onDataChange(p0: DataSnapshot) {
                        t = Integer.parseInt(p0.child(FirebaseAuth.getInstance().uid.toString()).child("raiting").value.toString())
                        t -= 10
                        rat = t.toString()
                        FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("raiting").setValue(rat)
                    }
                })
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users")
                    .child(FirebaseAuth.getInstance().uid.toString()).child("deliver_for_id").setValue("none")
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users")
                    .child(Posts[position].uId).child("u_posts").child("post_${Posts[position].id}").child("deliverer_uId").setValue("none")
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users")
                    .child(Posts[position].uId).child("u_posts").child("post_${Posts[position].id}").child("status").setValue(0)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val del_but = itemView.findViewById<Button>(R.id.baton)
        val acc_but = itemView.findViewById<Button>(R.id.accept)
        var name : TextView = itemView.findViewById(R.id.rv_main_name)
        var address : TextView = itemView.findViewById(R.id.rv_main_address)
        var info : TextView = itemView.findViewById(R.id.rv_main_info)
        var catg_img: ImageView = itemView.findViewById(R.id.category)
        var status_img: ImageView = itemView.findViewById(R.id.status)

        var id = 0
    }


    override fun getItemCount(): Int = Posts.size

}