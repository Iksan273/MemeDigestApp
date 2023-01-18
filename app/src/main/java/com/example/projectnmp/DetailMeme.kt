package com.example.projectnmp

import android.content.*
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_meme.*
import kotlinx.android.synthetic.main.activity_detail_meme.btnSendComment
import kotlinx.android.synthetic.main.activity_detail_meme.editTextComment
import kotlinx.android.synthetic.main.activity_detail_meme.recyclerViewComment
import kotlinx.android.synthetic.main.card_template.btnLikes
import kotlinx.android.synthetic.main.card_template.imgMeme
import kotlinx.android.synthetic.main.card_template.txtButtom
import kotlinx.android.synthetic.main.card_template.txtLikes
import kotlinx.android.synthetic.main.card_template.txtTop
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.template_comment.*
import org.json.JSONObject

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


private const val ARG_MEME = "the_meme"
private const val ARG_COMMENT = "the_comment"
private const val ARG_COMMAND = "command"
var commandReply: String? = ""
var the_meme: meme? = null
var commentList:ArrayList<Comment> = ArrayList()
var replyList:ArrayList<ReplyComment> = ArrayList()
var meme_id:Int? = 0
var the_comment: Comment? = null

class DetailMeme : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meme)


        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences = this.getSharedPreferences(sharedfile, Context.MODE_PRIVATE)?: return
        var idCompanion="IDUSER";
        var idUser=shared.getInt(idCompanion,0);

        the_meme = intent.getParcelableExtra(ARG_MEME)


        Picasso.get().load(the_meme?.meme_url).into(imgMeme)
        txtTop.text = the_meme?.top_text
        txtButtom.text = the_meme?.bottom_text
        meme_id = the_meme?.meme_id

        showAllComment()

        btnSendComment.setOnClickListener{
            if(editTextComment.text.toString() != ""){
                if(commandReply != "" || commandReply != ""){
                    addReplyComment(editTextComment.text.toString(), the_comment!!.comment_id)
                } else {
                    addComment(editTextComment.text.toString())
                }
//                editTextComment.setText("")
            } else {
                Toast.makeText(this, "Comment can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
        btnBackDetail.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showAllComment(){
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_comment.php"
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var stringRequest = @RequiresApi(Build.VERSION_CODES.N)
        object:StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult1", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    var data = obj.getJSONArray("data")
                    commentList.clear()
                    for(i in 0 until data.length()) {
                        val commentObj = data.getJSONObject(i)
                        Log.d("cektanggal", commentObj.toString())
                        var date  = formatter.parse(commentObj.getString("date"))
                        var currDate = SimpleDateFormat("DD MMM yy").format(date)
                        val comment = Comment(
                            Integer.parseInt(commentObj.getString("comment_id")),
                            commentObj.getString("content"),
                            commentObj.getString("first_name"),
                            commentObj.getString("last_name"),
                            currDate
                        )
                        commentList.add(comment)
                    }
                    updateComment()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult2", it.message.toString())
            })
        {
            override fun getParams() = hashMapOf(
                "meme_id" to meme_id.toString()
            )
        }
        q.add(stringRequest)
    }

    fun updateComment(){
        val lm: LinearLayoutManager = LinearLayoutManager(this)
        var recyclerView = recyclerViewComment
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = CommentAdapter(commentList)
    }

    fun addComment(new_comment: String){
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences = this.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var idCompanion="IDUSER";
        var idUser=shared.getInt(idCompanion,0);

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/add_comment.php"

        var stringRequest = @RequiresApi(Build.VERSION_CODES.N)
        object:StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult1", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    commentList.clear()
                    editTextComment.setText("")
                    showAllComment()
                } else {
                    Toast.makeText(this, "Comment failed", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult2", it.message.toString())
            })
        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String,String>()
                params["user_id"]= idUser.toString()
                params["meme_id"]= meme_id.toString()
                params["content"]= new_comment.toString()
                return params
            }
        }
        q.add(stringRequest)
    }

    //For Reply Comment
    fun showAllReplyComment(comment_id: Number){
//            val q = Volley.newRequestQueue(this)
//            val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_reply_comment.php"
//            val formatter = SimpleDateFormat("yyyy-MM-dd")
//
//            var stringRequest = object:StringRequest(Request.Method.POST, url,
//                Response.Listener<String> {
//                    Log.d("apiresult12", it)
//                    val obj = JSONObject(it)
//                    if(obj.getString("result") == "success") {
//                        var data = obj.getJSONArray("data")
//                        replyList.clear()
//                        for(i in 0 until data.length()) {
//                            val replyObj = data.getJSONObject(i)
//                            var date  = formatter.parse(replyObj.getString("publish_date"))
//                            var currDate = SimpleDateFormat("DD MMM yy").format(date)
//                            val replyComment = ReplyComment(
//                                replyObj.getString("content"),
//                                replyObj.getString("first_name"),
//                                replyObj.getString("last_name"),
//                                currDate
//                            )
//                            replyList.add(replyComment)
//                            Log.d("cekreplyan", replyList[i].content.toString())
//                            updateReplyComment()
//                        }
//                        Log.d("cekisiarray1", replyList[0].toString())
//                    }
//                },
//            Response.ErrorListener {
//                Log.e("apiresult2", it.message.toString())
//            })
//        {
//            override fun getParams() = hashMapOf(
//                "comment_id" to comment_id.toString()
//            )
//        }
//        q.add(stringRequest)
    } //Not used anymore, move to CommentAdapter

    fun addReplyComment(new_reply: String, comment_id: Number){
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences = this.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var idCompanion="IDUSER";
        var firstNameCompanion = "FIRST";
        var lastNameCompanion = "LAST";
        var idUser=shared.getInt(idCompanion,0);
        var firstName=shared.getString(firstNameCompanion,"tes")
        var lastName=shared.getString(lastNameCompanion,"tes")

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/add_reply_comment.php"

        var stringRequest = object:StringRequest(Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult1", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    editTextComment.setText("")
                    updateComment()
                    editTextComment.setHint("Comment...")
                    the_comment = null
                    commandReply = ""
                    Log.d("cekkkk11", the_comment.toString())
                } else {
                    Toast.makeText(this, "Reply Comment failed", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult2", it.message.toString())
            })
        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String,String>()
                params["content"]= new_reply.toString()
                params["comment_id"]= comment_id.toString()
                params["user_id"]= idUser.toString()
                return params
            }
        }
        q.add(stringRequest)
    }

    fun updateReplyComment(){
//        val lm: LinearLayoutManager = LinearLayoutManager(this)
//        var recyclerView = recyclerViewReply
//        recyclerView?.layoutManager = lm
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.adapter = ReplyCommentAdapter(replyList)
    } //Not used anymore, move to CommentAdapter

    private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            commandReply = intent.getStringExtra(ARG_COMMAND)
            the_comment = intent.getParcelableExtra(ARG_COMMENT)
            editTextComment.setHint("Reply comment...")
            editTextComment.requestFocus()
        }
    }
    override fun onResume() {
        super.onResume()
        registerReceiver(myReceiver, IntentFilter("my_custom_event"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myReceiver)
    }
}