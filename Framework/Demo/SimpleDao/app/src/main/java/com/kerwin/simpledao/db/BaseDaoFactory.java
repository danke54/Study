package com.kerwin.simpledao.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class BaseDaoFactory {
    private String sqliteDatabasePath;

    private SQLiteDatabase sqLiteDatabase;

    private static BaseDaoFactory instance = new BaseDaoFactory();

    public BaseDaoFactory() {
        sqliteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/teacher.db";
        openDatabase();
    }

    /**
     * @param clazz       具体数据库操作类
     * @param entityClass 操作实体
     * @param <T>
     * @param <M>
     * @return
     */
    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);
        } catch (Exception e) {

        } finally {

        }
        return (T) baseDao;
    }


    private void openDatabase() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);
    }

    public static BaseDaoFactory getInstance() {
        return instance;
    }
}
