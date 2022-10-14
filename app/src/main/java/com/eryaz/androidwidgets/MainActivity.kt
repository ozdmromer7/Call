package com.eryaz.androidwidgets

import android.Manifest
import android.content.Context

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        checkPermission()

        call.setOnClickListener {


            val number = editTextPhone.text.toString()

            if (it.id == R.id.call) {

                if (number.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$number")
                    startActivity(intent)

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onResume() {
        super.onResume()

            getCallDetails()
        
    }
    fun checkPermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),101)
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){

          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG),102)
        }

    }

    fun getCallDetails(){

        //val sp = this.getSharedPreferences("Last Call", Context.MODE_PRIVATE)
       // val editor = sp.edit()

        val contacts = CallLog.Calls.CONTENT_URI
        val cursor = this.contentResolver.query(contacts,null,null,null,null)
        val duration = cursor?.getColumnIndex(CallLog.Calls.DURATION)
        val number = cursor?.getColumnIndex(CallLog.Calls.CACHED_NAME)

        if (cursor != null) {
             var lastCall = 0
             var name = ""
            while (cursor.moveToNext()){

                val durationtime = duration?.let { cursor.getString(it) }
                val number = number?.let { cursor.getString(it) }

                lastCall = durationtime.toString().toInt()
                name = number.toString()
            }
            cursor.close()

            //editor.putInt("Duration",lastCall)
            //editor.apply()

            //val t=sp.getString("Duration","0")

            textView.setText(lastCall.toString() +" seconds \nCalled "+ name)

        }

    }


}

