package com.example.itemtouchhelperdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements StartDragListener {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //1、设置适配器
        List<Message> list = DataUtils.init();
        RecyclerAdapter adapter = new RecyclerAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        //2、设置ItemTouchHelper
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    /**
     * 在Adapter中需要执行startDrag方法开始拖动效果
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        // 执行拖动效果
        mItemTouchHelper.startDrag(viewHolder);
    }
}
