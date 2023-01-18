package com.example.projectnmp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.template_comment.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat

class CommentAdapter (var commentList: ArrayList<Comment>):
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
        class CommentViewHolder(val v:View):RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.template_comment, parent,false)
        return CommentViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var full_name:String = commentList[position].first_name
        if(commentList[position].last_name != null){
            full_name = full_name + " " + commentList[position].last_name
        }
        holder.v.txtNameComment.text = full_name
        holder.v.txtComment2.text =commentList[position].content
        holder.v.txtDateComment.text =commentList[position].publish_date

        ShowReply(holder, position)

        holder.v.btnReply.setOnClickListener{
            val intent = Intent("my_custom_event")
            intent.putExtra("the_comment", commentList[position])
            intent.putExtra("command", "reply_comment")
            holder.v.context.sendBroadcast(intent)
        }
    }

    fun ShowReply(holder: CommentViewHolder, position: Int){
        val q = Volley.newRequestQueue(holder.v.context)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_reply_comment.php"
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        var stringRequest = object: StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult12", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success") {
                    var data = obj.getJSONArray("data")
                    replyList.clear()
                    for(i in 0 until data.length()) {
                        val replyObj = data.getJSONObject(i)
                        var date  = formatter.parse(replyObj.getString("publish_date"))
                        var currDate = SimpleDateFormat("DD MMM yy").format(date)
                        val replyComment = ReplyComment(
                            replyObj.getString("content"),
                            replyObj.getString("first_name"),
                            replyObj.getString("last_name"),
                            currDate
                        )
                        replyList.add(replyComment)
                        Log.d("cekreplyan", replyList[i].content.toString())

                        val lm: LinearLayoutManager = LinearLayoutManager(holder.v.context)
                        var recyclerView = holder.v.recyclerViewReply
                        recyclerView?.layoutManager = lm
                        recyclerView?.setHasFixedSize(true)
                        recyclerView?.adapter = ReplyCommentAdapter(replyList)
                    }
                    Log.d("cekisiarray1", replyList[0].toString())
                }
            },
            Response.ErrorListener {
                Log.e("apiresult2", it.message.toString())
            })
        {
            override fun getParams() = hashMapOf(
                "comment_id" to commentList[position].comment_id.toString()
            )
        }
        q.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}