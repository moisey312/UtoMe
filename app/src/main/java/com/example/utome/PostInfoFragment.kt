package com.example.utome


import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.utome.RowType.*
import java.util.*
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.MapsInitializer
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


// Енто класс фрагмента

@Suppress("DEPRECATION")
class PostInfoFragment : Fragment() {

    var adapter: MultipleTypesAdapter? = null
    internal var items: MutableList<PostInfoRowType> = ArrayList()
    internal var rnd = Random(1337)

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_post_info, container, false)

        var post: Post? = arguments?.getParcelable("post")

        if (post != null) {
            items.add(NameRowType(post.name, 20))
            items.add(TextRowType("Расстояние", "${post.distance}"))
            items.add(TextRowType("Описание", "${post.info}"))
            items.add(TextRowType("Адрес", "${post.address}"))
            items.add(TextRowType("Цена товара(ов)", "${post.price}" + "\u20BD"))
            items.add(TextRowType("Оплата", "${post.pay}" + "\u20BD"))
        }

        val recyclerView: RecyclerView = v.findViewById(R.id.RVPostInfo)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MultipleTypesAdapter(items)
        recyclerView.adapter = adapter

        if(post != null) v.findViewById<Button>(R.id.accept).isEnabled = (post.uId != FirebaseAuth.getInstance().uid.toString() && post.deliverer_uId == "none")
        if(!v.findViewById<Button>(R.id.accept).isEnabled) v.findViewById<Button>(R.id.accept).text = "Занят"
        else v.findViewById<Button>(R.id.accept).text = "Принять заказ"

        v.findViewById<Button>(R.id.accept).setOnClickListener{
            if (post != null && post.uId != FirebaseAuth.getInstance().uid.toString()) {
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("deliver_for_id").setValue(""+post.uId.toString())
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(FirebaseAuth.getInstance().uid.toString()).child("deliver_post").setValue("post_" + post.id.toString())
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(post.uId).child("u_posts").child("post_"+post.id).child("deliverer_uId").setValue(FirebaseAuth.getInstance().uid.toString())
                FirebaseDatabase.getInstance().getReference("UToMe").child("Users").child(post.uId).child("u_posts").child("post_"+post.id).child("status").setValue(2)

                val meFragment = MeFragment()

                val manager = activity?.supportFragmentManager

                val frag_transaction = manager?.beginTransaction()

                if (frag_transaction != null) {
                    frag_transaction.replace(R.id.frgmCont, meFragment)
                    frag_transaction.commit()
                }

            }
        }

        return v
    }
}
