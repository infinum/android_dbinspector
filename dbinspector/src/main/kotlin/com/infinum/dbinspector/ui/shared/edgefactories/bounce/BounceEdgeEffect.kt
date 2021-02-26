package com.infinum.dbinspector.ui.shared.edgefactories.bounce

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

internal class BounceEdgeEffect(
    private val recyclerView: RecyclerView,
    private val direction: Int
) : EdgeEffect(recyclerView.context) {

    companion object {
        private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

        private const val FLING_TRANSLATION_MAGNITUDE = 0.5f
    }

    private var translationAnim: SpringAnimation? = null

    override fun onPull(deltaDistance: Float) {
        super.onPull(deltaDistance)

        doOnPull(deltaDistance)
    }

    override fun onPull(deltaDistance: Float, displacement: Float) {
        super.onPull(deltaDistance, displacement)

        doOnPull(deltaDistance)
    }

    override fun onRelease() {
        super.onRelease()

        if (recyclerView.translationY != 0f) {
            translationAnim = createSpringAnimation().also { it.start() }
        }
    }

    override fun onAbsorb(velocity: Int) {
        super.onAbsorb(velocity)

        val sign = if (direction == RecyclerView.EdgeEffectFactory.DIRECTION_BOTTOM) -1 else 1
        val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
        translationAnim?.cancel()
        translationAnim = createSpringAnimation()
            .setStartVelocity(translationVelocity)
            .also { it.start() }
    }

    override fun draw(canvas: Canvas?): Boolean = false

    override fun isFinished(): Boolean =
        translationAnim?.isRunning?.not() ?: true

    private fun doOnPull(deltaDistance: Float) {
        val sign = if (direction == RecyclerView.EdgeEffectFactory.DIRECTION_BOTTOM) -1 else 1
        val translationYDelta = sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
        recyclerView.translationY += translationYDelta

        translationAnim?.cancel()
    }

    private fun createSpringAnimation(): SpringAnimation =
        SpringAnimation(recyclerView, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0.0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
}
