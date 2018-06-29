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
    private var mShowFlag = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuCellHolder {
        return SudokuCellHolder(SudokuCell(parent.context), this)
    }

    override fun getItemCount(): Int {
        return 81
    }

    fun getSelectPosition() = mSelectPosition

    fun getTitle() = mTitle

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

    /**
     * 候选数开关
     */
    fun setShowFlag(show: Boolean) {
        if (show == mShowFlag) return
        mShowFlag = show
        if (mShowFlag) {
            showFlag()
        } else {
            hideFlag()
        }
    }

    /**
     * 显示题目
     */
    fun setTitle(title: Array<ByteArray>) {
        mTitle = title
        load(title)
    }

    /**
     * 装载题目
     */
    private fun load(title: Array<ByteArray>?) {
        if (title == null) return
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                val bean = mList[i * 9 + j]
                bean.num = byte.toInt()
                bean.mode = if (byte > 0) SudokuCell.MODE_TITLE else SudokuCell.MODE_INPUT
            }
        }
        notifyDataSetChanged()
        if (mShowFlag) showFlag()
    }

    /**
     *重做
     */
    fun reload() {
        load(mTitle)
    }


    /**
     * 显示候选数
     */
    private fun showFlag() {
        val title = getInputMap()
        title.forEachIndexed { i, bytes ->
            bytes.forEachIndexed { j, byte ->
                if (byte.toInt() == 0) {
                    val cell = mList[i * 9 + j]
                    cell.mode = SudokuCell.MODE_FLAG
                    cell.flag = SudokuChecker.findCellCanInputNum(title, i, j) as ArrayList<Int>
                    notifyItemChanged(i * 9 + j)
                }
            }
        }
    }

    /**
     * 隐藏候选数
     */
    private fun hideFlag() {
        mList.forEachIndexed { index, sudokuCellBean ->
            if ((sudokuCellBean.mode and SudokuCell.MODE_FLAG) == SudokuCell.MODE_FLAG) {
                sudokuCellBean.mode = SudokuCell.MODE_INPUT
                notifyItemChanged(index)
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
        clearError()
        val repeatCell = SudokuChecker.getRepeatCell(getInputMap(), mSelectPosition / 9, mSelectPosition % 9)
        if (repeatCell.size > 0) {
            mList[mSelectPosition].mode = mList[mSelectPosition].mode or SudokuCell.MODE_ERROR
            repeatCell.forEach {
                val i = it.x * 9 + it.y
                mList[i].mode = mList[i].mode or SudokuCell.MODE_ERROR
                notifyItemChanged(i)
            }
        }
        notifyItemChanged(mSelectPosition)
        if (mShowFlag) showFlag()
    }

    /**
     * 清空错误提示
     */
    private fun clearError() {
        mList.forEachIndexed { index, cell ->
            if (cell.mode and SudokuCell.MODE_ERROR == SudokuCell.MODE_ERROR) {
                cell.mode = cell.mode xor SudokuCell.MODE_ERROR
                notifyItemChanged(index)
            }
        }
    }

    /**
     * mList转二维数组
     */
    fun getInputMap(): Array<ByteArray> {
        val title: Array<ByteArray> = Array(9) { ByteArray(9) }
        for (i in 0..8) {
            for (j in 0..8) {
                val cell = mList[i * 9 + j]
                title[i][j] = cell.num.toByte()
            }
        }
        return title
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
     * 输入变题目
     */
    fun tobeTitle() {
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
        mTitle = null
        mSelectPosition = -1
        notifyDataSetChanged()
    }

}