package cn.leo.sudoku

import android.view.View

class TestKotlin {
    var onClickListener: (View) -> Unit = { }


    fun test() {
        val method = method { a, b -> a * b }
    }

    fun method(action: ((a: Int, b: Int) -> Int)): Int {
        return action(2, 3)
    }
}