package cn.leo.sudoku

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var a = method(3, 2, ::plus)
        var b = method(3, 2, ::sub)
        var c = method(3, 2, ::multiply)
        println("$a,$b,$c")
    }

    fun plus(a: Int, b: Int) = a + b
    fun sub(a: Int, b: Int) = a - b
    fun multiply(a: Int, b: Int) = a * b

    fun method(x: Int, y: Int, z: (a: Int, b: Int) -> Int): Int {
        return z(x, y)
    }


}
