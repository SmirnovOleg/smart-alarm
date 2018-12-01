package com.example.smartplanner

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 123;
    var manager :AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(ALARM_SERVICE) as AlarmManager

        addAlarmBtn.setOnClickListener { _ ->

            val alarm_number=666;

            val intent = Intent(this.applicationContext, AddActivity::class.java);
            intent.putExtra("request",alarm_number);
            startActivityForResult(intent, REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK) {
                val myintent = Intent(this, TriggerredActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this, 0, myintent, PendingIntent.FLAG_ONE_SHOT
                )

                val dateAndTime = data!!.getLongExtra("dateandtime", 0)
                val routeTime = data!!.getLongExtra("routeTime", 0)
                val preparingTime = data!!.getLongExtra("preparingTime", 0)

                Toast.makeText(this, "*Замена будильника: ${dateAndTime} ${dateAndTime - routeTime - preparingTime}", Toast.LENGTH_LONG).show();

                val alarmTime = dateAndTime - routeTime - preparingTime
                if (alarmTime > 0) {
                    //manager?.set(AlarmManager.RTC_WAKEUP, dateAndTime, pendingIntent)
                    Toast.makeText(this, "Напоминание установлено", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this, "Некорректное время", Toast.LENGTH_LONG).show();
            }
        }
    }
}
