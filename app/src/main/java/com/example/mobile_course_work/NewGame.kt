/**
 * Video demonstration google drive link :- https://drive.google.com/drive/folders/1YcyB3RapuobGsVnEbgARjipE8dTjev_Z?usp=share_link
 * @author Kumod Hansidu Munasingha Arachchi
 * UOW number :- w1867092
 * IIT number :- w1867092
 */


package com.example.mobile_course_work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.random.Random

class NewGame : AppCompatActivity() {

    // to pass the data between landscape mode and portrait mode.
    private var bundle = Bundle()
    private lateinit var txtViewTotalComputer: TextView
    private lateinit var txtViewTotalYours: TextView
    private lateinit var btnScore: Button
    private val imgButtonComputer = ArrayList<ImageView>()
    private val imgButtonYous = ArrayList<ImageView>()
    private var computerScore = MutableList(size = 5) { 0 }
    private var userScore = MutableList(size = 5) { 0 }
    private var usersSelection = MutableList(5) { false }
    private var attempts: Int = 0
    private var numberOfWins: Int = 0
    private var numberOfLoses: Int = 0
    private var lastScore = MutableList(size = 2) { 0 }
    private var target: Int = 101
    private lateinit var targetTxt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        val btnThrow = findViewById<Button>(R.id.throwButton)
        val btnScore = findViewById<Button>(R.id.scoreButton)
        val winTxt = findViewById<TextView>(R.id.notification)

        txtViewTotalComputer = findViewById(R.id.scoreCom)
        txtViewTotalYours = findViewById(R.id.scoreyou)
        targetTxt = findViewById<TextView>(R.id.txt_target)

        imgButtonComputer.add(findViewById<ImageView>(R.id.imageViewC_1))
        imgButtonComputer.add(findViewById<ImageView>(R.id.imageViewC_2))
        imgButtonComputer.add(findViewById<ImageView>(R.id.imageViewC_3))
        imgButtonComputer.add(findViewById<ImageView>(R.id.imageViewC_4))
        imgButtonComputer.add(findViewById<ImageView>(R.id.imageViewC_5))

        imgButtonYous.add(findViewById<ImageView>(R.id.imageViewYo_1))
        imgButtonYous.add(findViewById<ImageView>(R.id.imageViewYo_2))
        imgButtonYous.add(findViewById<ImageView>(R.id.imageViewYo_3))
        imgButtonYous.add(findViewById<ImageView>(R.id.imageViewYo_4))
        imgButtonYous.add(findViewById<ImageView>(R.id.imageViewYo_5))

        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle("data_bundle")!!
            target = bundle.getInt("target")
            updateTheTarget(targetTxt)

            attempts = bundle.getInt("attempts")
            numberOfWins = bundle.getInt("numberOfWins")
            numberOfLoses = bundle.getInt("numberOfLoses")

            lastScore = bundle.getIntegerArrayList("arrayListLastScore")!!.toMutableList()
            displayScore(txtViewTotalComputer, txtViewTotalYours, winTxt)

            computerScore = bundle.getIntegerArrayList("arrayListComputerScore")!!.toMutableList()
            userScore = bundle.getIntegerArrayList("arrayListUserScore")!!.toMutableList()
//            usersSelection = bundle.getBooleanArray("arrayListUsersSelection ")!!.toMutableList()

            restoreDiceImages()
            restoreSelection()
            showVariableLogMessages()
        }

        btnThrow.setOnClickListener {
            attempts++
            btnScore.isEnabled = true

            var isSelected = false
            for (i in usersSelection)
                if (i) isSelected = true

            if (isSelected) {
                reRoll()
            } else
                setRandomImagesToBoth()

            if (attempts % 3 == 0) {
                btnScore.isEnabled = false
                displayScore(txtViewTotalComputer, txtViewTotalYours, winTxt)
                showVariableLogMessages()

                for ((index, image) in imgButtonYous.withIndex()) {
                    usersSelection[index] = false
                    image.background = null
                }
            }

            if (lastScore[0] == lastScore[1] && lastScore[0] >= target && attempts % 3 !=0){
                Toast.makeText(this,"The game is tie, There's a throw for a one last time" +
                        " then all five dice have to throws (no optional rerolls)\n" +
                        "it repeats for exceeding score.", Toast.LENGTH_SHORT).show()
                    setRandomImagesToBoth()

            }
            showVariableLogMessages()
        }

        targetTxt.setOnClickListener {
            if (attempts == 0) {
                toSetTarget(targetTxt)
            }
        }

        btnScore.setOnClickListener {
            btnScore.isEnabled = false
            if (attempts % 3 != 0) {
                displayScore(txtViewTotalComputer, txtViewTotalYours, winTxt)
                showVariableLogMessages()
            }
        }

        for ((index, imgView) in imgButtonYous.withIndex()) {
            imgView.setOnClickListener {

                val border: Drawable =
                    ResourcesCompat.getDrawable(resources, R.drawable.the_border, null)!!

                if (imgView.background == null) {
                    imgView.background = border
                    usersSelection[index] = true
                } else {
                    // if a background(border) is available for image view now, then remove the background (border)
                    imgView.background = null
                    usersSelection[index] = false
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val arrayListLastScore: ArrayList<Int> = lastScore.toCollection(ArrayList())
        val arrayListComputerScore: ArrayList<Int> = computerScore.toCollection(ArrayList())
        val arrayListUserScore: ArrayList<Int> = userScore.toCollection(ArrayList())

        bundle.putInt("target", target)
        bundle.putInt("attempts", attempts)
        bundle.putInt("numberOfWins", numberOfWins)
        bundle.putInt("numberOfLoses", numberOfLoses)

        bundle.putIntegerArrayList("arrayListLastScore", arrayListLastScore)
        bundle.putIntegerArrayList("arrayListComputerScore", arrayListComputerScore)
        bundle.putIntegerArrayList("arrayListUserScore", arrayListUserScore)
        bundle.putBooleanArray("arrayListUsersSelection", usersSelection.toBooleanArray())

        outState.putBundle("data_bundle", bundle)
    }

    private fun randomNumberGenerator(start: Int, end: Int): Int {
        return (start..end).random()
    }

    private fun setRandomImage(imgView: ImageView): Int {
        val randomNum = randomNumberGenerator(1, 6)

        val resourceId = resources.getIdentifier(
            "die_face_$randomNum",
            "drawable",
            "com.example.mobile_course_work"
        )
        imgView.setImageResource(resourceId)
        return randomNum
    }

    private fun setRandomImagesToComputer(imgBtnList: List<ImageView>) {
        for ((index, imgView) in imgBtnList.withIndex()) {
            var generateRandomNumber = setRandomImage(imgView)
            computerScore[index] = generateRandomNumber
        }
    }

    private fun setRandomImagesToYour(imgBtnList: List<ImageView>) {
        for ((index, imgView) in imgBtnList.withIndex()) {
            var generateRandomNumber = setRandomImage(imgView)
            userScore[index] = generateRandomNumber
        }
    }

    private fun setImagesToImageView(index: Int, imgView: ImageView) {
        var generateRandomNumber = setRandomImage(imgView)
        userScore[index] = generateRandomNumber
    }

    private fun setRandomImagesToBoth() {
        setRandomImagesToComputer(imgButtonComputer)
        setRandomImagesToYour(imgButtonYous)
    }

    private fun displayScore(txtViewCom: TextView, txtViewYou: TextView, winLosTxt: TextView) {
        val computerTotalScore = computerScore.sum()
        val userTotalScore = userScore.sum()

        lastScore[0] += computerTotalScore
        lastScore[1] += userTotalScore

        txtViewTotalComputer.text = "Computer - " + lastScore[0].toString()
        txtViewTotalYours.text = "You - " + lastScore[1].toString()

        if (isTheTargetIsExceeding() == 0) {
            Log.d("loser", "fales")
            displayWhoTheWinner(" You  Lose!!! ", false)
            numberOfLoses++
            noOfWinsOrNoOfLoses(winLosTxt)
        } else if (isTheTargetIsExceeding() == 1) {
//            displayWhoTheWinner(" Game  tie ", true)
        } else if (isTheTargetIsExceeding() == 2) {
            Log.d("win", "true")
            displayWhoTheWinner(" You  Win... ", true)
            numberOfWins++
            noOfWinsOrNoOfLoses(winLosTxt)
        } else if (isTheTargetIsExceeding() == -1) {

        }
        showVariableLogMessages()
    }

    private fun reRoll() {
        for ((index, selected) in usersSelection.withIndex()) {

            if (!selected) {
                setImagesToImageView(index, imgButtonYous[index])
            }
            Log.d("try", usersSelection.toString())
            Log.d("try", usersSelection.toString())

        }
    }

    /**
     * The computer player follows a random strategy and going to reRoll or not
     */
    private fun computerSelectRandomRolling(): Boolean {
        return Random.nextBoolean()
    }

    /**
     * Select random for random number dices to keep
     */

//    private fun computerSelectRandomDiceToKeep(): MutableList<Int> {
//
//        var selectRandomDiceToKeep = randomNumberGenerator(0,5)
//
//        var randomList = MutableList<Int>(size = selectRandomDiceToKeep) { 0 }
//
//        for (i in 0 <= until < selectRandomDiceToKeep){
//            randomList[i] = randomNumberGenerator(0,6)
//        }
//
//        return randomlist
//    }

    private fun toSetTarget(txtViewTarget: TextView) {
        lateinit var popup: PopupWindow
        val popupView = layoutInflater.inflate(R.layout.to_set_target, null)

        popup = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popup.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        val confirmButton = popupView.findViewById<Button>(R.id.button_save_score)
        confirmButton.setOnClickListener {
            val editText = popupView.findViewById<EditText>(R.id.edit_text_score)
            val input = editText.text.toString().toInt()
            // set the input to the target.
            target = input
            updateTheTarget(targetTxt)
            popup.dismiss()
        }

        val cancelButton = popupView.findViewById<Button>(R.id.button_cancel)
        cancelButton.setOnClickListener {
            popup.dismiss()
        }
    }

    private fun updateTheTarget(targetText: TextView) {
        targetText.text = "Target - $target ✒"
    }

    private fun isTheTargetIsExceeding(): Int {

        var computerScore: Int = lastScore[0]
        var youScore: Int = lastScore[1]

        if (computerScore > youScore && computerScore >= target) {
            return 0
        } else if (computerScore == youScore && computerScore >= target) {
            return 1
        } else if (youScore > computerScore && youScore >= target) {
            return 2
        }
        return -1
    }

    private fun displayWhoTheWinner(message: String, state: Boolean) {
        lateinit var popupWindow: PopupWindow
        val popView = layoutInflater.inflate(R.layout.winner_or_loser, null)
        popupWindow = PopupWindow(
            popView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0)

        val cancelButton = popView.findViewById<Button>(R.id.button_cancel_w_l)
        val playAgain = popView.findViewById<Button>(R.id.button_play_again)

        cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            popupWindow.dismiss()
        }

        playAgain.setOnClickListener {
            val intent = Intent(this, NewGame::class.java)
            startActivity(intent)
            finish()
            popupWindow.dismiss()
        }

        val theState = popView.findViewById<TextView>(R.id.textView_win)
        theState.text = message
        if (state) {
            theState.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            theState.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }

    private fun whenScoresAreTie(){
        Toast.makeText(this,"The game is tie", Toast.LENGTH_SHORT).show()
        while (lastScore[0] == lastScore[1]){
            setRandomImagesToBoth()
        }
    }

    private fun restoreDiceImages() {
        val imageViewList = ArrayList<ArrayList<ImageView>>()
        imageViewList.add(imgButtonComputer)
        imageViewList.add(imgButtonYous)

        for ((index, value) in computerScore.withIndex())
            imgButtonComputer[index].setImageResource(
                resources.getIdentifier(
                    "die_face_$value", "drawable", "com.example.mobile_course_work"
                )
            )

        for ((index, value) in userScore.withIndex())
            imgButtonYous[index].setImageResource(
                resources.getIdentifier(
                    "die_face_$value", "drawable", "com.example.mobile_course_work"
                )
            )
    }

    private fun restoreSelection() {
        val border: Drawable = ResourcesCompat.getDrawable(resources, R.drawable.the_border, null)!!
        for ((index, value) in usersSelection.withIndex()) {
            if (value) {
                imgButtonYous[index].background = border
            }
        }
    }

    private fun noOfWinsOrNoOfLoses(winLosTxt: TextView) {
        winLosTxt.text = "H - $numberOfWins  /  C - $numberOfLoses"
    }

    /**
     * this method will create a log of all data in the program
     */
    private fun showVariableLogMessages() {
        Log.d("++++++++++", "++++++++++++++")
        Log.d("lastScores", lastScore.toString())
        Log.d("computerScores", computerScore.toString())
        Log.d("userScores", userScore.toString())
        Log.d("target", target.toString())
        Log.d("attempts", attempts.toString())
        Log.d("imageViewSelected", usersSelection.toString())
        Log.d("++++++++++SCORE LOGS", "++++++++++++++")
        Log.d("computer scores", computerScore.sum().toString())
        Log.d("user scores", userScore.sum().toString())
    }
}

