package com.example.projectnmp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_meme_create.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Creation.newInstance] factory method to
 * create an instance of this fragment.
 */
class Creation : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var memeListForCreation:ArrayList<meme> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onResume() {
        memeListForCreation.clear();
        super.onResume()
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences =
            activity?.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var editor: SharedPreferences.Editor=shared.edit();
        var userCompanion="USERNAME";
        var idCompanion="IDUSER";
        var nama=shared.getString(userCompanion,"yuhu");
        var idUser=shared.getInt(idCompanion,0);
        memeListForCreation.clear();
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_memes2.php"
        var stringRequest =object:StringRequest(
            Request.Method.POST, url,
            Response.Listener {
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
                        memeListForCreation.add(hasil)
                    }
                    updateMeme(idUser);
                    Log.d("cekisimeme", memeListForCreation.toString())
                }
            },
            Response.ErrorListener {
                Log.e("Gagal", it.message.toString())
            })

        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String,String>()
                params["id"]=idUser.toString();


                return params


            }
        }
        q.add(stringRequest)



    }
    fun updateMeme(idUser: Int){

        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.MyCreationView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = memeAdapter(memeListForCreation, idUser)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creation, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Creation.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Creation().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}