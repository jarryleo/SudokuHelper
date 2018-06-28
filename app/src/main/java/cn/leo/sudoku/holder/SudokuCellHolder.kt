package cn.leo.sudoku.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import cn.leo.sudoku.bean.SudokuCellBean
import cn.leo.sudoku.view.SudokuCell

class SudokuCellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(bean: SudokuCellBean) {
        val cell = itemView as SudokuCell
        cell.setNum(bean.num)
        cell.setMode(bean.mode)
        cell.flag = bean.flag
    }
}