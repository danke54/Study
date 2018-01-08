package com.example.zhangke.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhangke on 16/6/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {


    private Context mContext;
    private ArrayList<String> mDatas;
    private OnItemClickListener mOnItemClickListener;
    private OnLongItemClickListener mOnLongItemClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(View.inflate(mContext, R.layout.item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.mTextView.setText(mDatas.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, layoutPosition);
                }
            });
        }
        if (mOnLongItemClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnLongItemClickListener.onLongItemClick(v, layoutPosition);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * viewholder
     */
    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }
    }

    /**
     * 添加
     * @param position
     * @param item
     */
    public void addItem(int position, String item){
        mDatas.add(position, item+position);
        notifyItemInserted(position);
    }

    /**
     * 删除
     * @param position
     */
    public void removeItem(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        /**
         * item点击事件
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position);
    }

    public interface OnLongItemClickListener {
        /**
         * item长按点击事件
         *
         * @param view
         * @param position
         */
        void onLongItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.mOnLongItemClickListener = listener;
    }
}
