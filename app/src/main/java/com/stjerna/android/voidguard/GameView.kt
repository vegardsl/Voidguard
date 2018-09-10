package com.stjerna.android.voidguard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast

class GameView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val cellPaint = Paint()
    private var shipHeading = 0f

    private val shipRect = RectF(width / 2f - 3f, height / 2f - 10f,
            width / 2f + 3f, height / 2f + 10f)
    lateinit var battlefield: Battlefield

    init {
        cellPaint.color = Color.BLACK
        shipRect.offsetTo(width / 2f, height / 2f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.rotate(-shipHeading, shipRect.centerX(), shipRect.centerY())
        canvas.drawRect(
                shipRect,
                cellPaint)
        canvas.restore()

        for (o in battlefield.objects) {
            val rect = RectF(width / 2f, height / 2f - 1f,
                    width / 2f + 1f, height / 2f + 1f)
            rect.offsetTo(o.pos.x + width / 2f, -o.pos.y + height / 2f)

            if (rect.intersect(shipRect)) Log.d("GameView", "HIT")
            //Toast.makeText(context, "HIT", Toast.LENGTH_SHORT).show()

            canvas.drawRect(rect, cellPaint)
        }

//        canvas.drawRect(
//                width /2f + ship.xPos.toFloat(), height /2f - 10f + ship.yPos.toFloat(),
//                width /2f + 10f + ship.xPos.toFloat(), height /2f + 10f + ship.yPos.toFloat(),
//                cellPaint)
    }

    fun update(ship: Ship) {
        this.shipHeading = ship.heading.toFloat()

        shipRect.offsetTo(ship.xPos.toFloat() + width / 2f, -ship.yPos.toFloat() + height / 2f)

        invalidate()
    }

}