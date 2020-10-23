package com.pleon.donim.node

import com.pleon.donim.APP_BASE_COLOR
import com.pleon.donim.Animatable
import com.pleon.donim.Animatable.AnimationDirection.FORWARD
import com.pleon.donim.Animatable.AnimationProperties
import com.pleon.donim.Timer
import com.pleon.donim.div
import javafx.beans.InvalidationListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.util.Duration
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CircularProgressBar : Animatable, Canvas() {

    // https://www.youtube.com/watch?v=V2yXGdC98jM
    // https://stackoverflow.com/q/24533556/8583692

    private lateinit var timer: Timer
    private lateinit var animationProperties: AnimationProperties
    private val sliceLength = 4             // in degrees
    private val sliceGap = sliceLength / 2  // in degrees
    private val arcStart = 90               // in degrees
    private var arcEnd = arcStart - 360     // in degrees
    private var color = APP_BASE_COLOR
    private var outerRadius = 0.0
    private var innerRadius = 0.0
    var title: String = ""

    init {
        val listener = InvalidationListener {
            outerRadius = min(width, height) / 2
            innerRadius = outerRadius * 0.66
            draw()
        }
        widthProperty().addListener(listener)
        heightProperty().addListener(listener)
    }

    override fun isResizable() = true

    private fun draw() {
        graphicsContext2D.clearRect(0.0, 0.0, width, height)
        drawBackgroundBar()
        var sliceStart = arcStart
        while (sliceStart - sliceGap >= arcEnd) {
            drawSlice(sliceStart, color)
            sliceStart -= (sliceLength + sliceGap)
        }
    }

    private fun drawBackgroundBar() {
        graphicsContext2D.lineWidth = outerRadius - innerRadius
        graphicsContext2D.stroke = Color.gray(0.5, 0.2)
        graphicsContext2D.beginPath()
        graphicsContext2D.arc(width / 2, height / 2, (innerRadius + outerRadius) / 2,
                (innerRadius + outerRadius) / 2, 0.0, 360.0)
        graphicsContext2D.closePath()
        graphicsContext2D.stroke()
    }

    private fun drawSlice(startAngle: Int, color: Color) {
        fillLinearGradient(startAngle, color)
        graphicsContext2D.beginPath()
        graphicsContext2D.arc(width / 2, height / 2, outerRadius, outerRadius,
                startAngle.toDouble(), -sliceLength.toDouble())
        graphicsContext2D.arc(width / 2, height / 2, innerRadius, innerRadius,
                (startAngle - sliceLength).toDouble(), sliceLength.toDouble())
        graphicsContext2D.closePath()
        graphicsContext2D.fill()
    }

    private fun fillLinearGradient(startAngle: Int, color: Color) {
        val angle = startAngle + (sliceLength / 2.0)

        fun Double.toRadian() = Math.toRadians(this)
        val startX = width / 2 + cos(angle.toRadian()) * innerRadius
        val startY = height / 2 + sin(angle.toRadian()) * -innerRadius
        val endX = width / 2 + cos(angle.toRadian()) * outerRadius
        val endY = height / 2 + sin(angle.toRadian()) * -outerRadius

        val startColor = Stop(0.0, color)
        val endColor = Stop(1.0, color.darker())

        graphicsContext2D.fill = LinearGradient(startX, startY, endX, endY,
                false, CycleMethod.NO_CYCLE, startColor, endColor)
    }

    private fun tick(elapsedTime: Duration) {
        val progress = animationProperties.initialProgress + (elapsedTime / animationProperties.duration)
        val hueShift = progress * (animationProperties.endColor.hue - animationProperties.startColor.hue)
        color = animationProperties.startColor.deriveColor(hueShift, 1.0, 1.0, 1.0)
        arcEnd = if (animationProperties.direction == FORWARD) (arcStart - progress * 360).toInt() else (arcStart - (1 - progress) * 360).toInt()
        draw()
    }

    private fun createTimer(onEnd: () -> Unit = {}) {
        timer = Timer(animationProperties.duration, Duration.millis(30.0), onEnd)
        timer.elapsedTimeProperty().addListener { _, _, elapsedTime -> tick(elapsedTime) }
    }

    override fun startAnimation(properties: AnimationProperties) {
        resetAnimation(properties)
        timer.start()
    }

    override fun startAnimation() {
        if (!this::timer.isInitialized) createTimer()
        timer.start()
    }

    override fun pauseAnimation() {
        timer.stop()
        color = animationProperties.pauseColor
        draw()
    }

    override fun resetAnimation(properties: AnimationProperties) {
        animationProperties = properties
        // TODO: Also remove listeners from timer properties to avoid memory leak
        if (this::timer.isInitialized) timer.stop()
        createTimer()
    }

    override fun endAnimation(onEnd: () -> Unit, graceful: Boolean, graceDuration: Duration) {
        val wasRunning = timer.isRunning
        timer.stop()
        val progress = timer.elapsedTimeProperty().get() / animationProperties.duration
        animationProperties = AnimationProperties(
                graceDuration,
                animationProperties.direction,
                if (wasRunning) animationProperties.startColor else APP_BASE_COLOR,
                if (wasRunning) animationProperties.endColor else APP_BASE_COLOR,
                initialProgress = progress)
        createTimer(onEnd)
        timer.start()
    }
}
