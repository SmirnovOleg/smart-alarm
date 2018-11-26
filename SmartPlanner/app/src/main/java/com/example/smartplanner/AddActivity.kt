package com.example.smartplanner

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.UniversalTimeScale.toLong
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import android.widget.SimpleAdapter
import java.util.*
import android.widget.TimePicker

class AddActivity : AppCompatActivity() {

    private val dateAndTime = Calendar.getInstance()
    /*fun showCamera(view: View) {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            // Camera permission has not been granted.
            requestCameraPermission()
        } else {
            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,
                "CAMERA permission has already been granted. Displaying camera preview.")
            showCameraPreview()
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        createListView()
        setListViewListener()

        okBtn.setOnClickListener{ _ ->
            val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val myintent = Intent(this, TriggerredActivity::class.java)
           // val myIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, myintent, PendingIntent.FLAG_ONE_SHOT)
         //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            manager.set(AlarmManager.RTC_WAKEUP, dateAndTime.timeInMillis, pendingIntent)
            Toast.makeText(this, "SET", Toast.LENGTH_LONG).show();
        }
    }

    private val timePickerListener = TimePickerDialog.OnTimeSetListener {
            view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener {
            view, year, month, dayOfMonth ->
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, month)
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    private fun setListViewListener() {
        actionsListView.onItemClickListener = AdapterView.OnItemClickListener {
                    parent, view, position, id ->

            val itemValue = actionsListView.getItemAtPosition(position) as HashMap<*, *>

            when(id) {
                0L -> {
                    TimePickerDialog(
                        this,
                        timePickerListener,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true
                    ).show()
                }
                1L -> {
                    DatePickerDialog(
                        this,
                        datePickerListener,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }

        }
    }

    private fun createListView() {
        val actions : ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf(
                "title" to getString(R.string.time_title),
                "subtitle" to getString(R.string.instruction_time_edit)
            ),
            hashMapOf(
                "title" to getString(R.string.date_title),
                "subtitle" to getString(R.string.instruction_date_edit)
            ),
            hashMapOf(
                "title" to getString(R.string.location_title),
                "subtitle" to getString(R.string.instruction_location_edit)
            )
        )
        val adapter: SimpleAdapter = SimpleAdapter(
            this,
            actions,
            android.R.layout.simple_list_item_2,
            arrayOf("title", "subtitle"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        actionsListView.adapter = adapter
    }

}
