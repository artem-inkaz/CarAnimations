package ui.smartpro.caranimations

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import kotlinx.coroutines.*
import ui.smartpro.caranimations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val carsPrepareAnimatorSet = AnimatorSet()
    var width: Int = 0
    var height: Int = 0
    var random1: Int = 0
    var random2: Int = 0
    var tempX1: Int = 0
    var tempY1: Int = 0
    var tempX2: Int = 0
    var tempY2: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenHeight()
    }

    override fun onResume() {
        super.onResume()
        countDownTimer()
    }

    private fun countDownTimer() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {
                binding.push.text = "До старта осталось:${p0 / 1000} сек"
            }

            override fun onFinish() {
                binding.push.text = "Приготовились!"
                startAnimations()
            }
        }.start()
    }

    private fun startAnimations() {

        val car1PrepareAnimator = AnimatorInflater.loadAnimator(this, R.animator.prepare_car1)
        car1PrepareAnimator.setTarget(binding.car1)

        val car2PrepareAnimator = AnimatorInflater.loadAnimator(this, R.animator.prepare_car2)
        car2PrepareAnimator.setTarget(binding.car2)

        carsPrepareAnimatorSet.playSequentially(car1PrepareAnimator, car2PrepareAnimator)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        carsPrepareAnimatorSet.addListener(onEnd = {
            binding.push.text = "Поехали!"
            binding.push.visibility = View.INVISIBLE
            scope.launch {
                launch {
                    repeat(5) {
                        Log.d("Animations", "getRandom")
                        getRandom()
                        Log.d("Animations", "Animation1")
                        Animation1()
                        Log.d("Animations", "Animation2")
                        Animation2()
                        Log.d("Animations", "Animation3")
                        Animation3()
                        delay(5000)
                    }
                    binding.push.text = "Финиш!"
                    binding.push.visibility = View.VISIBLE
                }

            }
        })

        carsPrepareAnimatorSet.start()
    }

    private suspend fun Animation1() {
        startFirstCarAnimation()
        delay(1000)

    }

    private suspend fun Animation2() {
        rotater()
        delay(300)

    }

    private suspend fun Animation3() {
        startSecondCarAnimation()
        delay(2000)

    }

    private suspend fun getRandom() {
        getCoordinatesCar1()
        getCoordinatesCar2()
        delay(300)
    }

    private fun startFirstCarAnimation() {
        tempX1 = (random1 + 50)
        tempY1 = (random1 + 250)
        if (tempX1 > height) tempX1 = height - 20
        if (tempY1 > width) tempY1 = width - 20

        binding.car1.animate()
            .x(tempX1.toFloat())
            .y(tempY1.toFloat())
            .setDuration(4000)
            .start()

        binding.car1.animate()
            .alpha(1f)
            .setStartDelay(4000)
            .setDuration(1000)
            .start()

        rotater()
    }

    private fun startSecondCarAnimation() {
        tempX2 = (random2 / 2)
        tempY2 = (random2 + 200)

        if (tempX2 > height) tempX2 = height - 20
        if (tempY2 > width) tempY2 = width - 20

        binding.car2.animate()
            .x(tempX2.toFloat())
            .y(tempY2.toFloat())
            .setDuration(4000)
            .start()

        binding.car2.animate()
            .alpha(1f)
            .setStartDelay(4000)
            .setDuration(1000)
            .start()

        rotater()
    }

    private fun screenHeight() {
        width = this.resources.displayMetrics.widthPixels
        height = this.resources.displayMetrics.heightPixels

//        val displayMetrics = DisplayMetrics()
//        var width = displayMetrics.widthPixels
//        var height = displayMetrics.heightPixels
    }

    private fun rotater() {
        if (binding.car1.absX() >= random1 && binding.car1.absY() >= random1)
            binding.car1.animate().rotation(-90f)
        else binding.car1.animate().rotation(90f)
        if (binding.car2.absX() >= random2 && binding.car2.absY() >= random2)
            binding.car2.animate().rotation(-90f)
        else binding.car2.animate().rotation(90f)
    }

    fun getCoordinatesCar1(): Int {
        random1 = (10..1000).random()
        Log.d("Animations", "Random=$random1")
        return random1
    }

    fun getCoordinatesCar2(): Int {
        random2 = (10..1000).random()
        Log.d("Animations", "Random=$random2")
        return random2
    }

    fun View.absX(): Int {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[0]
    }

    fun View.absY(): Int {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[1]
    }
}