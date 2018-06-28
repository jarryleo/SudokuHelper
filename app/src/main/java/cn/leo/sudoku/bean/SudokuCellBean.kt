package cn.leo.sudoku.bean

import cn.leo.sudoku.view.SudokuCell

data class SudokuCellBean(var num: Int) {
    var flag: ArrayList<Int> = ArrayList()
    var mode: Int = SudokuCell.MODE_INPUT
}