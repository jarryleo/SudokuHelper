package cn.leo.sudoku.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import cn.leo.sudoku.R;

public class SolvingDialog extends Dialog {
    public SolvingDialog(@NonNull Context context) {
        this(context, 0);
    }

    public SolvingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_solving, null);
        setContentView(inflate);
        setCancelable(false);
    }
}
