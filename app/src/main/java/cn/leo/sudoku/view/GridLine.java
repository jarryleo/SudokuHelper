package cn.leo.sudoku.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridLine extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int x, y, l, t, r, b;
        int i = layoutManager.getPosition(view);
        x = i % 9;
        y = i / 9;
        if (x % 3 == 0) l = 2;
        else l = 1;
        if (x % 3 == 2) r = 2;
        else r = 1;
        if (y % 3 == 0) t = 2;
        else t = 1;
        if (y % 3 == 2) b = 2;
        else b = 1;
        outRect.set(l, t, r, b);
    }
}
