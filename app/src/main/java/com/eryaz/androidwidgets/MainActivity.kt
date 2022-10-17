package com.eryaz.androidwidgets

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var lastCall = String()
    private lateinit var liveData: MutableLiveData<Boolean>
    private lateinit var sp : SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp= this.getSharedPreferences("LastCall",Context.MODE_PRIVATE)
        editor=sp.edit()

        val lastDuration= sp.getString("last","0.0")

        liveData = MutableLiveData<Boolean>()
        liveData.value=false

        checkPermission()

        textView.setText(lastDuration + " seconds")


        call.setOnClickListener {

            textView.setText("")
            val number = editTextPhone.text.toString()

            if (it.id == R.id.call) {

                if (number.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$number")
                    startActivity(intent)
                }
            }
            liveData.value = true
        }
    }
    override fun onResume() {
        super.onResume()

        if (liveData.value==true) {

            getCallDetails()
        }

    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG), 102)
        }

    }

    fun getCallDetails() {

        val contacts = CallLog.Calls.CONTENT_URI
        val cursor = this.contentResolver.query(contacts, null, null, null, null)
        val duration = cursor?.getColumnIndex(CallLog.Calls.DURATION)

        if (cursor != null) {
            lastCall = "0"
            while (cursor.moveToNext()) {

                val durationtime = duration?.let { cursor.getString(it) }

                lastCall = durationtime.toString()
            }
            cursor.close()

            editor.putString("last",lastCall)
            editor.commit()

            if (liveData.value==true) {
                textView.setText(lastCall + " seconds")
            }
        }
    }

}

