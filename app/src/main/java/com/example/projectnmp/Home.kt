package com.example.projectnmp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.SharedPreferences
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var memeList:ArrayList<meme> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        val q = Volley.newRequestQueue(activity)
//        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_memes.php"
//        var stringRequest = StringRequest(
//            Request.Method.POST, url,
//            {
//                Log.d("apiresult", it)
//                val obj = JSONObject(it)
//                if(obj.getString("result") == "success") {
//                    val data = obj.getJSONArray("data")
//
//                    for(i in 0 until data.length()) {
//                        val memeObj = data.getJSONObject(i)
//                        val hasil = meme(
//                            memeObj.getInt("meme_id"),
//                            memeObj.getString("meme_url"),
//                            memeObj.getString("top_text"),
//                            memeObj.getString("bottom_text"),
//                            memeObj.getInt("num_likes"),
//                            memeObj.getInt("user_id")
//                        )
//                        memeList.add(hasil)
//                    }
//                    memeList.clear()
//                    updateMeme();
//                    Log.d("cekisimeme", memeList.toString())
//                }
//            },
//            {
//                Log.e("apiresultmeme", it.message.toString())
//            })
//        q.add(stringRequest)
    }
    //Make On resume karena setiap fragment e di buka dia akan ngeload terus, jadi
    //Meme baru akan ter refresh terus

    override fun onResume() {
        super.onResume()
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences =
            activity?.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var editor: SharedPreferences.Editor=shared.edit();
        var idCompanion="IDUSER";
        var idUser=shared.getInt(idCompanion,0);

        memeList.clear();
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_memes2.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")

                    for(i in 0 until data.length()) {
                        val memeObj = data.getJSONObject(i)
                        var comment: Int = memeObj.getInt("total_comment")
                        var reply: Int = memeObj.getInt("total_reply")
                        var total: Int = comment + reply
                        var hasil:meme = meme(
                            memeObj.getInt("meme_id"),
                            memeObj.getString("meme_url"),
                            memeObj.getString("top_text"),
                            memeObj.getString("bottom_text"),
                            memeObj.getInt("num_likes"),
                            memeObj.getInt("user_id"),
                            memeObj.getInt("liked"),
                            total
                        )
                        memeList.add(hasil)
                    }
                    updateMeme(idUser);
                    Log.d("cekisimeme", memeList.toString())
                }
            },
            {
                Log.e("apiresultmeme", it.message.toString())
            })

        q.add(stringRequest)
    }


    fun updateMeme(idUser: Int){

        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.memeView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = memeAdapter(memeList, idUser)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var btn:FloatingActionButton;
//        lateinit var btn: FloatingActionButton
        val active = activity as MainActivity
        btn = view.findViewById(R.id.fabCreateMeme)
        btn.setOnClickListener{

            active.startActivity(Intent(activity,memeCreate::class.java))
            val fragmentManager = (activity as MainActivity).supportFragmentManager
            fragmentManager.popBackStack()

        }
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}