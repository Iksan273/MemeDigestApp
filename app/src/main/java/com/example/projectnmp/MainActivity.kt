package com.example.projectnmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_header.view.imgDrawer
import kotlinx.android.synthetic.main.drawer_header.view.txtIdDrawer
import kotlinx.android.synthetic.main.drawer_header.view.txtNameDrawer
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONObject
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val fragments:ArrayList<Fragment> = ArrayList()
    var memeList:ArrayList<meme> = ArrayList()
    var bg:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        setContentView(R.layout.drawer_layout)
        supportActionBar?.title = "Daily Meme Digest"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,
                R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/hybrid/160420077/meme_api/get_memes2.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult12", it)
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
                    Log.d("cekisimeme12", memeList.toString())
                    var rand = (0..3).random()
                    bg = memeList[rand].meme_url.toString()
                    val navBar = navView.getHeaderView(0)
                    Glide.with(this).load(bg)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation()))
                        .into(navBar.backgroundDrawer)
                }
            },
            {
                Log.e("apiresultmeme", it.message.toString())
            })
        q.add(stringRequest)

        fragments.add(Home())
        fragments.add(Creation())
        fragments.add(leaderboard())
        fragments.add(settings())

        val adapter=fragmentAdapter(this,fragments)
        viewPagerUtama.adapter=adapter

        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences = getSharedPreferences(sharedfile, MODE_PRIVATE)
        var editor: SharedPreferences.Editor=shared.edit();
        var userCompanion = "USERNAME";
        var firstNameCompanion = "FIRST";
        var lastNameCompanion = "LAST";
        var idCompanion="IDUSER";
        var avatarLink="AVATAR";
//        var usernama=shared.getString(userCompanion,"yuhu");
//        var firstName=shared.getString(firstNameCompanion,"tes")
//        var lastName=shared.getString(lastNameCompanion,"tes")
//        var avatar=shared.getString(avatarLink,"tes")
////
//        var fullname=firstName+" "+lastName;

        var idUser=shared.getInt(idCompanion,0);

        val navBar = navView.getHeaderView(0)
//        navBar.txtNameDrawer.setText(fullname.toString())
//        navBar.txtIdDrawer.setText(usernama.toString())
//        val myByteArray: ByteArray = Base64.decode(avatar, Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray.size)
//        navBar.imgDrawer.setImageBitmap(bitmap)

        navBar.fabLogoutDrawer.setOnClickListener{
            editor.remove(userCompanion)
            editor.remove(idCompanion)
            editor.remove(firstNameCompanion)
            editor.remove(lastNameCompanion)
            editor.remove(avatarLink)
            editor.apply()
            var intent= Intent(this,Login::class.java)
            startActivity((intent))
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            viewPagerUtama.currentItem  = when(it.itemId) {
                R.id.menuHome -> 0
                R.id.menuCreation -> 1
                R.id.menuLeaderboard -> 2
                R.id.menuSettings->3
                else -> 0
            }

            true
        }
        navView.setNavigationItemSelectedListener {
             viewPagerUtama.currentItem = when(it.itemId) {
                R.id.menuHome -> 0
                R.id.menuCreation -> 1
                R.id.menuLeaderboard -> 2
                R.id.menuSettings -> 3
                else -> 0
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        viewPagerUtama.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
                navView.setCheckedItem(bottomNav.menu.getItem(position).itemId)
            }
        })


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences = getSharedPreferences(sharedfile, MODE_PRIVATE)
        var editor: SharedPreferences.Editor=shared.edit();
        var userCompanion = "USERNAME";
        var firstNameCompanion = "FIRST";
        var lastNameCompanion = "LAST";
        var idCompanion="IDUSER";
        var avatarLink="AVATAR";
        var usernama=shared.getString(userCompanion,"yuhu");
        var firstName=shared.getString(firstNameCompanion,"tes")
        var lastName=shared.getString(lastNameCompanion,"tes")
        var avatar=shared.getString(avatarLink,"tes")

//        Log.d("fname1", firstName.toString())
        var fullname=firstName+" "+lastName;

        var idUser=shared.getInt(idCompanion,0);

        val navBar = navView.getHeaderView(0)
        navBar.txtNameDrawer.setText(fullname.toString())
        navBar.txtIdDrawer.setText(usernama.toString())
        val myByteArray: ByteArray = Base64.decode(avatar, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray.size)
        navBar.imgDrawer.setImageBitmap(bitmap)
    }
}