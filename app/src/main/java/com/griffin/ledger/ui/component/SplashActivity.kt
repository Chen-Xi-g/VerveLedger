package com.griffin.ledger.ui.component

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.griffin.core.utils.mmkv.BaseMV
import com.griffin.core.utils.start
import com.griffin.feature.login.component.LoginActivity
import com.griffin.ledger.ui.component.guide.GuideActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页
 */
class SplashActivity : AppCompatActivity() {

    companion object{
        /**
         * 启动页等待时间
         */
        const val SPLASH_WAIT_TIME = 1000L
    }

    private var isSplashScreenRemoved = true

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isSplashScreenRemoved
            }
            setOnExitAnimationListener { screen ->
                // 获取屏幕高度
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels.toFloat()

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.TRANSLATION_Y,
                    0f,
                    screenHeight / 5f,
                    -screenHeight / 1.5f
                )
                zoomY.interpolator = AccelerateInterpolator()
                zoomY.duration = 500
                zoomY.doOnEnd {
                    screen.remove()
                    startMain()
                }

                zoomY.start()
            }
        }

        lifecycleScope.launch {
            delay(SPLASH_WAIT_TIME)
            isSplashScreenRemoved = false
        }
    }

    private fun startMain(){
        if (BaseMV.System.isFirst) {
            start(GuideActivity::class.java)
            return
        }
        if (BaseMV.User.token.isNullOrBlank()){
            start(LoginActivity::class.java)
        }else{
            start(MainActivity::class.java)
        }
    }
}