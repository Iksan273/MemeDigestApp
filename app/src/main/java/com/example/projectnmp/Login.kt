package com.example.projectnmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.util.Objects


class Login : AppCompatActivity() {
    companion object {
        var userCompanion = "USERNAME";
        var firstNameCompanion = "FIRST";
        var lastNameCompanion = "LAST";
        var dateCompanion = "DATE";
        var idCompanion="IDUSER";
        var avatarLink="AVATAR";
        var privacy="PRIVACY";
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var users:ArrayList<user> = ArrayList()
        var sharedfile="com.example.projectnmp"
        var shared: SharedPreferences =getSharedPreferences(sharedfile, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=shared.edit();
        editor.putString(userCompanion,"1");
        editor.apply();

        btnCreateAcc.setOnClickListener {
            finish()
            var intent=Intent(this,Register::class.java)
            startActivity((intent))
        }
        btnSignIn.setOnClickListener {

            if(txtUsername.text?.isNotEmpty() == true && txtPassword.text?.isNotEmpty() == true)
            {
                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.fun/hybrid/160420077/meme_api/login_process.php"

                var stringRequest =object:StringRequest(
                    Request.Method.POST, url,
                    {
                        Log.d("apiresult", it)
                        val obj = JSONObject(it)
                        if(obj.getString("result") == "success") {
                            val data = obj.getJSONObject("data")

                            for(i in 0 until data.length()) {
                                var idUser=data.getInt("user_id")
                                var username_login=data.getString("username")
                                var first_login=data.getString("first_name")
                                var last_login=data.getString("last_name")
                                var date_login=data.getString("registration_date")
                                var avatar=data.getString("avatar_link")
                                var privasi=data.getString("privacy_setting")
                                editor.putInt(idCompanion,idUser)
                                editor.putString(userCompanion,username_login)
                                editor.putString(firstNameCompanion,first_login)
                                editor.putString(lastNameCompanion,last_login)
                                editor.putString(dateCompanion,date_login)
                                editor.putString(avatarLink,avatar)
                                editor.putString(privacy,privasi)
                                editor.apply()
                                val userData = user (data.getInt("user_id"),
                                    data.getString("username"),
                                    data.getString("first_name"),
                                    data.getString("last_name"),
                                    data.getString("password"),
                                    data.getString("registration_date"),
                                    data.getString("avatar_link"),
                                    data.getString("privacy_setting")



                                )
                                users.add(userData)
                                var intent=Intent(this,MainActivity::class.java)
                                startActivity((intent))
                                finish()
                            }

                            Log.d("cekisiarray", users.toString())
                        }
                        else{
                            Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                        }
                    },
                    {
                        Log.e("apiresult", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String,String> {
                        val params=HashMap<String,String>()

                        params["username"]=txtUsername.text.toString();
                        params["password"]=txtPassword.text.toString();

                        return params
                    }
                }
                q.add(stringRequest)
            }
            else
            {
                Toast.makeText(this, "Kolom Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }









        }
    }
}