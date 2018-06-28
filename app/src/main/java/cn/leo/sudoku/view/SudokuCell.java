package cn.leo.sudoku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SudokuCell extends View {
    //数独格子模式
    public static final int MODE_TITLE = 1; //题目格子 浅灰背景，表示不可输入，点击不可选中
    public static final int MODE_INPUT = 1 << 1; //输入格子 默认背景，空白或者显示已填数字
    public static final int MODE_FLAG = 1 << 2;  //标记格子 默认背景，9个小数字，显示可填数字
    public static final int MODE_ERROR = 1 << 3; //错误格子 重复数字，数字红色
    public static final int MODE_FOCUS = 1 << 4; //焦点格子 点击后，准备输入，背景绿色
    //当前模式
    private int mCurrentMode = MODE_INPUT;
    //显示数字
    private int mShowNum;
    //标记可填数字
    private List<Integer> mFlag = new ArrayList<>();
    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文字画笔
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    @IntDef(flag = true, value = {MODE_TITLE, MODE_INPUT, MODE_FLAG, MODE_ERROR, MODE_FOCUS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {

    }

    public SudokuCell(Context context) {
        this(context, null);
    }

    public SudokuCell(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SudokuCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.BLACK);
    }

    public int getShowNum() {
        return mShowNum;
    }

    public void setNum(int showNum) {
        mShowNum = showNum;
        invalidate();
    }

    public List<Integer> getFlag() {
        return mFlag;
    }

    public void setFlag(List<Integer> flag) {
        mFlag.clear();
        mFlag.addAll(flag);
        invalidate();
    }

    public void setMode(@Mode int mode) {
        mCurrentMode = mode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //让高等于宽
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String s = String.valueOf(mShowNum);
        int w = getWidth();
        int h = getHeight();
        //显示候选数
        if ((mCurrentMode & MODE_FLAG) == MODE_FLAG) {
            w /= 3;
            h /= 3;
            mTextPaint.setTextSize(h);
            mTextPaint.setColor(Color.LTGRAY);
            Rect rect = new Rect();
            mTextPaint.getTextBounds(s, 0, 1, rect);
            for (int i = 1; i <= 9; i++) {
                if (mFlag.contains(i)) {
                    int x = w * ((i - 1) % 3) + w / 2;
                    int y = h * ((i - 1) / 3) + h;
                    canvas.drawText(String.valueOf(i), x, y - (h - rect.height()) / 2, mTextPaint);
                }
            }
            setBackgroundColor(Color.WHITE);
            return;
        }
        if (mShowNum == 0) {
            setBackgroundColor(Color.WHITE);
            return;
        }
        mTextPaint.setTextSize(h);
        mTextPaint.setColor(Color.BLACK);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(s, 0, 1, rect);
        canvas.drawText(s, w / 2, h - (h - rect.height()) / 2, mTextPaint);
        //显示题目数字
        if ((mCurrentMode & MODE_TITLE) == MODE_TITLE) {
            setBackgroundColor(Color.LTGRAY);
        }
        //显示可输入数字
        if ((mCurrentMode & MODE_INPUT) == MODE_INPUT) {
            setBackgroundColor(Color.WHITE);
        }
        //显示错误
        if ((mCurrentMode & MODE_ERROR) == MODE_ERROR) {
            setBackgroundColor(Color.RED);
        }
        //显示焦点
        if ((mCurrentMode & MODE_FOCUS) == MODE_FOCUS) {
            setBackgroundColor(Color.GREEN);
        }
    }
}
