package com.example.projectnmp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Daily Meme Digest"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnUpdateProfile.setOnClickListener {

            if(txtUsernameRegis.text?.isNotEmpty() == true && txtFirstName.text?.isNotEmpty() == true
                &&txtLastName.text?.isNotEmpty() == true && txtPasswordRegis.text?.isNotEmpty() == true
                 )
            {
                if(txtRePassword.text.toString()==txtPasswordRegis.text.toString()){
                    val q = Volley.newRequestQueue(this)
                    val url = "https://ubaya.fun/hybrid/160420077/meme_api/register_process.php"

                    var stringRequest =object: StringRequest(
                        Request.Method.POST, url, Response.Listener { Log.d("Berhasil Register", it) },
                        Response.ErrorListener {
                            Log.e("Gagal", it.message.toString())
                        }
                    )
                    {
                        override fun getParams(): MutableMap<String,String> {
                            val params=HashMap<String,String>()

                            params["username"]=txtUsernameRegis.text.toString();
                            params["first_name"]=txtFirstName.text.toString();
                            params["last_name"]=txtLastName.text.toString();
                            params["password"]=txtPasswordRegis.text.toString();

                            params["privacy_setting"]="false";

                            return params
                        }
                    }
                    q.add(stringRequest)

                    var intent=Intent(this,Login::class.java)
                    startActivity((intent))
                    finish()

                }
                else{
                    Toast.makeText(this, "Password tidak cocok , silahkan ulangi", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Kolom tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }


}