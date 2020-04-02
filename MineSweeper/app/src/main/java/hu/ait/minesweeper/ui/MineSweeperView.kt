package hu.ait.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import hu.ait.minesweeper.MainActivity
import hu.ait.minesweeper.R
import hu.ait.minesweeper.model.MineSweeperModel
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Boolean.FALSE

class MineSweeperView (context: Context,attributeSet: AttributeSet): View(context, attributeSet){

    var paintBackground = Paint()
    var paintLine = Paint()
    var paintText = Paint()

    var gameOver = FALSE
    var flagNumber = 0
    var correctFlagged = 0

    var bombImage = BitmapFactory.decodeResource(
        context?.resources,
        R.drawable.bomb)
    var flagImage = BitmapFactory.decodeResource(
        context?.resources,
        R.drawable.flag
    )


    init{
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f
        paintText.textSize = 45f
        paintText.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        drawBoard(canvas)
        displayContent(canvas)
        invalidate()

        if (gameOver == java.lang.Boolean.TRUE) {
            showBomb(canvas)

        }
    }

    private fun drawBoard(canvas: Canvas?){
        canvas?.drawRect(0f,0f,width.toFloat(), height.toFloat(), paintLine)

        canvas?.drawLine(0f, (height/5).toFloat(), width.toFloat(), (height/5).toFloat(),paintLine)

        canvas?.drawLine(
            0f, (2*height/5).toFloat(), width.toFloat(),(2*height/5).toFloat(), paintLine)
        canvas?.drawLine(
            0f, (3*height/5).toFloat(), width.toFloat(),(3*height/5).toFloat(), paintLine)
        canvas?.drawLine(
            0f, (4*height/5).toFloat(), width.toFloat(),(4*height/5).toFloat(), paintLine)

        canvas?.drawLine((width / 5).toFloat(), 0f, (width / 5).toFloat(), height.toFloat(), paintLine)
        canvas?.drawLine((2*width / 5).toFloat(), 0f, (2*width / 5).toFloat(), height.toFloat(), paintLine)
        canvas?.drawLine((3*width / 5).toFloat(), 0f, (3*width / 5).toFloat(), height.toFloat(), paintLine)
        canvas?.drawLine((4 * width / 5).toFloat(), 0f, (4 * width / 5).toFloat(), height.toFloat(), paintLine)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paintText.textSize = height/5f

        bombImage = Bitmap.createScaledBitmap(bombImage,
            width/5, height/5, false)

        flagImage = Bitmap.createScaledBitmap(flagImage,
            width/5, height/5, false)
    }

    private fun displayBomb(canvas: Canvas?, x: Int,y: Int){
        canvas?.drawBitmap(bombImage,
            (x * width / 5).toFloat(),
            (y * height / 5).toFloat(),
            null)
    }

    private fun showBomb(canvas: Canvas?){
        for (i in 0..4){
            for (j in 0..4){
                if (MineSweeperModel.getBaseContent(i, j) == MineSweeperModel.BOMB){
                    displayBomb(canvas, i,j)
                }
            }
        }
    }

    private fun displayFlag(canvas: Canvas?, x:Int, y:Int){
        canvas?.drawBitmap(flagImage,
            (x * width / 5).toFloat(),
            (y * height  / 5).toFloat(),
            null)
    }

    private fun displayContent(canvas: Canvas?){
        for(i in 0..4){
            for (j in 0..4) {
                if (MineSweeperModel.getFlagContent(i, j) == MineSweeperModel.FLAG) {
                    displayFlag(canvas, i, j)
                    invalidate()
                } else if (MineSweeperModel.getBaseContent(i, j) == MineSweeperModel.TRY) {
                    numberOfMines(canvas, i, j)
                    invalidate()
                }
            }
        }
    }

    private fun emptyField(x: Int, y: Int) { //rename to placeTry
        if (x < 5 && y < 5 && MineSweeperModel.getBaseContent(x, y) == MineSweeperModel.EMPTY
            && (MineSweeperModel.getCurrentMode() == MineSweeperModel.TRY)) {
            MineSweeperModel.setBaseContent(x, y, MineSweeperModel.TRY)
        }
    }

    private fun placeFlag(x:Int, y:Int){
        if (x < 5 && y < 5){
            if (MineSweeperModel.getBaseContent(x,y) == MineSweeperModel.EMPTY
                && MineSweeperModel.getCurrentMode() == MineSweeperModel.FLAG){
                if (MineSweeperModel.getFlagContent(x,y) == MineSweeperModel.FLAG){
                    flagNumber -=1
                    if (flagNumber == 3){
                        var win = determineWinner()
                        if (win == 1){
                            invalidate()
                            Toast.makeText(
                                (context as MainActivity),
                                "YOU WON", Toast.LENGTH_LONG).show()
                        }
                    }
                }else if (MineSweeperModel.getFlagContent(x,y) == MineSweeperModel.EMPTY){
                    flagGame(x,y)
                }
            }
            if (MineSweeperModel.getFlagContent(x,y) == MineSweeperModel.EMPTY &&
                (MineSweeperModel.getCurrentMode() == MineSweeperModel.FLAG)){
                MineSweeperModel.setFlagContent(x,y,MineSweeperModel.FLAG)
                invalidate()
            }else if (MineSweeperModel.getFlagContent(x,y) == MineSweeperModel.FLAG
                && MineSweeperModel.getCurrentMode() == MineSweeperModel.FLAG){
                MineSweeperModel.setFlagContent(x,y,MineSweeperModel.EMPTY)
                invalidate()
            }
        }
    }


    private fun flagGame(x: Int, y: Int){
        flagNumber += 1
        if(flagNumber == 3){
            var win = determineWinner()
            if (win == 1){
                invalidate()
                Toast.makeText((context as MainActivity), "YOU WON", Toast.LENGTH_LONG).show()
            }
        }
        MineSweeperModel.setFlagContent(x,y,MineSweeperModel.EMPTY)
    }

    private fun mineField (x : Int, y: Int) {
        if ((x < 5 && y < 5 && MineSweeperModel.getBaseContent(x, y) == MineSweeperModel.BOMB)) {
            if (MineSweeperModel.getCurrentMode() == MineSweeperModel.TRY) {
                Toast.makeText((context as MainActivity), "GAME OVER", Toast.LENGTH_LONG).show()
                gameOver = java.lang.Boolean.TRUE
            } else if (MineSweeperModel.getCurrentMode() == MineSweeperModel.FLAG) {
                if (MineSweeperModel.getFlagContent(x, y) == MineSweeperModel.FLAG) {
                    flagNumber += 1

                    if (flagNumber == 3) {

                        var winner = determineWinner()
                        if (winner == 1) {
                            invalidate()
                            Toast.makeText((context as MainActivity), "GAME WON!", Toast.LENGTH_LONG).show()
                        }
                    }
                    MineSweeperModel.setFlagContent(x, y, MineSweeperModel.FLAG)

                } else if (MineSweeperModel.getFlagContent(x, y) == MineSweeperModel.EMPTY) {
                    flagNumber -= 1
                    if (flagNumber == 3) {
                        var winner = determineWinner()
                        if (winner == 1) {
                            invalidate()
                            Toast.makeText((context as MainActivity), "GAME WON!", Toast.LENGTH_LONG).show()
                        }
                    }
                    MineSweeperModel.setFlagContent(x, y, MineSweeperModel.EMPTY)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            emptyField(tX, tY)

            placeFlag(tX, tY)

            mineField(tX, tY)
        }
        return true
    }

    private fun numberOfMines(canvas: Canvas?, x: Int, y: Int){
        if (MineSweeperModel.getBaseContent(x,y) != MineSweeperModel.BOMB){
            displayBombNumber(canvas, x, y, adjacentNum(x,y))
        }
    }

    private fun adjacentNum(x: Int, y: Int):Int{
        var count =0;
        for(i in x - 1 .. x + 1){
            for(j in y - 1 .. y + 1){
                if (i>=0 && i<5 && j>=0 && j<5){
                    if(MineSweeperModel.getBaseContent(i,j) == MineSweeperModel.BOMB)
                        count++
                }
            }
        }
        return count
    }

    private fun displayBombNumber(canvas: Canvas?, x: Int, y: Int, bombCount: Int){
        canvas?.drawText(
            bombCount.toString(),
            (x * width / 5 + width / 18).toFloat(),
            (y * height / 5 +height / 6).toFloat(),
            paintText
        )
    }

    private fun determineWinner(): Int{
        correctFlagged = 0
        for(i in 0..4){
            for (j in 0..4){
                if (MineSweeperModel.getFlagContent(i,j) ==MineSweeperModel.FLAG &&
                        MineSweeperModel.getBaseContent(i,j) == MineSweeperModel.BOMB){
                    correctFlagged++
                }
            }
        }
        if(correctFlagged == 3 && flagNumber ==3){
            return 1
        }
        return 0
    }
}
