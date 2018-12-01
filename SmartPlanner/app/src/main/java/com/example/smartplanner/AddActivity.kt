package com.example.smartplanner

import android.annotation.SuppressLint
import android.app.*
import android.app.ProgressDialog.show
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
import android.content.Context
import android.content.Intent
import android.content.Context.ALARM_SERVICE
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_add.view.*


class AddActivity : AppCompatActivity() {

    private val dateAndTime = Calendar.getInstance()
    val REQUEST_CODE = 124
    private var routeTime = 0L
    private var preparingTime = 60L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        createListView()
        setListViewListener()
        setCurrentTimeTextView()

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                preparingTime = progress.toLong()
                seekBarText.text = "Время, чтобы собраться: ${preparingTime} минут"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        okBtn.setOnClickListener{ _ ->

            val ResultIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("dateandtime", dateAndTime.timeInMillis.toLong())
                putExtra("routeTime", routeTime * 60_000L)
                putExtra("preparingTime", preparingTime * 60_000L)
            }
            setResult(android.app.Activity.RESULT_OK, ResultIntent)
            finish()
        }

        cancelBtn.setOnClickListener { _ ->
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                routeTime = data!!.getStringExtra("routeTime").toLong();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentTimeTextView() {
        val date = DateUtils.formatDateTime(this, dateAndTime.timeInMillis, DateUtils.FORMAT_SHOW_DATE)
        val time = DateUtils.formatDateTime(this, dateAndTime.timeInMillis, DateUtils.FORMAT_SHOW_TIME)
        currentTimeTextView.text = "$time, $date"
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
                    val intent = Intent(this.applicationContext, MapActivity::class.java);
                    startActivityForResult(intent, REQUEST_CODE)
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
