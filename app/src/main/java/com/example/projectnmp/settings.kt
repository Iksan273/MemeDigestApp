package com.example.projectnmp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_meme_create.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.ByteArrayOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class settings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val REQUEST_SELECT_FOTO = 1
    val REQUEST_SELECT_GALERI = 2
    val REQUEST_IMAGE_CAPTURE = 3
    var imageProfileinbyte= byteArrayOf()
    var stringProfile="";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongThread")
    override fun onResume() {
        super.onResume()
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences =
            activity?.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var editor: SharedPreferences.Editor=shared.edit();
        var userCompanion = "USERNAME";
        var firstNameCompanion = "FIRST";
        var lastNameCompanion = "LAST";
        var dateCompanion = "DATE";
        var idCompanion="IDUSER";
        var avatarLink="AVATAR";
        var privacy="PRIVACY";
        var usernama=shared.getString(userCompanion,"yuhu");
        var firstName=shared.getString(firstNameCompanion,"tes")
        var lastName=shared.getString(lastNameCompanion,"tes")
        var date=shared.getString(dateCompanion,"tes")
        var avatar=shared.getString(avatarLink,"tes")
        var fullname=firstName+" "+lastName;
        var privasiSetting=shared.getString(privacy,"tes")
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateConvert = format.parse(date)

//        Log.d("fname2", firstName.toString())
//        Log.d("lname", lastName.toString())
//        Log.d("ps", privasiSetting.toString())

        val outputFormat = SimpleDateFormat("MMMM yyyy")
        val tanggalString = outputFormat.format(dateConvert)
        var contentResolver:ContentResolver
        var idUser=shared.getInt(idCompanion,0);

        txtNameSetting.setText(fullname.toString())
        txtSinceSetting.setText("Active Since "+tanggalString.toString())
        txtUsernameSetting.setText(usernama.toString())
        if(avatar!="")
        {
            val myByteArray: ByteArray = Base64.decode(avatar, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray.size)
            imgProfileSetting.setImageBitmap(bitmap);

        }

        txtFirstNameSetting.setText(firstName.toString())
        txtLastNameSetting.setText(lastName.toString())
        fabLogoutSetting.setOnClickListener{
            editor.remove(userCompanion)
            editor.remove(idCompanion)
            editor.remove(firstNameCompanion)
            editor.remove(lastNameCompanion)
            editor.remove(avatarLink)
            editor.remove(privacy)
            editor.apply()
            val activity: Activity? = activity
            val intent = Intent(activity, Login::class.java)
            activity?.startActivity(intent)
        }
        if(privasiSetting=="true"){
            cbHideNameSetting.isChecked=true;
        }
        else
        {
            cbHideNameSetting.isChecked=false
        }


        btnUpdateProfile.setOnClickListener {
            val profileImage=(imgProfileSetting.drawable as BitmapDrawable).bitmap
            val stream= ByteArrayOutputStream()
            profileImage.compress(Bitmap.CompressFormat.JPEG,100,stream)
            imageProfileinbyte=stream.toByteArray()

            val encoded: String = Base64.encodeToString(imageProfileinbyte, Base64.DEFAULT)

            var updateFirstName=txtFirstNameSetting.text.toString();
            var updateLastName=txtLastNameSetting.text.toString();
            var updatePrivasi="";

            if(cbHideNameSetting.isChecked)
            {
                updatePrivasi="true"
            }
            else
            {
                updatePrivasi="false"
            }

            val vol = Volley.newRequestQueue(activity)
            val url = "https://ubaya.fun/hybrid/160420077/meme_api/update_profile.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { Log.d("Berhasil update Profile", it)

                    editor.putString(firstNameCompanion,updateFirstName)
                    editor.putString(lastNameCompanion,updateLastName)
                    editor.putString(avatarLink,encoded)
                    editor.putString(privacy,updatePrivasi)
                    editor.apply()
                    editor.commit()
                    txtNameSetting.setText(updateFirstName.toString() + " "+ updateLastName.toString())
                    txtFirstNameSetting.setText(updateFirstName.toString())
                    txtLastNameSetting.setText(updateLastName.toString())
//                    var navbar=navView.getHeaderView(0)
                    requireActivity().navView.txtNameDrawer.setText(updateFirstName.toString() + " "+ updateLastName.toString())
                    requireActivity().navView.txtIdDrawer.setText(usernama.toString())
                    avatar=shared.getString(avatarLink,"tes")
                    val myByteArray: ByteArray = Base64.decode(avatar, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray.size)
                    requireActivity().navView.imgDrawer.setImageBitmap(bitmap)
                                  },
                Response.ErrorListener {
                    Log.d("Gagal Update Profile", it.message.toString())
                }
            )
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String,String>()

                    params["first_name"] = updateFirstName.toString()
                    params["last_name"] = updateLastName.toString()
                    params["avatar_link"] = encoded.toString()
                    params["privacy_setting"] = updatePrivasi.toString()
                    params["user_id"] = idUser.toString();
                    return params


                }

            }
            vol.add(stringRequest)
            editor.putString(firstNameCompanion,updateFirstName)
            editor.putString(lastNameCompanion,updateLastName)
            editor.putString(avatarLink,encoded)
            editor.putString(privacy,updatePrivasi)
            editor.apply()

            Toast.makeText(activity,"berhasil update Profile", Toast.LENGTH_SHORT).show()

        }

        imgProfileSetting.setOnClickListener{
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_SELECT_FOTO)
            } else {
                selectImage(editor)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences =
            activity?.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var editor: SharedPreferences.Editor=shared.edit();
        var contentResolver:ContentResolver
        if(resultCode == Activity.RESULT_OK) {
           if(requestCode == REQUEST_SELECT_GALERI) {
                val selectedImageUri = data?.data
//                var content:ContentResolver
                val bitmap2 : Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedImageUri)
//                editor.putString()
                imgProfileSetting.setImageBitmap(bitmap2)
            } else if(requestCode == REQUEST_SELECT_FOTO){
               data?.extras?.let { imgProfileSetting.setImageBitmap(it.get("data") as Bitmap)}
           }
        }
        // do not remove this line
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) { REQUEST_SELECT_FOTO -> {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                takePicture()
            else
                Toast.makeText(activity, "You must grant permission to access the camera.", Toast.LENGTH_LONG).show()
        } }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val navBar = navView.getHeaderView(0)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment settings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun selectImage(editor: SharedPreferences.Editor) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                takePicture()
            } else if (options[item] == "Choose from Gallery") {
                selectPictGalery(editor)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    fun takePicture() {
        var sharedfile="com.example.projectnmp"
        val shared: SharedPreferences =
            activity?.getSharedPreferences(sharedfile, Context.MODE_PRIVATE) ?: return
        var editor: SharedPreferences.Editor=shared.edit();

        val i = Intent()
        i.action = MediaStore.ACTION_IMAGE_CAPTURE
        editor.putString("AVATAR","")
        editor.apply()
        startActivityForResult(i, REQUEST_SELECT_FOTO)
    }

    fun selectPictGalery(editor: SharedPreferences.Editor){
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"
        editor.putString("AVATAR","")
        editor.apply()
        startActivityForResult(pickImageIntent, REQUEST_SELECT_GALERI)
    }
}