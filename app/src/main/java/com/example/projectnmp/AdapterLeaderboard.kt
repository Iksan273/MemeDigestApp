package com.example.projectnmp

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_template.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.template_leaderboard.view.*

class AdapterLeaderboard (val LeaderBoardList:ArrayList<LeaderboardClass>): RecyclerView.Adapter<AdapterLeaderboard.leaderViewHolder>(){
    class  leaderViewHolder(val v:View):RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): leaderViewHolder
    {
        val inflater= LayoutInflater.from(parent.context)
        var v=inflater.inflate(R.layout.template_leaderboard,parent,false)
        return leaderViewHolder(v)
    }

    override fun onBindViewHolder(holder: leaderViewHolder, position: Int) {
//        var privacyA="*****"
        val url=LeaderBoardList[position].avatar
        if(url!="")
        {
            val myByteArray: ByteArray = Base64.decode(url, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray.size)
            holder.v.imgProfileLB.setImageBitmap(bitmap)
        }

//        Picasso.get().load(url).into(holder.v.imgProfileLB)
        if(LeaderBoardList[position].privacy =="false")
        {
            var first=LeaderBoardList[position].first.toString()
            var last= LeaderBoardList[position].last.toString()
            var fullname=first+" "+last;
            holder.v.txtNameLB.text=fullname.toString()
        }
        else{
            var first=LeaderBoardList[position].first.toString()
            var last= LeaderBoardList[position].last.toString()
            var fullname=first+" "+last;
            val charArray = fullname.toCharArray()
            for (i in  3 until charArray.size)
            {
               charArray[i]='*'

            }
            var privacyFix= String(charArray)
            holder.v.txtNameLB.text=privacyFix.toString()
        }
        holder.v.txtLikeLB.text=LeaderBoardList[position].like.toString()


    }

    override fun getItemCount(): Int {
        return LeaderBoardList.size
    }
}