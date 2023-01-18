package com.example.projectnmp

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
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [leaderboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class leaderboard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var leaderBoard:ArrayList<LeaderboardClass> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onResume() {
        super.onResume()
        leaderBoard.clear();
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/user_likes.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")

                    for(i in 0 until data.length()) {
                        val memeObj = data.getJSONObject(i)
                        val hasil = LeaderboardClass(
                            memeObj.getInt("user_id"),
                            memeObj.getString("username"),
                            memeObj.getString("first_name"),
                            memeObj.getString("last_name"),
                            memeObj.getString("privacy_setting"),
                            memeObj.getString("avatar_link"),
                            memeObj.getInt("total_like")
                        )
                        leaderBoard.add(hasil)
                    }
                    updateLeaderBoard();
                    Log.d("cekisileaderboard", leaderBoard.toString())
                }
            },
            {
                Log.e("apiresultleaderboard", it.message.toString())
            })

        q.add(stringRequest)
    }
    fun updateLeaderBoard(){

        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerLeaderboard)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = AdapterLeaderboard(leaderBoard)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment leaderboard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            leaderboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}