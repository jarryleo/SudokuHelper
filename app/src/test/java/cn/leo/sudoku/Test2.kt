package cn.leo.sudoku

import android.view.View

 class Test2 {
    fun test() {
        val testKotlin = TestKotlin()
        testKotlin.onClickListener = { it?.visibility = View.GONE }
    }
}