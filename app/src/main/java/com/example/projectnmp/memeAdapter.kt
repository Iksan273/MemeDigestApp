package com.example.projectnmp
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_template.view.*
import org.json.JSONObject

class memeAdapter (val memeList:ArrayList<meme>, val user_id: Int):RecyclerView.Adapter<memeAdapter.memeViewHolder>(){
    class  memeViewHolder(val v:View):RecyclerView.ViewHolder(v)

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memeViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        var v=inflater.inflate(R.layout.card_template,parent,false)
        return memeViewHolder(v)
    }

    override fun onBindViewHolder(holder: memeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val url=memeList[position].meme_url
        var result = null
        Picasso.get().load(url).into(holder.v.imgMeme)
        holder.v.txtLikes.text=memeList[position].num_like.toString()
        holder.v.txtTop.text=memeList[position].top_text
        if(memeList[position].liked == user_id){
            holder.v.btnLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
        } else {
            holder.v.btnLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
        }

        holder.v.txtButtom.text=memeList[position].bottom_text
        holder.v.txtComment2.text = memeList[position].jum_comment.toString()
        holder.v.btnComment.setOnClickListener{
            val intent = Intent(holder.v.context, DetailMeme::class.java)
//            intent.putExtra("meme_id", memeList[position].meme_id)
//            intent.putExtra("meme_url", memeList[position].meme_url)
//            intent.putExtra("meme_top_text", memeList[position].top_text)
//            intent.putExtra("meme_bottom_text", memeList[position].bottom_text)
//            intent.putExtra("meme_num_like", memeList[position].num_like)
//            intent.putExtra("meme_user_id", memeList[position].user_id)
//            intent.putExtra("meme_liked", memeList[position].liked)
            intent.putExtra("the_meme", memeList[position])
            holder.v.context.startActivity(intent)
        }

        holder.v.btnLikes.setOnClickListener{
            like(holder, position, "likes")
        }
    }

    fun like(holder: memeViewHolder, @SuppressLint("RecyclerView") position: Int, command: String){
        val q =Volley.newRequestQueue(context)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/like_process.php"
        val stringRequest =  object : StringRequest(
            Request.Method.POST, url,
            Response.Listener {
                Log.d("cekparams", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success" && command == "likes") {
                    memeList[position].num_like++
                    var newlikes = memeList[position].num_like
                    holder.v.txtLikes.text = "$newlikes"
                    holder.v.btnLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
                    holder.v.btnLikes.tag = R.drawable.ic_baseline_favorite_24
                } else if (obj.getString("result") == "success" && command == "unlike") {
                    memeList[position].num_like--
                    var newlikes = memeList[position].num_like
                    holder.v.txtLikes.text = "$newlikes"
                    holder.v.btnLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
                    holder.v.btnLikes.tag = R.drawable.ic_baseline_favorite_border_24
                } else if (obj.getString("result") != "success" && command == "likes"){
                    like(holder, position, "unlike")
                }
//                memeList[position].num_like--
//                var newlikes = memeList[position].num_like
//                holder.v.txtLikes.text = "$newlikes"
//                holder.v.btnLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
//                holder.v.btnLikes.tag = R.drawable.ic_baseline_favorite_border_24
            },
            Response.ErrorListener {
                Log.d("cekErrorLikes", it.message.toString())
            }
        )
        {
            override fun getParams() = hashMapOf(
                "meme_id" to memeList[position].meme_id.toString(),
                "user_id" to user_id.toString(),
                "command" to command
            )
        }
        q.add(stringRequest)
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemCount(): Int {
        return memeList.size
    }


}