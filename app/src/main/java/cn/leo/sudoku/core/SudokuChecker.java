package cn.leo.sudoku.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数独格式检查员，检查数独格式是否符合标准：
 * 每行每列每个九宫数字不重复
 */
public class SudokuChecker {
    /**
     * 检查数独字符串
     *
     * @param s 数独字符串
     * @return 是否符合规则
     */
    public static boolean check(String s) {
        if (s.length() != 81) return false;
        byte[] b = new byte[81];
        char[] chars = s.toCharArray();
        for (int i = 0; i < 81; i++) {
            b[i] = (byte) (chars[i] - 48);
        }
        return check(b);
    }

    /**
     * 检查数独数字一维数组
     *
     * @param s 数独数字数组
     * @return 是否符合规则
     */
    public static boolean check(byte[] s) {
        if (s.length != 81) return false;
        byte[][] b = new byte[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(s, i * 9, b[i], 0, 9);
        }
        return check(b);
    }

    /**
     * 检查数独数字二维数组
     *
     * @param s 数独数字数组
     * @return 是否符合规则
     */
    public static boolean check(byte[][] s) {
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(s[i]));
            int sum1 = 1 << s[i][0];
            int sum2 = 1 << s[0][i];
            int sum3 = 1 << s[(i / 3) * 3][(i % 3) * 3];
            int b;
            for (int j = 1; j < 9; j++) {
                //每横
                if (s[i][j] > 0) {
                    b = 1 << s[i][j];
                    if ((sum1 & b) == b) return false;
                    sum1 += b;
                }
                //每竖
                if (s[j][i] > 0) {
                    b = 1 << s[j][i];
                    if ((sum2 & b) == b) return false;
                    sum2 += b;
                }
                //每九宫
                byte b1 = s[(i / 3) * 3 + (j / 3)][(i % 3) * 3 + (j % 3)];
                if (b1 > 0) {
                    b = 1 << b1;
                    if ((sum3 & b) == b) return false;
                    sum3 += b;
                }
            }
        }
        return true;
    }

    /**
     * 找出指定位置x,y的数字冲突的位置
     *
     * @param map 数独二维数组
     * @param x   指定位置x
     * @param y   指定位置y
     * @return 找到的冲突位置集合
     */
    public static List<Cell> getRepeatCell(byte[][] map, int x, int y) {
        List<Cell> list = new ArrayList<>();
        if (map[x][y] == 0) return list;
        for (int i = 0; i < 9; i++) {
            if (map[x][i] == map[x][y] && i != y) {
                list.add(new Cell(x, i));
            }
            if (map[i][y] == map[x][y] && i != x) {
                list.add(new Cell(i, y));
            }
            int x1 = (x / 3) * 3 + (i / 3);
            int y1 = (y / 3) * 3 + (i % 3);
            byte b = map[x1][y1];
            if (b == map[x][y] && (x1 != x || y1 != y)) {
                list.add(new Cell(x1, y1));
            }
        }
        return list;
    }

    /**
     * 检查目标位置数字是否有冲突
     *
     * @param map 数独数组
     * @param x   位置x
     * @param y   位置y
     * @param num 位置x,y的值
     * @return false 表示有冲突
     */
    public static boolean checkCell(byte[][] map, int x, int y, int num) {
        if (num == 0) return true;
        for (int i = 0; i < 9; i++) {
            if (map[x][i] == num && i != y) {
                return false;
            }
            if (map[i][y] == num && i != x) {
                return false;
            }
            int x1 = (x / 3) * 3 + (i / 3);
            int y1 = (y / 3) * 3 + (i % 3);
            byte b = map[x1][y1];
            if (b == num && (x1 != x || y1 != y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找格子x,y的所有可填数
     *
     * @param map 数独数组
     * @param x   位置x
     * @param y   位置y
     * @return 范围位置可填数字集合
     */
    public static List<Integer> findCellCanInputNum(byte[][] map, int x, int y) {
        List<Integer> nums = new ArrayList<>();
        if (map[x][y] > 0) return nums;
        for (int i = 1; i <= 9; i++) {
            if (checkCell(map, x, y, i)) {
                nums.add(i);
            }
        }
        return nums;
    }

    /**
     * 获取已填数个数
     *
     * @param map 数独二维数组
     * @return 已填数个数
     */
    public static int getInputCount(byte[][] map) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 数独二维数组转字符串
     *
     * @param map 数独二维数组
     * @return 转换后的字符串
     */
    public static String mapToString(byte[][] map) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(map[i][j]);
            }
        }
        return sb.toString();
    }

    /**
     * 打印数独二维数组
     *
     * @param map 要打印的二维数组
     */
    public static void showSudokuMap(byte[][] map) {
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(map[i]));
        }
    }

    public static class Cell {
        public int x, y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Cell() {
        }
    }
}
