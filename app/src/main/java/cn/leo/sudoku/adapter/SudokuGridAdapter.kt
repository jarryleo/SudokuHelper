package cn.leo.sudoku.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import cn.leo.sudoku.bean.SudokuCellBean
import cn.leo.sudoku.core.SudokuChecker
import cn.leo.sudoku.holder.SudokuCellHolder
import cn.leo.sudoku.view.SudokuCell

class SudokuGridAdapter() : RecyclerView.Adapter<SudokuCellHolder>() {
    var mList: ArrayList<SudokuCellBean> = ArrayList()
    var mTitle: Array<ByteArray>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuCellHolder {
        return SudokuCellHolder(SudokuCell(parent.context))
    }

    override fun getItemCount(): Int {
        return 81
    }

    override fun onBindViewHolder(holder: SudokuCellHolder, position: Int) {
        if (mList.size != 81) return
        val bean = mList[position]
        holder.setData(bean)
    }

    fun setTitle(title: Array<ByteArray>) {
        mTitle = title
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                val element = SudokuCellBean(byte.toInt())
                element.mode = if (byte > 0) SudokuCell.MODE_TITLE else SudokuCell.MODE_INPUT
                mList.add(i * 9 + j, element)
            }
        }
    }

    fun showFlag() {
        if (mTitle == null) return
        mTitle!!.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                if (byte.toInt() == 0) {
                    val cell = mList[i * 9 + j]
                    cell.mode = SudokuCell.MODE_FLAG
                    cell.flag = SudokuChecker.findCellCanInputNum(mTitle, i, j) as ArrayList<Int>
                }
            }
        }
    }
}