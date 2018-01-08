package com.example.zhangke.recyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhangke on 16/6/15.
 */
public class StaggeredGridLayoutAdapter extends RecyclerView.Adapter<StaggeredGridLayoutAdapter.RecyclerViewHolder> {


    private ArrayList<Integer> heights;
    private Context mContext;
    private ArrayList<String> mDatas;

    public StaggeredGridLayoutAdapter(Context context, ArrayList<String> datas) {
        this.mContext = context;
        this.mDatas = datas;

        heights = new ArrayList<Integer>();
        for (int i = 0; i < mDatas.size(); i++) {
            heights.add((int) Math.max(200, Math.random() * 550));
        }
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(View.inflate(mContext, R.layout.item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mTextView.getLayoutParams();
        layoutParams.height = heights.get(position);
        holder.mTextView.setBackgroundColor(Color.argb((int) Math.max(100, (Math.random() * 255)), 150, (int) (Math.random() * 255), 80));
        holder.mTextView.setLayoutParams(layoutParams);
        holder.mTextView.setText(mDatas.get(position));
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


}
