package cn.leo.sudoku.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import cn.leo.sudoku.bean.SudokuCellBean
import cn.leo.sudoku.core.SudokuChecker
import cn.leo.sudoku.holder.SudokuCellHolder
import cn.leo.sudoku.view.SudokuCell

class SudokuGridAdapter : RecyclerView.Adapter<SudokuCellHolder>() {
    private var mList: ArrayList<SudokuCellBean> = ArrayList()
    private var mTitle: Array<ByteArray>? = null
    private var mSelectPosition = -1
    private var mLastSelectPosition = -1

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
        mLastSelectPosition = mSelectPosition
        mSelectPosition = position
        notifyItemChanged(mSelectPosition)
        notifyItemChanged(mLastSelectPosition)
    }

    fun getSelectPosition() = mSelectPosition

    /**
     * 显示题目
     */
    fun setTitle(title: Array<ByteArray>) {
        mTitle = title
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                val bean = mList[i * 9 + j]
                bean.num = byte.toInt()
                bean.mode = if (byte > 0) SudokuCell.MODE_TITLE else SudokuCell.MODE_INPUT
            }
        }
        notifyDataSetChanged()
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

    /**
     * 输入数字
     */
    fun inputNum(num: Int) {
        if (mSelectPosition < 0) return
        if (mList.size == 0) initList()
        mList[mSelectPosition].num = num
        mList[mSelectPosition].mode = SudokuCell.MODE_INPUT
        notifyItemChanged(mSelectPosition)
    }

    /**
     * 初始化集合
     */
    private fun initList() {
        for (i in 0..80) {
            val element = SudokuCellBean(0)
            element.mode = SudokuCell.MODE_INPUT
            mList.add(element)
        }
    }

    /**
     * 自动解题,输入变题目
     */
    fun autoSolve() {
        val title: Array<ByteArray> = Array(9) { ByteArray(9) }
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = mList[i * 9 + j]
                title[i][j] = cell.num.toByte()
                if (cell.num > 0) {
                    cell.mode = SudokuCell.MODE_TITLE
                    notifyItemChanged(i * 9 + j)
                }
            }
        }
        mSelectPosition = -1
        mTitle = title
    }

    fun getTitle() = mTitle

    /**
     * 显示答案
     */
    fun showAnswer(title: Array<ByteArray>) {
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                val bean = mList[i * 9 + j]
                if (bean.num == 0) {
                    bean.num = byte.toInt()
                    bean.mode = SudokuCell.MODE_INPUT
                    notifyItemChanged(i * 9 + j)
                }
            }
        }
    }

    /**
     * 全部清除
     */
    fun allClear() {
        for (i in 0..80) {
            val bean = mList[i]
            bean.mode = SudokuCell.MODE_INPUT
            bean.num = 0
        }
        mSelectPosition = -1
        notifyDataSetChanged()
    }

}