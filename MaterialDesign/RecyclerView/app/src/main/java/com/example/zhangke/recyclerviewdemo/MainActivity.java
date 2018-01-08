package com.example.zhangke.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> mDatas;
    private RecyclerViewAdapter mAdapter;
    private StaggeredGridLayoutAdapter mStagAdapter;
    private RecyclerView.ItemDecoration mItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        initData();

        //垂直listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        mItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mItemDecoration);
        // 设置增删动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RecyclerViewAdapter(this, mDatas);

        //设置点击事件
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "onItemClick " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnLongItemClickListener(new RecyclerViewAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "onLongItemClick " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("item " + i);
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mItemDecoration != null) {
            mRecyclerView.removeItemDecoration(mItemDecoration);
        }
        switch (item.getItemId()) {
            case R.id.add:

                mAdapter.addItem(1, "item" + 1);
                break;

            case R.id.remove:
                mAdapter.removeItem(1);
                break;

            case R.id.showVertical:
                //垂直listview
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(mItemDecoration);
                mAdapter = new RecyclerViewAdapter(this, mDatas);
                mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "onItemClick " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                mAdapter.setOnLongItemClickListener(new RecyclerViewAdapter.OnLongItemClickListener() {
                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "onLongItemClick " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                mRecyclerView.setAdapter(mAdapter);
                break;

            case R.id.showHorizontal:
                //水平listview
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
                mRecyclerView.addItemDecoration(mItemDecoration);
                mAdapter = new RecyclerViewAdapter(this, mDatas);
                mRecyclerView.setAdapter(mAdapter);
                break;

            case R.id.showGridHorizontal:
                // 水平gridview
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.HORIZONTAL, false));
                mItemDecoration = new DividerGridItemDecoration(this, DividerGridItemDecoration.HORIZONTAL);
                mRecyclerView.addItemDecoration(mItemDecoration);
                mAdapter = new RecyclerViewAdapter(this, mDatas);
                mRecyclerView.setAdapter(mAdapter);
                break;

            case R.id.showGridVertical:
                // 垂直gridview
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                mItemDecoration = new DividerGridItemDecoration(this, DividerGridItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(mItemDecoration);
                mAdapter = new RecyclerViewAdapter(this, mDatas);
                mRecyclerView.setAdapter(mAdapter);
                break;

            case R.id.showStaggle:
                // 瀑布流
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
                mStagAdapter = new StaggeredGridLayoutAdapter(this, mDatas);
                mRecyclerView.setAdapter(mStagAdapter);
                break;
        }
        return true;
    }
}
