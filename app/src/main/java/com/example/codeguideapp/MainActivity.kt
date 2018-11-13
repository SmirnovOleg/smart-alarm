package com.example.codeguideapp

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yandex.mapkit.Animation
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TimePicker
import android.text.format.DateUtils
import android.text.format.DateUtils.formatDateTime
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Установка и подключение Yandex MapKit
        MapKitFactory.setApiKey("@string/api_key")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)
        mapview?.getMap()?.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5.0f), null
        )

        //Установка времени
        getTime(alarmTime)
    }

    override fun onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapview.onStart()
    }

    fun getTime(textView: TextView) {

        val cal = Calendar.getInstance()

        val timeSetListener = OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView.text = SimpleDateFormat("HH:mm", Locale.US).format(cal.time)
        }

        changeTimeBtn.setOnClickListener {
            TimePickerDialog(
                this@MainActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

}
