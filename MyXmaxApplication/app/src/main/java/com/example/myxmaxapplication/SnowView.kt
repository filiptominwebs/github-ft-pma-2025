package com.example.myxmaxapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SnowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply { color = 0x88FFFFFF.toInt() }
    private var particles = mutableListOf<Particle>()
    private var running = false

    private data class Particle(var x: Float, var y: Float, var speed: Float, var size: Float)

    init {
        // inicializace částic
        for (i in 0 until 5) {
            particles.add(randomParticle())
        }
    }

    private fun randomParticle(): Particle {
        val x = Random.nextFloat() * (width.coerceAtLeast(1))
        val y = Random.nextFloat() * (height.coerceAtLeast(1))
        val speed = 1f + Random.nextFloat() * 3f
        val size = 8f + Random.nextFloat() * 12f
        return Particle(x, y, speed, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        particles.clear()
        for (i in 0 until 20) particles.add(Particle(Random.nextFloat() * w, Random.nextFloat() * h, 1f + Random.nextFloat() * 3f, 4f + Random.nextFloat() * 8f))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in particles) {
            canvas.drawCircle(p.x, p.y, p.size, paint)
        }
        if (running) {
            updateParticles()
            postInvalidateOnAnimation()
        }
    }

    private fun updateParticles() {
        for (p in particles) {
            p.y += p.speed
            p.x += (Random.nextFloat() - 0.5f) * 1.5f
            if (p.y > height) {
                p.y = -p.size
                p.x = Random.nextFloat() * width
            }
        }
    }

    fun start() {
        if (!running) {
            running = true
            postInvalidateOnAnimation()
            visibility = VISIBLE
        }
    }

    fun stop() {
        running = false
        visibility = GONE
    }

    fun setSnowEnabled(enabled: Boolean) {
        if (enabled) start() else stop()
    }
}

