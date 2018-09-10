package com.stjerna.android.voidguard

import android.app.Activity
import android.graphics.PointF
import android.util.Log
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

class GameController(val context: Activity, val ship: Ship, val battlefield: Battlefield, val gameView: GameView) {

    private val DELAY = 0
    private var periodMillis = 50
    private var timer = Timer()

    private var isStarted = false

    fun startGame() {
        if (isStarted) return
        isStarted = true
        startGameLoop()
    }

    private fun startGameLoop() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                //logStepDuration();
                ship.nextPos()
                battlefield.update()
                context.runOnUiThread { gameView.update(ship) }
            }
        }, DELAY.toLong(), periodMillis.toLong())
    }
}

class PDCRound(val posDot: PointF, val pos: PointF)

class Battlefield {
    val objects = CopyOnWriteArrayList<PDCRound>()

    fun update() {
        for (o in objects) {
            o.pos.x += o.posDot.x
            o.pos.y += o.posDot.y
        }
    }
}

class Ship(private val battlefield: Battlefield) : ThrusterControllerListener, FireControls {

    var fwThrusterOn = false
    var breakingThrusterOn = false
    var xPos = 0.0
    var yPos = 0.0

    var xVel = 0.0
    var yVel = 0.0

    var yawLeft = false
    var yawRight = false

    var heading = 0.0
    var headingRate = 0.0

    private var pdcBurstCounter = 0

    override fun engageFwThruster() {
        fwThrusterOn = true
    }

    override fun disengageFwThruster() {
        fwThrusterOn = false
    }

    override fun engageAftThruster() {
        breakingThrusterOn = true
    }

    override fun disengageAftThruster() {
        breakingThrusterOn = false
    }

    fun nextPos() {
        if (yawLeft) headingRate += 0.2
        if (yawRight) headingRate -= 0.2
        heading += headingRate

        if (fwThrusterOn) {
            val headingRad = heading * ((2.0 * Math.PI) / 360.0)
            xVel += 0.2 * (Math.sin(headingRad))
            yVel += -0.2 * (Math.cos(headingRad))
        }

        if (breakingThrusterOn) {
            val headingRad = heading * ((2.0 * Math.PI) / 360.0)
            xVel -= 0.1 * (Math.sin(headingRad))
            yVel -= -0.1 * (Math.cos(headingRad))
        }

        xPos += xVel
        yPos += yVel

        Log.d("SHIP", "Heading: $heading, velocity: ${Math.sqrt(xVel * xVel + yVel * yVel)} ")
        Log.d("SHIP", "x velocity: $xVel, y velocity: $yVel)} ")

        if (pdcBurstCounter > 0) {
            val headingRad = heading * ((2.0 * Math.PI) / 360.0)
            battlefield.objects.add(
                    PDCRound(
                            PointF(
                                    xVel.toFloat() + 4 * Math.sin(headingRad + Math.PI / 4.0).toFloat(),
                                    yVel.toFloat() - 4 * Math.cos(headingRad + Math.PI / 4.0).toFloat()),
                            PointF(xPos.toFloat(), yPos.toFloat()))
            )

            battlefield.objects.add(
                    PDCRound(
                            PointF(
                                    xVel.toFloat() + 4 * Math.sin(headingRad - Math.PI / 4.0).toFloat(),
                                    yVel.toFloat() - 4 * Math.cos(headingRad - Math.PI / 4.0).toFloat()),
                            PointF(xPos.toFloat(), yPos.toFloat()))
            )
            pdcBurstCounter--
        }
    }

    override fun engageYawLeft() {
        yawLeft = true
    }

    override fun disengageYawLeft() {
        yawLeft = false
    }

    override fun engageYawRight() {
        yawRight = true
    }

    override fun disengageYawRight() {
        yawRight = false
    }

    override fun firePDC() {
        pdcBurstCounter = 15
    }

    override fun fireTorpedoes() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}