package cn.leo.sudoku.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import cn.leo.sudoku.bean.SudokuCellBean
import cn.leo.sudoku.core.SudokuChecker
import cn.leo.sudoku.holder.SudokuCellHolder
import cn.leo.sudoku.view.SudokuCell

class SudokuGridAdapter : RecyclerView.Adapter<SudokuCellHolder>() {
    var mList: ArrayList<SudokuCellBean> = ArrayList()
    var mTitle: Array<ByteArray>? = null
    var mSelectPostion = -1
    var mLastSelectPostion = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuCellHolder {
        return SudokuCellHolder(SudokuCell(parent.context), this)
    }

    override fun getItemCount(): Int {
        return 81
    }

    override fun onBindViewHolder(holder: SudokuCellHolder, position: Int) {
        if (mList.size != 81) initList()
        val bean = mList[position]
        holder.setData(bean)
    }

    fun setSelectPosition(position: Int) {
        if (mList[position].mode and SudokuCell.MODE_TITLE == SudokuCell.MODE_TITLE) return
        mLastSelectPostion = mSelectPostion
        mSelectPostion = position
        notifyItemChanged(mSelectPostion)
        notifyItemChanged(mLastSelectPostion)
    }

    fun getSelectPosition() = mSelectPostion

    /**
     * 显示题目
     */
    fun setTitle(title: Array<ByteArray>) {
        mTitle = title
        mList.clear()
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                val element = SudokuCellBean(byte.toInt())
                element.mode = if (byte > 0) SudokuCell.MODE_TITLE else SudokuCell.MODE_INPUT
                mList.add(i * 9 + j, element)
            }
        }
    }

    /**
     * 显示候选数
     */
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

    fun inputNum(num: Int) {
        if (mSelectPostion < 0) return
        if (mList.size == 0) initList()
        mList[mSelectPostion].num = num
        mList[mSelectPostion].mode = SudokuCell.MODE_INPUT
        notifyItemChanged(mSelectPostion)
    }

    private fun initList() {
        for (i in 0..81) {
            val element = SudokuCellBean(0)
            element.mode = SudokuCell.MODE_INPUT
            mList.add(element)
        }
    }

}