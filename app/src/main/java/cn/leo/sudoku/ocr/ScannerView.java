package cn.leo.sudoku.ocr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * create by : Jarry Leo
 * date : 2018/7/20 10:54
 */
public class ScannerView extends View {

    private Paint mBackPaint;
    private Paint mRectPaint;
    private Rect mRect = new Rect();
    private String mResult;
    private TextPaint mTextPaint;

    public ScannerView(Context context) {
        this(context, null);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint.setARGB(0x99, 0x00, 0x00, 0x00);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.BLACK);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setShadowLayer(1, 1, 1, Color.BLACK);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int rw = getWidth() * 2 / 3;
        mRect.set(rw / 4, getHeight() / 2 - rw / 2, rw + rw / 4, getHeight() / 2 + rw / 2);
        //画背景半透明
        canvas.drawRect(0, 0, getWidth(), mRect.top, mBackPaint);
        canvas.drawRect(0, mRect.bottom, getWidth(), getHeight(), mBackPaint);
        canvas.drawRect(0, mRect.top, mRect.left, mRect.bottom, mBackPaint);
        canvas.drawRect(mRect.right, mRect.top, getWidth(), mRect.bottom, mBackPaint);
        //画格子
        int ch = mRect.height() / 9;
        canvas.clipRect(mRect);
        for (int i = 0; i < 9; i++) {
            canvas.drawLine(mRect.left, mRect.top + i * ch, mRect.right, mRect.top + i * ch, mRectPaint);
            canvas.drawLine(mRect.left + i * ch, mRect.top, mRect.left + i * ch, mRect.bottom, mRectPaint);
        }
        //画数字
        if (TextUtils.isEmpty(mResult)) return;
        if (mResult.length() != 81) return;
        mTextPaint.setTextSize(ch * 2 / 3);
        Rect charRect = new Rect();
        for (int i = 0; i < mResult.length(); i++) {
            char c = mResult.charAt(i);
            String s = String.valueOf(c);
            if ("0".equals(s)) continue;
            mTextPaint.getTextBounds(s, 0, 1, charRect);
            int x = i % 9;
            int y = i / 9;
            canvas.drawText(s, mRect.left + x * ch + ch / 2,
                    mRect.top + y * ch + ch / 2 + charRect.height() / 2, mTextPaint);
        }
    }

    public void setResult(String result) {
        mResult = result;
        invalidate();
    }
}
