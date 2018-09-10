package com.stjerna.android.voidguard

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class GameControllerView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    companion object {
        val TAG: String = GameControllerView::class.java.simpleName
    }

    var thrusterControllerListener: ThrusterControllerListener? = null
    var fireControlListener: FireControls? = null
    lateinit var battlefield: Battlefield

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.game_controller_view, this)
        initFwButton(view)
        initAftButton(view)
        initYawLeftButton(view)
        initYawRightButton(view)
        initPDCButton(view)
    }

    private fun initFwButton(view: View) {
        val fwButton = view.findViewById<Button>(R.id.fw_button)
        fwButton.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            Log.d(TAG, "MotionEvent: ${motionEvent.action}")
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> thrusterControllerListener?.engageFwThruster()
                MotionEvent.ACTION_UP -> thrusterControllerListener?.disengageFwThruster()
            }
            return@setOnTouchListener false
        }
    }

    private fun initAftButton(view: View) {
        val fwButton = view.findViewById<Button>(R.id.aft_button)
        fwButton.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            Log.d(TAG, "MotionEvent: ${motionEvent.action}")
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> thrusterControllerListener?.engageAftThruster()
                MotionEvent.ACTION_UP -> thrusterControllerListener?.disengageAftThruster()
            }
            return@setOnTouchListener false
        }
    }

    private fun initYawLeftButton(view: View) {
        val fwButton = view.findViewById<Button>(R.id.left_button)
        fwButton.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            Log.d(TAG, "MotionEvent: ${motionEvent.action}")
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> thrusterControllerListener?.engageYawLeft()
                MotionEvent.ACTION_UP -> thrusterControllerListener?.disengageYawLeft()
            }
            return@setOnTouchListener false
        }
    }

    private fun initYawRightButton(view: View) {
        val fwButton = view.findViewById<Button>(R.id.right_button)
        fwButton.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            Log.d(TAG, "MotionEvent: ${motionEvent.action}")
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> thrusterControllerListener?.engageYawRight()
                MotionEvent.ACTION_UP -> thrusterControllerListener?.disengageYawRight()
            }
            return@setOnTouchListener false
        }
    }

    private fun initPDCButton(view: View) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        view.findViewById<Button>(R.id.pdc_button).setOnClickListener {
            if (!vibrator.hasVibrator()) return@setOnClickListener
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000L, 255))
            } else {
                vibrator.vibrate(1000L)
            }
            fireControlListener?.firePDC()
        }
    }
}

interface ThrusterControllerListener {
    fun engageFwThruster()
    fun disengageFwThruster()
    fun engageAftThruster()
    fun disengageAftThruster()
    fun engageYawLeft()
    fun disengageYawLeft()
    fun engageYawRight()
    fun disengageYawRight()
}

interface FireControls {
    fun firePDC()
    fun fireTorpedoes()
}