package com.example.projectnmp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_meme.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_meme_create.*
import kotlinx.android.synthetic.main.card_template.view.*


class memeCreate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_create)
        var like=0;
        txtTopTextPreview.isVisible=false;
        txtBottomTextPreview.isVisible=false;
        var sharedfile="com.example.projectnmp"
        var shared: SharedPreferences =getSharedPreferences(sharedfile, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=shared.edit();
        var userCompanion="USERNAME";
        var idCompanion="IDUSER";
        var nama=shared.getString(userCompanion,"yuhu");
        var id=shared.getInt(idCompanion,0);

//        btnPreview.setOnClickListener {
//
//            txtTopTextPreview.isVisible=true;
//            txtBottomTextPreview.isVisible=true;
//
//            if(!txtUrlAdd.text.isNullOrEmpty() && !txtTopTextAdd.text.isNullOrEmpty()
//                && !txtBottomTextAdd.text.isNullOrEmpty()){
//
//                var imgUrl=txtUrlAdd.text.toString();
//                Picasso.get().load(imgUrl).into(imgPreview)
//                txtTopTextPreview.setText(txtTopTextAdd.text.toString())
//                txtBottomTextPreview.setText(txtBottomTextAdd.text.toString())
//
//
//            }
//            else{
//
//                Toast.makeText(this,"Kolom tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
//
//            }
//        }
        txtUrlAdd.addTextChangedListener(){
            txtTopTextPreview.isVisible=true;
            txtBottomTextPreview.isVisible=true;


            if(txtUrlAdd.text?.isNotEmpty() == true){

                var imgUrl=txtUrlAdd.text.toString();
                Picasso.get().load(imgUrl).into(imgPreview)
//                txtTopTextPreview.setText(txtTopTextAdd.text.toString())
//                txtBottomTextPreview.setText(txtBottomTextAdd.text.toString())


            }
            else
            {
                Toast.makeText(this,"Kolom URL tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }

        }
        txtTopTextAdd.addTextChangedListener(){
            if(txtTopTextAdd.text?.isNotEmpty()==true){
                txtTopTextPreview.setText(txtTopTextAdd.text.toString())
            }
            else{
                Toast.makeText(this,"Kolom top text tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
        txtBottomTextAdd.addTextChangedListener(){
            if(txtBottomTextAdd.text?.isNotEmpty()==true){
                txtBottomTextPreview.setText(txtBottomTextAdd.text.toString())
            }
            else{
                Toast.makeText(this,"Kolom Bottom text tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }


        btnSubmit.setOnClickListener {
            if(!txtUrlAdd.text.isNullOrEmpty() && !txtTopTextAdd.text.isNullOrEmpty()
                && !txtBottomTextAdd.text.isNullOrEmpty())
            {
                val vol = Volley.newRequestQueue(this)
                val url = "https://ubaya.fun/hybrid/160420077/meme_api/add_memes.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { Log.d("Berhasil add", it) },
                    Response.ErrorListener {
                        Log.d("Gagal", it.message.toString())
                    }
                )
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String,String>()

                        params["memeUrl"] = txtUrlAdd.text.toString()
                        params["top_text"] = txtTopTextAdd.text.toString()
                        params["bottom_text"] = txtBottomTextAdd.text.toString()
                        params["num_likes"] = "0";
                        params["user_id"] = id.toString();
                        return params


                    }

                }
                vol.add(stringRequest)
                finish()
            }
            else
            {
                Toast.makeText(this,"Kolom tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }

//




//
//
        }
        btnBackCreate.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
    fun refreshFragment()
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = Home()
        fragmentTransaction.add(R.id.viewPagerUtama, myFragment)

    }
}