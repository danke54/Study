package com.example.zhangke.recyclerviewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * item分割线
 * <p/>
 * Created by zhangke on 16/6/15.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

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

    /**
     * @param context     上下文
     * @param orientation 分割线方向
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DividerItemDecoration(Context context, int orientation) {
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
        if (mOrientation == HORIZONTAL) {
            drawHorizontal(c, parent, state);
        } else {
            drawVertical(c, parent, state);
        }

        super.onDraw(c, parent, state);
    }

    /**
     * 绘制垂直方向分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    private void drawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int top = childView.getBottom() + layoutParams.bottomMargin + Math.round(ViewCompat.getTranslationY(childView));
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
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getRight() + layoutParams.rightMargin + Math.round(ViewCompat.getTranslationX(childView));
            int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 设置分割线宽偏移量:
     * 通过RecyclerView源码可知：getItemOffset方法会在measureChild方法中调用，我们通过给方法的参数outRect
     * 设置left、top、right、bottom，设置的值会被增加到childView的padding值中。
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mOrientation == HORIZONTAL) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }


}
