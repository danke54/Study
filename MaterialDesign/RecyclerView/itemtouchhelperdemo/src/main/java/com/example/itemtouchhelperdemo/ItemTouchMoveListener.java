package com.example.itemtouchhelperdemo;

/**
 * Created by zhangke on 16/6/21.
 */
public interface ItemTouchMoveListener {

    /**
     * 当Item上下拖动时调用
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * 当item左右滑动时调用
     */
    boolean onItemRemove(int position);
}
