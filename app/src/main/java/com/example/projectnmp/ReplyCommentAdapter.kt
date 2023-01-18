package com.example.projectnmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.template_comment.view.*

class ReplyCommentAdapter (var replyList: ArrayList<ReplyComment>):
    RecyclerView.Adapter<ReplyCommentAdapter.ReplyCommentViewHolder>(){
    class ReplyCommentViewHolder(val v: View): RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyCommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.template_reply_comment, parent,false)
        return ReplyCommentAdapter.ReplyCommentViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReplyCommentViewHolder, position: Int) {
        var full_name:String = replyList[position].first_name
        if(replyList[position].last_name != null){
            full_name = full_name + " " + replyList[position].last_name
        }
        holder.v.txtNameComment.text = full_name
        holder.v.txtComment2.text =replyList[position].content
        holder.v.txtDateComment.text =replyList[position].publish_date
    }

    override fun getItemCount(): Int {
        return  replyList.size
    }

}