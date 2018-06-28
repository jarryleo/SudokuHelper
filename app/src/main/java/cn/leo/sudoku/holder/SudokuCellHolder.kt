package cn.leo.sudoku.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import cn.leo.sudoku.adapter.SudokuGridAdapter
import cn.leo.sudoku.bean.SudokuCellBean
import cn.leo.sudoku.view.SudokuCell

class SudokuCellHolder(itemView: View, adapter: SudokuGridAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var mAdapter = adapter

    fun setData(bean: SudokuCellBean) {
        val cell = itemView as SudokuCell
        cell.setNum(bean.num)
        cell.setMode(bean.mode)
        cell.flag = bean.flag
        cell.setOnClickListener(this)
        if (adapterPosition == mAdapter.getSelectPosition()) {
            cell.setMode(bean.mode or SudokuCell.MODE_FOCUS)
        }
    }

    override fun onClick(v: View?) {
        mAdapter.setSelectPosition(adapterPosition)
    }
}