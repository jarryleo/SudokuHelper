package cn.leo.sudoku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cn.leo.sudoku.adapter.SudokuGridAdapter
import cn.leo.sudoku.core.SudokuChecker
import cn.leo.sudoku.core.SudokuEval
import cn.leo.sudoku.core.SudokuFactory
import cn.leo.sudoku.view.GridLine
import cn.leo.sudoku.view.SolvingDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.switch_item.view.*

class MainActivity : AppCompatActivity() {
    private val sudokuGridAdapter = SudokuGridAdapter()
    private var dialog: SolvingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initButton()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu?.findItem(R.id.item_switch)?.actionView?.sSwitch?.setOnCheckedChangeListener { _, isChecked -> setFlag(isChecked) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_input -> {
                sudokuGridAdapter.tobeTitle()
            }
            R.id.item_reload -> {
                sudokuGridAdapter.reload()
            }
            R.id.item_clear -> {
                sudokuGridAdapter.allClear()
            }
            R.id.item_solve -> {
                computerSolve()
            }
            R.id.item_level0 -> {
                createTitle(SudokuFactory.ENTRY_LEVEL)
            }
            R.id.item_level1 -> {
                createTitle(SudokuFactory.BEGINNER)
            }
            R.id.item_level2 -> {
                createTitle(SudokuFactory.INTERMEDIATE)
            }
            R.id.item_level3 -> {
                createTitle(SudokuFactory.ADVANCED)
            }
            R.id.item_level4 -> {
                createTitle(SudokuFactory.HARDCORE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        rvSudokuGrid.layoutManager = GridLayoutManager(this, 9)
        rvSudokuGrid.adapter = sudokuGridAdapter
        rvSudokuGrid.addItemDecoration(GridLine())
        dialog = SolvingDialog(this)
    }

    private fun initButton() {
        val list = listOf(btnNum0,
                btnNum1, btnNum2, btnNum3,
                btnNum4, btnNum5, btnNum6,
                btnNum7, btnNum8, btnNum9)
        setClick(list)
    }

    //生成题目
    private fun createTitle(@SudokuFactory.Level level: Int) {
        val sudokuFactory = SudokuFactory.create(level)
        sudokuGridAdapter.setTitle(sudokuFactory.title)
    }

    //显示候选数
    private fun setFlag(flag: Boolean) {
        sudokuGridAdapter.setShowFlag(flag)
    }

    //点击事件
    private fun setClick(views: List<View>) {
        views.forEachIndexed { i, view ->
            view.setOnClickListener {
                when (view) {
                    views[i] -> {
                        sudokuGridAdapter.inputNum(i)
                    }
                }
            }
        }
    }

    //电脑解题
    private fun computerSolve() {


        val title = sudokuGridAdapter.getInputMap()
        val check = SudokuChecker.check(title)
        if (!check) {
            Toast.makeText(this,
                    "题目有误，请检查！",
                    Toast.LENGTH_SHORT).show()
            return
        }
        dialog?.show()
        if (sudokuGridAdapter.getTitle() == null)
            sudokuGridAdapter.tobeTitle()
        Thread {
            val sudokuEval = SudokuEval()
            val map = sudokuEval.input(title).solution().map
            runOnUiThread {
                sudokuGridAdapter.showAnswer(map)
                dialog?.dismiss()
                Toast.makeText(this,
                        "解题完毕！用时${sudokuEval.solutionTime}毫秒",
                        Toast.LENGTH_LONG).show()
            }
        }.start()
    }
}
