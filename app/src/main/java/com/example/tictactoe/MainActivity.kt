package com.example.tictactoe

import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.tictactoe.R
import android.os.Bundle
import android.widget.Toast
import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.content.ComponentName
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null
    var gameActive = true

    // Player representation
    // 0 - X
    // 1 - o
    var activePlayer = 0
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var status: TextView? = null

    // Game state Meanings
    // 0 - X
    // 1 - o
    // 2 - null
    var winPosition = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    fun playerTap(view: View) {
        status = findViewById(R.id.status)
        val img = view as ImageView
        val tappedImage = img.tag.toString().toInt()
        if (!gameActive) {
            gameReset()
            return
        }
        if (gameState[tappedImage] == 2 && gameActive) {
            if (gameState[tappedImage] == 2) {
                gameState[tappedImage] = activePlayer
                img.translationY = -1000f
                if (activePlayer == 0) {
                    img.setImageResource(R.drawable.x)
                    activePlayer = 1
                    status.setText("O's Turn - Tap to Play")
                } else {
                    img.setImageResource(R.drawable.o)
                    activePlayer = 0
                    status.setText("X's Turn - Tap to Play")
                }
                img.animate().translationYBy(1000f).duration = 300
            }
            // check if anyone player has Won ?
            for (winPosition in winPosition) {
                // winning condition
                if (gameState[winPosition[0]] != 2 && gameState[winPosition[1]] != 2 && gameState[winPosition[2]] != 2) {
                    if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]]) {
                        // find out who ? X or O
                        gameActive = false
                        var winnerStr: String
                        if (gameState[winPosition[0]] == 0) {
                            winnerStr = "X has Won !"
                            status.setText(winnerStr)
                        } else {
                            winnerStr = "O has Won !"
                            status.setText(winnerStr)
                        }
                    }
                }
            }
            var emptySquare = false
            for (squareState in gameState) {
                if (squareState == 2) {
                    emptySquare = true
                    break
                }
            }
            if (!emptySquare && gameActive) {
                gameActive = false
                val winnerStr: String
                winnerStr = "It's a Draw !"
                status.setText(winnerStr)
            }
        }
    }

    fun gameReset() {
        gameActive = true
        activePlayer = 0
        for (i in gameState.indices) {
            gameState[i] = 2
        }
        (findViewById<View>(R.id.imageView1) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView2) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView3) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView4) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView5) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView6) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView7) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView8) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView9) as ImageView).setImageResource(0)
        status!!.text = "X's Turn - Tap to Play"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Welcome ! 🤗", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer.create(this, R.raw.guitar)
        mediaPlayer.setLooping(true)
        mediaPlayer.start()
    }

    override fun onPause() {
        if (this.isFinishing) { //basically BACK was pressed from this activity
            mediaPlayer!!.stop()
            Toast.makeText(this@MainActivity, "YOU PRESSED BACK", Toast.LENGTH_SHORT).show()
        }
        val context = applicationContext
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)
        if (!taskInfo.isEmpty()) {
            val topActivity = taskInfo[0].topActivity
            if (topActivity!!.packageName != context.packageName) {
                mediaPlayer!!.stop()
                Toast.makeText(this@MainActivity, "YOU LEFT YOUR APP", Toast.LENGTH_SHORT).show()
            } else {
                mediaPlayer!!.stop()
                Toast.makeText(
                    this@MainActivity,
                    "YOU SWITCHED ACTIVITIES WITHIN YOUR APP",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onPause()
    }
}