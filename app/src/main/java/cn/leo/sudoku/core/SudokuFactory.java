package cn.leo.sudoku.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 数独题目工厂
 */
public class SudokuFactory {
    //Entry-level, Beginner, Intermediate, Advanced, Hardcore
    //入门，初级，中级，高级，骨灰级
    public static final int ENTRY_LEVEL = 0;
    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int ADVANCED = 3;
    public static final int HARDCORE = 4;
    private byte[][] map = new byte[9][9];
    private byte[][] title = new byte[9][9];

    //@IntDef({ENTRY_LEVEL, BEGINNER, INTERMEDIATE, ADVANCED, HARDCORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
    }

    private SudokuFactory(int level) {
        if (level < 0 || level > 4) {
            throw new IllegalArgumentException("题目等级参数错误");
        }
        //先生成终盘
        clearMap();
        boolean create = createMap();
        while (!create) {
            clearMap();
            create = createMap();
        }
        //再抽取已知数
        getRandomNum(level);
    }

    /**
     * 获取答案
     *
     * @return 答案
     */
    public byte[][] getAnswer() {
        return map;
    }

    /**
     * 获取题目
     *
     * @return 题目
     */
    public byte[][] getTitle() {
        return title;
    }

    /**
     * 生成数独终盘
     *
     * @return 是否生成成功
     */
    private boolean createMap() {
        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] > 0) continue;
                List<Integer> inputNum = SudokuChecker.findCellCanInputNum(map, i, j);
                if (inputNum.size() == 0) return false;
                int n = r.nextInt(inputNum.size());
                map[i][j] = inputNum.get(n).byteValue();
            }
        }
        return true;
    }

    /**
     * 根据等级获取数独题目
     *
     * @param level 等级 {@link Level} 对应 入门(36) 初级(32) 中级(28) 高级(24) 骨灰级(20) +-3
     * @return 返回 本对象
     */
    public static SudokuFactory create(@Level int level) {
        return new SudokuFactory(level);
    }

    /**
     * 抽取随机数字作为题目
     *
     * @param level 等级{@link Level}
     */
    private void getRandomNum(int level) {
        int[] levelList = {36, 32, 28, 24, 20};
        int i = levelList[level];
        Random r = new Random();
        int ran = r.nextInt(7);
        int count = i - 3 + ran;
        for (int j = 0; j < count; j++) {
            int x = r.nextInt(9);
            int y = r.nextInt(9);
            while (title[x][y] > 0) {
                x = r.nextInt(9);
                y = r.nextInt(9);
            }
            title[x][y] = map[x][y];
        }

    }

    /**
     * 清空数独二维数组
     */
    private void clearMap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                map[i][j] = 0;
            }
        }
    }


}
