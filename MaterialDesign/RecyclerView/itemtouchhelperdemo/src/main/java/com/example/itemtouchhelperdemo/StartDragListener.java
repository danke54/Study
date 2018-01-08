package com.example.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView.ViewHolder;

public interface StartDragListener {
	/**
	 * 开始拖动
	 * @param viewHolder
	 */
	public void onStartDrag(ViewHolder viewHolder);

}
