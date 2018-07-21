package cn.leo.sudoku.ocr;

import android.graphics.Rect;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析图片识别的数独题目
 * create by : Jarry Leo
 * date : 2018/7/19 13:37
 */
public class Ocr2String {
    private String mResult;

    public static Ocr2String create(String boxString, int bitmapWidth, int bitmapHeight) {
        return new Ocr2String(boxString, bitmapWidth, bitmapHeight);
    }

    private Ocr2String(String boxString, int bitmapWidth, int bitmapHeight) {
        if (TextUtils.isEmpty(boxString)) return;
        List<BoxChar> boxCharList = new ArrayList<>();
        String[] box = boxString.split("\n");
        for (String b : box) {
            BoxChar boxChar = new BoxChar(b, bitmapHeight);
            boxCharList.add(boxChar);
        }
        StringBuilder sb = new StringBuilder();
        Rect rect = new Rect();
        int cw = bitmapWidth / 9;
        int ch = bitmapHeight / 9;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                rect.set(j * cw, i * ch, j * cw + cw, i * ch + ch);
                boolean flag = false;
                for (BoxChar boxChar : boxCharList) {
                    if (rect.contains(boxChar.getRect())) {
                        sb.append(boxChar.getWord());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    sb.append("0");
                }
            }
        }
        mResult = sb.toString();
    }

    public String getResult() {
        return mResult;
    }

    public static class BoxChar {
        private String mWord;
        private Rect mRect = new Rect();

        BoxChar(String box, int bitmapHeight) {
            String[] s = box.split(" ");
            mWord = s[0];
            int left = Integer.parseInt(s[1]);
            int top = bitmapHeight - Integer.parseInt(s[4]);
            int right = Integer.parseInt(s[3]);
            int bottom = bitmapHeight - Integer.parseInt(s[2]);
            mRect.set(left, top, right, bottom);
        }

        public String getWord() {
            return mWord;
        }

        public Rect getRect() {
            return mRect;
        }
    }

}
