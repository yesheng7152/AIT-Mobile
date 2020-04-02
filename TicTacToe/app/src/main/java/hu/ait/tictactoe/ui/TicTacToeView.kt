package hu.ait.tictactoe.ui

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import hu.ait.tictactoe.MainActivity
import hu.ait.tictactoe.R
import hu.ait.tictactoe.model.TicTacToeModel
import kotlinx.android.synthetic.main.activity_main.view.*

class TicTacToeView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    var paintBackground = Paint()
    var paintLine = Paint()
    var paintLine2 = Paint()

    var paintText = Paint()

    var bitmapBg = BitmapFactory.decodeResource(
        context?.resources,
        R.drawable.background   //background is the image variable name
    )

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color. WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintLine2.color = Color.BLUE
        paintLine2.style = Paint.Style.STROKE
        paintLine2.strokeWidth = 5f

        paintText.textSize = 60f
        paintText.color = Color.MAGENTA
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paintText.textSize = height / 3f

        bitmapBg = Bitmap.createScaledBitmap(bitmapBg, width, height, false)
        //image can be resized here from the width and height
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.drawRect(0f,0f, width.toFloat(), height.toFloat(), paintBackground)

        canvas?.drawBitmap(bitmapBg, 0f,0f,null)

        canvas?.drawText("1", 0f, height / 3f, paintText)


        drawBoard(canvas)
        drawPlayers(canvas)
    }

    private fun drawBoard(canvas: Canvas?) {
        // border
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // two horizontal lines
        canvas?.drawLine(
            0f, (height / 3).toFloat(), width.toFloat(), (height / 3).toFloat(),
            paintLine
        )
        canvas?.drawLine(
            0f, (2 * height / 3).toFloat(), width.toFloat(),
            (2 * height / 3).toFloat(), paintLine
        )

        // two vertical lines
        canvas?.drawLine(
            (width / 3).toFloat(), 0f, (width / 3).toFloat(), height.toFloat(),
            paintLine
        )
        canvas?.drawLine(
            (2 * width / 3).toFloat(), 0f, (2 * width / 3).toFloat(), height.toFloat(),
            paintLine
        )

    }

    private fun drawPlayers(canvas: Canvas?) {
        for (i in 0..2) {
            for (j in 0..2) {
                if (TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CIRCLE) {
                    val centerX = (i * width / 3 + width / 6).toFloat()
                    val centerY = (j * height / 3 + height / 6).toFloat()
                    val radius = height / 6 - 2

                    canvas?.drawCircle(centerX, centerY, radius.toFloat(), paintLine2)
                    //counterCIRCLE.start()
                    //counterCROSS.stop()

                } else if (TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CROSS) {
                    canvas?.drawLine((i * width / 3).toFloat(), (j * height / 3).toFloat(),
                        ((i + 1) * width / 3).toFloat(),
                        ((j + 1) * height / 3).toFloat(), paintLine)

                    canvas?.drawLine(((i + 1) * width / 3).toFloat(), (j * height / 3).toFloat(),
                        (i * width / 3).toFloat(), ((j + 1) * height / 3).toFloat(), paintLine)
                    //counterCROSS.start()
                    //counterCIRCLE.stop()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){ // ACTION_MOVE then can hold and move
            val tX = event.x.toInt()/(width/3)
            val tY = event.y.toInt()/(height/3)

            if (tX < 3 && tY < 3 && TicTacToeModel.getFieldContent(tX, tY) == TicTacToeModel.EMPTY) {
                TicTacToeModel.setFieldContext(tX, tY, TicTacToeModel.getNextPlayer())
                TicTacToeModel.changePlayer()
                invalidate() // redraws the view, by calling onDraw() eventually....

                var win = winner()
                if (win == 0) {
                    Toast.makeText((context as MainActivity), "Circle is the WINNER", Toast.LENGTH_LONG).show()
                    //restart()
                }else if (win == 1){
                    Toast.makeText((context as MainActivity), "Cross is the WINNER", Toast.LENGTH_LONG).show()
                    //restart()
                }

                var nextPlayerText = if (TicTacToeModel.getNextPlayer() == TicTacToeModel.CIRCLE)
                    "O" else "X"

                (context as MainActivity).showStatusMessage(
                    context.resources.getString(R.string.text_next_player, nextPlayerText)
                )


            }
        }

        return true
    }

    public fun winner():Int{
        if (checkForColumn()=="Circle" || checkForRow() == "Circle" || checkForDiagnal() == "Circle")
            return 0
        else if (checkForColumn()=="Cross" || checkForRow() == "Cross" || checkForDiagnal() == "Cross")
            return 1
        return 2
    }

    public fun checkForRow(): String{
        for (i in 0..2) {
            if(TicTacToeModel.getFieldContent(i,0)== TicTacToeModel.CIRCLE &&
                TicTacToeModel.getFieldContent(i,1)== TicTacToeModel.CIRCLE &&
                TicTacToeModel.getFieldContent(i,2) == TicTacToeModel.CIRCLE){
                return "Circle"
            }else if (TicTacToeModel.getFieldContent(i,0)== TicTacToeModel.CROSS&&
                    TicTacToeModel.getFieldContent(i,1)== TicTacToeModel.CROSS &&
                    TicTacToeModel.getFieldContent(i,2) == TicTacToeModel.CROSS) {
                return "Cross"
            }
        }
        return "Tie"
    }

    public fun checkForColumn():String{
        for (j in 0..2){
            if(TicTacToeModel.getFieldContent(0,j)== TicTacToeModel.CIRCLE &&
                TicTacToeModel.getFieldContent(1,j)== TicTacToeModel.CIRCLE &&
                TicTacToeModel.getFieldContent(2,j) == TicTacToeModel.CIRCLE){
                return "Circle"
            }else if (TicTacToeModel.getFieldContent(0,j)== TicTacToeModel.CROSS&&
                TicTacToeModel.getFieldContent(1,j)== TicTacToeModel.CROSS &&
                TicTacToeModel.getFieldContent(2,j) == TicTacToeModel.CROSS) {
                return "Cross"
            }
        }
        return "Tie"
    }

    public fun checkForDiagnal(): String{
        if ((TicTacToeModel.getFieldContent(0,0)== TicTacToeModel.CIRCLE &&
            TicTacToeModel.getFieldContent(1,1)== TicTacToeModel.CIRCLE &&
            TicTacToeModel.getFieldContent(2,2) == TicTacToeModel.CIRCLE) ||(
                    TicTacToeModel.getFieldContent(0,2)== TicTacToeModel.CIRCLE &&
                    TicTacToeModel.getFieldContent(1,1)== TicTacToeModel.CIRCLE &&
                    TicTacToeModel.getFieldContent(2,0) == TicTacToeModel.CIRCLE)){
            return "Circle"
        }else if ((TicTacToeModel.getFieldContent(0,0)== TicTacToeModel.CROSS &&
                    TicTacToeModel.getFieldContent(1,1)== TicTacToeModel.CROSS &&
                    TicTacToeModel.getFieldContent(2,2) == TicTacToeModel.CROSS) ||(
                    TicTacToeModel.getFieldContent(0,2)== TicTacToeModel.CROSS &&
                            TicTacToeModel.getFieldContent(1,1)== TicTacToeModel.CROSS &&
                            TicTacToeModel.getFieldContent(2,0) == TicTacToeModel.CROSS)){
            return "Cross"
        }
        return "Tie"
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }



    public fun restart(){
        TicTacToeModel.resetModel()
        // counterCROSS.base
        //counterCIRCLE.base
        invalidate()
    }


}