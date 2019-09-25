package com.pleon.donim.util

import com.pleon.donim.util.AnimationUtil.FadeMode.IN
import com.pleon.donim.util.AnimationUtil.MoveDirection.BOTTOM
import com.pleon.donim.util.AnimationUtil.MoveDirection.BOTTOM_RIGHT
import javafx.animation.*
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.stage.Window
import javafx.util.Duration

object AnimationUtil {

    enum class MoveDirection {
        NONE, BOTTOM, BOTTOM_RIGHT
    }

    enum class FadeMode {
        IN, OUT
    }

    fun fade(fadeMode: FadeMode, node: Node, delay: Duration, duration: Duration,
             onFinished: EventHandler<ActionEvent>? = null) {
        val fade = FadeTransition(duration, node)
        fade.delay = delay
        fade.fromValue = if (fadeMode == IN) 0.0 else 1.0
        fade.toValue = if (fadeMode == IN) 1.0 else 0.0
        fade.onFinished = onFinished
        fade.play()
    }

    fun move(moveDirection: MoveDirection, window: Window,
             delay: Duration, duration: Duration,
             onFinished: EventHandler<ActionEvent>? = null) {

        val step = 0.01
        val timeline = Timeline()
        val frameDuration = duration.multiply(step)
        timeline.keyFrames.add(KeyFrame(frameDuration, EventHandler<ActionEvent> {
            if (moveDirection == BOTTOM) {
                window.y += step.times(15)
            } else if (moveDirection == BOTTOM_RIGHT) {
                window.y += step.times(50)
                window.x += step.times(50)
            }
        }))
        timeline.delay = delay
        timeline.cycleCount = (1 / step).toInt()
        timeline.onFinished = onFinished
        timeline.play()
    }

    fun rotate(node: Node, byAngle: Double, durationMillis: Int, delayMillis: Int) {
        val rotate = RotateTransition(Duration.millis(durationMillis.toDouble()), node)
        rotate.interpolator = Interpolator.EASE_BOTH
        rotate.delay = Duration.millis(delayMillis.toDouble())
        rotate.byAngle = byAngle
        rotate.play()
    }
}
