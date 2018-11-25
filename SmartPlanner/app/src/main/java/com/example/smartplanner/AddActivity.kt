package com.example.smartplanner

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.icu.util.UniversalTimeScale.toLong
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import android.widget.SimpleAdapter
import java.util.*
import android.widget.TimePicker
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Context.ALARM_SERVICE

class AddActivity : AppCompatActivity() {

    private val dateAndTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        createListView()
        setListViewListener()
        setCurrentTimeTextView()

        setAlarmBtn.setOnClickListener {
            val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)

            manager.set(AlarmManager.RTC_WAKEUP, dateAndTime.timeInMillis, pendingIntent)
            Toast.makeText(this, getString(R.string.alarm_complete), Toast.LENGTH_SHORT).show()
        }

        cancelBtn.setOnClickListener { _ ->
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this.applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setCurrentTimeTextView() {
        currentTimeTextView.text = dateAndTime.time.toString()
    }

    private val timePickerListener = TimePickerDialog.OnTimeSetListener {
            view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)
        setCurrentTimeTextView()
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener {
            view, year, month, dayOfMonth ->
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, month)
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        setCurrentTimeTextView()
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
                2L -> {
                    val intent: Intent? = Intent(this.applicationContext, MapActivity::class.java)
                    startActivity(intent)
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
