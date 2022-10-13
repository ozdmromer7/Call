package com.eryaz.androidwidgets

import android.Manifest

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        checkPermission()

        imageView.setOnClickListener {

            val number = editTextPhone.text.toString()

            if (number.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            }
        }
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
        val sb =StringBuffer()
        val contacts = CallLog.Calls.CONTENT_URI
        val cursor = this.contentResolver.query(contacts,null,null,null,null)
        val duration = cursor?.getColumnIndex(CallLog.Calls.DURATION)
        val date = cursor?.getColumnIndex(CallLog.Calls.DATE)
        val number = cursor?.getColumnIndex(CallLog.Calls.CACHED_NAME)
        sb.append("Call Details: ")

        if (cursor != null) {
             var x = 0
             var name = ""
            while (cursor.moveToNext()){


                val durationtime = duration?.let { cursor.getString(it) }
                val date = date?.let { cursor.getString(it) }
                val number = number?.let { cursor.getString(it) }

                sb.append("\nDuration Time"+ durationtime)
                x = durationtime.toString().toInt()
                name = number.toString()

            }
            cursor.close()
            println(x.toString())
            textView.setText(x.toString()+"seconds by "+ name)

        }

    }


}

