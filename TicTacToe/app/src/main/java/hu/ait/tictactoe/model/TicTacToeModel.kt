package hu.ait.tictactoe.model

import javax.crypto.Cipher

object TicTacToeModel {
    public var EMPTY: Short =0
    public var CROSS: Short =1
    public var CIRCLE: Short =2

    private val model = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY)
    )

    private var nextPlayer = CIRCLE

    fun resetModel(){
        for (i in 0..2){
            for (j in 0..2){
                model[i][j] = EMPTY
            }
        }
        nextPlayer = CIRCLE
    }
    fun getFieldContent(x:Int, y:Int) = model[x][y]

    fun setFieldContext(x:Int, y:Int, player:Short){
        model[x][y] = player

    }

    fun getNextPlayer() = nextPlayer

    fun changePlayer() {
        nextPlayer= if (nextPlayer == CIRCLE) CROSS else CIRCLE
    }
}