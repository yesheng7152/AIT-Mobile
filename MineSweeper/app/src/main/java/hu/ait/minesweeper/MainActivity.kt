package hu.ait.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import hu.ait.minesweeper.model.MineSweeperModel
import hu.ait.minesweeper.ui.MineSweeperView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Boolean

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tbFlag.setOnClickListener{
            MineSweeperModel.switchMode()
        }

        btnStart.setOnClickListener {
            mineView.restart()
        }

        btnRestart.setOnClickListener {
            mineView.restart()
        }
    }
    private fun MineSweeperView.restart() {
        MineSweeperModel.resetModel()
        gameOver = Boolean.FALSE
        flagNumber = 0
        correctFlagged = 0

        cmTimer.base = SystemClock.elapsedRealtime()
        cmTimer.start()
        invalidate()
    }
}
