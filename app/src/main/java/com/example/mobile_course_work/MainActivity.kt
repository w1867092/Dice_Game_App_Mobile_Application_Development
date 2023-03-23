/**
 * Video demonstration google drive link :- https://drive.google.com/drive/folders/1YcyB3RapuobGsVnEbgARjipE8dTjev_Z?usp=share_link
 * @author Kumod Hansidu Munasingha Arachchi
 * UOW number :- w1867092
 * IIT number :- 20210336
 */

package com.example.mobile_course_work

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btAbout = findViewById<Button>(R.id.about)
        btAbout.setOnClickListener {
            my_button_onClick_working(btAbout)
        }

        val btNewGame = findViewById<Button>(R.id.newgame)
        btNewGame.setOnClickListener {
            val toTheGame = Intent(this, NewGame::class.java)
            startActivity(toTheGame)
        }



    }


    @SuppressLint("ClickableViewAccessibility")
    fun my_button_onClick_working(view: View?) {

        // step 1
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_layout, null)

        // step 2
        val wid = LinearLayout.LayoutParams.WRAP_CONTENT
        val high = LinearLayout.LayoutParams.WRAP_CONTENT
        val focus= true
        val popupWindow = PopupWindow(popupView, wid, high, focus)

        // step 3
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }



    }
}