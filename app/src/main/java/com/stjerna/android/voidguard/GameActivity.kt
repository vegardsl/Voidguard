package com.stjerna.android.voidguard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {

    private lateinit var gameController: GameController
    private lateinit var gameControllerView: GameControllerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val battlefield = Battlefield()
        val ship = Ship(battlefield)
        gameControllerView = findViewById(R.id.game_controller_view)
        gameControllerView.thrusterControllerListener = ship
        gameControllerView.fireControlListener = ship
        val gameView = findViewById<GameView>(R.id.game_view)
        gameView.battlefield = battlefield
        gameController = GameController(this, ship, battlefield, gameView)
    }

    override fun onResume() {
        super.onResume()
        gameController.startGame()
    }
}
