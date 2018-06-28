package cn.leo.sudoku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.leo.sudoku.adapter.SudokuGridAdapter
import cn.leo.sudoku.core.SudokuFactory
import cn.leo.sudoku.view.GridLine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val sudokuGridAdapter = SudokuGridAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        //createTitle()
    }

    private fun initView() {
        rvSudokuGrid.layoutManager = GridLayoutManager(this, 9)
        rvSudokuGrid.adapter = sudokuGridAdapter
        rvSudokuGrid.addItemDecoration(GridLine())
        setClick(btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9)

    }

    private fun createTitle() {
        val sudokuFactory = SudokuFactory.create(SudokuFactory.BEGINNER)
        sudokuGridAdapter.setTitle(sudokuFactory.title)
        sudokuGridAdapter.showFlag()
    }

    private fun setClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener {
                when (it) {
                    btnNum1 -> {
                        sudokuGridAdapter.inputNum(1)
                    }
                    btnNum2 -> {
                        sudokuGridAdapter.inputNum(2)
                    }
                    btnNum3 -> {
                        sudokuGridAdapter.inputNum(3)
                    }
                    btnNum4 -> {
                        sudokuGridAdapter.inputNum(4)
                    }
                    btnNum5 -> {
                        sudokuGridAdapter.inputNum(5)
                    }
                    btnNum6 -> {
                        sudokuGridAdapter.inputNum(6)
                    }
                    btnNum7 -> {
                        sudokuGridAdapter.inputNum(7)
                    }
                    btnNum8 -> {
                        sudokuGridAdapter.inputNum(8)
                    }
                    btnNum9 -> {
                        sudokuGridAdapter.inputNum(9)
                    }
                    else -> {
                    }
                }
            }
        }
    }
}
