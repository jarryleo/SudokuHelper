package cn.leo.sudoku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
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
        createTitle()
    }

    private fun initView() {
        rvSudokuGrid.layoutManager = GridLayoutManager(this, 9)
        rvSudokuGrid.adapter = sudokuGridAdapter
        rvSudokuGrid.addItemDecoration(GridLine())

    }

    private fun createTitle() {
        val sudokuFactory = SudokuFactory.create(SudokuFactory.BEGINNER)
        sudokuGridAdapter.setTitle(sudokuFactory.title)
        sudokuGridAdapter.showFlag()
    }
}
