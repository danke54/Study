package com.example.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhangke on 16/6/21.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemTouchMoveListener {
    private List<Message> list;
    private StartDragListener dragListener;

    public RecyclerAdapter(List<Message> list, StartDragListener dragListener) {
        this.list = list;
        this.dragListener = dragListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvHead;
        private TextView mTvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIvHead = (ImageView) itemView.findViewById(R.id.iv_logo);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int location) {
        List<Message> list = this.list;
        Message message = list.get(location);
        holder.mIvHead.setImageResource(message.getImage());
        holder.mTvTitle.setText(message.getTitle());

        // 给头像设置触摸事件，实现触摸头像时实现拖动效果
        holder.mIvHead.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 执行开始拖动动画
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    /**
     * 当Item上下拖动会调用该方法
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 当Item左右滑动时调用该方法
     *
     * @param position
     * @return
     */
    @Override
    public boolean onItemRemove(int position) {
        list.remove(position);
        notifyItemRemoved(position);

        return true;
    }


}
