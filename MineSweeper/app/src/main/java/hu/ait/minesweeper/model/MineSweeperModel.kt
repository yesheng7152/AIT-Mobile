package hu.ait.minesweeper.model

object MineSweeperModel {
    var EMPTY: Short = 0
    var TRY: Short = 1
    var FLAG: Short = 2
    var BOMB: Short = 3

    private var basemodel = arrayOf(
        shortArrayOf(EMPTY, BOMB, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, BOMB, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, BOMB, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, BOMB)
    )

    private var model = basemodel
    private var mode = TRY

    private var flagModel = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)
    )

    fun resetModel(){
        for (i in 0..4) {
            for (j in 0..4) {
                if (model[i][j] == BOMB) {
                    flagModel[i][j] = EMPTY
                    continue
                } else {
                    model[i][j] = EMPTY
                    flagModel[i][j] = EMPTY
                }
            }
        }

    mode = TRY
    model = basemodel.toMutableList().apply{shuffle()}.toTypedArray()
}

    fun getFlagContent(x:Int, y:Int): Short {
        return flagModel[x][y]
    }

    fun setFlagContent(x:Int, y:Int, content: Short){
        flagModel[x][y] = content
    }

    fun getBaseContent(x:Int, y:Int): Short{
        return basemodel[x][y]
    }

    fun setBaseContent(x:Int, y:Int, content: Short){
        basemodel[x][y]=content
    }

    fun getCurrentMode() = mode

    fun switchMode(){
        if(mode == FLAG)
            mode = TRY
        else
            mode = FLAG
    }
}