package cn.leo.sudoku;

import android.view.View;

public class TestJava {
    private OnClickLister mOnClickLister;

    public void test() {
        if (mOnClickLister != null) {
            mOnClickLister.onClick(null);
        }
    }

    public void setOnClickLister(OnClickLister onClickLister) {
        mOnClickLister = onClickLister;
    }

    interface OnClickLister {
        void onClick(View view);
    }
}
