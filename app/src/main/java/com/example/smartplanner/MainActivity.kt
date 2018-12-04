package com.example.smartplanner

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.activity_main.*
import android.support.constraint.ConstraintSet



class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 123
    var manager : AlarmManager? = null
    var layout : ConstraintLayout? = null
    var saved_d_t : Long = 0L
    var myToggle : ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = this.findViewById<ConstraintLayout>(R.id.activity_mainid)

        manager = getSystemService(ALARM_SERVICE) as AlarmManager

        addAlarmBtn.setOnClickListener { _ ->

            val alarm_number = 1;

            val intent = Intent(this.applicationContext, AddActivity::class.java);
            intent.putExtra("request",alarm_number);
            startActivityForResult(intent, REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val myintent = Intent(this, TriggerredActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this, 0, myintent, PendingIntent.FLAG_ONE_SHOT
                )

                val dateAndTime = data!!.getLongExtra("dateandtime", 0)
                val routeTime = data!!.getLongExtra("routeTime", 0)
                val preparingTime = data!!.getLongExtra("preparingTime", 0)

                //Toast.makeText(this, "*Замена будильника: ${dateAndTime} ${dateAndTime - routeTime - preparingTime}", Toast.LENGTH_LONG).show();

                val alarmTime = dateAndTime - routeTime - preparingTime

                if (alarmTime > 0) {
                    manager?.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
                    Toast.makeText(this, "Напоминание установлено", Toast.LENGTH_LONG).show();

                    saved_d_t = alarmTime

                    val toggle = ToggleButton(this)
                    toggle.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
                    toggle.id = R.id.my_toogle_1
                    toggle.isChecked = true
                    toggle.setTextSize(17F)
                    toggle.setTextOff("Напоминание отключено");
                    toggle.setTextOn("Напоминание включено");
                    toggle.setText("Напоминание включено");
                    toggle.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            val newmyintent = Intent(this, TriggerredActivity::class.java)
                            val newpendingIntent= PendingIntent.getActivity(this, 0, newmyintent, PendingIntent.FLAG_ONE_SHOT)
                            manager?.set(AlarmManager.RTC_WAKEUP, saved_d_t, newpendingIntent)
                        }
                        else {
                            val newmyintent = Intent(this, TriggerredActivity::class.java)
                            val newpendingIntent= PendingIntent.getActivity(this, 0, newmyintent, PendingIntent.FLAG_ONE_SHOT)
                            manager?.cancel(newpendingIntent)
                        }
                    }
                    myToggle = toggle
                    layout?.addView(toggle)

                    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                    with (sharedPref.edit()) {
                        putBoolean("toggle_state",true)
                        commit()
                    }
                }
                else
                    Toast.makeText(this, "Некорректное время", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (myToggle == null) {
            return
        }
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val state = sharedPref.getBoolean("toogle_state", false)

        myToggle?.isChecked = state
    }

}
