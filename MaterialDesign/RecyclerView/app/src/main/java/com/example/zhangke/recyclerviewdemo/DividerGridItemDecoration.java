package com.example.zhangke.recyclerviewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * item分割线
 * <p/>
 * Created by zhangke on 16/6/15.
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 分割线
     */
    private static int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 水平分割线
     */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    /**
     * 垂直分割线
     */
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 分割线
     */
    private Drawable mDivider;

    private int mOrientation;
    private int spanCount;

    /**
     * @param context     上下文
     * @param orientation 分割线方向
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DividerGridItemDecoration(Context context, int orientation) {
        this.mContext = context;
//        TypedArray typedArray = mContext.obtainStyledAttributes(ATTRS);
//        mDivider = typedArray.getDrawable(0);
//        typedArray.recycle();

        mDivider = context.getDrawable(R.drawable.divider_view);
        setOrientation(orientation);
    }

    /**
     * 设置分割线方向
     *
     * @param orientation
     */
    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("参数异常");
        }
        this.mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent, state);
        drawVertical(c, parent, state);
    }

    /**
     * 绘制垂直方向分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    private void drawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (GridLayoutManager.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight()+ params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

    /**
     * 绘制水平方向分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    private void drawHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (GridLayoutManager.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 设置分割线宽偏移量:
     * 通过RecyclerView源码可知：getItemOffset方法会在measureChild方法中调用，我们通过给方法的参数outRect
     * 设置left、top、right、bottom，设置的值会被增加到childView的padding值中。
     */
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (isLastColum(itemPosition, parent)) {
            //最后一列
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else if (isLastRow(itemPosition, parent)) {
            //最后一行
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }

    }

    /**
     * 是否是最后一行
     *
     * @param itemPosition
     * @param parent
     * @return
     */
    private boolean isLastRow(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int childCount = parent.getAdapter().getItemCount();

            int lastRowCount = childCount %  spanCount;
            if (lastRowCount == 0) {
                return false;
            } else if(itemPosition >= (childCount - lastRowCount)){
                return true;
            }
        }
        return false;
    }

    /*
    * 是否是最后一列
     */
    private boolean isLastColum(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = getSpanCount(parent);
            if ((itemPosition + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager lm = (GridLayoutManager) layoutManager;
            int spanCount = lm.getSpanCount();
            return spanCount;
        }
        return 0;
    }

}
