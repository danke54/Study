package com.kerwin.simpledao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.kerwin.simpledao.db.annotion.DbFiled;
import com.kerwin.simpledao.db.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    /**
     * 持有数据库操作类的引用
     */
    private SQLiteDatabase database;
    /**
     * 保证实例化一次
     */
    private boolean isInit = false;
    /**
     * 持有操作数据库表所对应的java类型
     * User
     */
    private Class<T> entityClass;
    /**
     * 维护这表名与成员变量名的映射关系
     * key---》表名
     * value --》Field
     */
    private HashMap<String, Field> cacheMap;

    private String tableName;

    /**
     * 初始化表
     *
     * @param entity
     * @param sqLiteDatabase
     * @return 实例化一次
     */
    protected synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            entityClass = entity;
            database = sqLiteDatabase;
            // 获取数据库表名，User类
            if (entity.getAnnotation(DbTable.class) == null) {
                tableName = entity.getClass().getSimpleName();
            } else {
                tableName = entity.getAnnotation(DbTable.class).value();
            }


            if (!database.isOpen()) {
                return false;
            }

            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }
            cacheMap = new HashMap<>();
            initCacheMap();

            isInit = true;
        }
        return isInit;
    }

    /**
     * 表名和字段映射关系
     */
    private void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            /**
             * 表的列名数组
             */
            String[] columnNames = cursor.getColumnNames();
            /**
             * 拿到Filed数组
             */
            Field[] colmunFields = entityClass.getFields();
            for (Field filed : colmunFields) {
                filed.setAccessible(true);
            }
            /**
             * 开始找对应关系
             */
            for (String colmunName : columnNames) {
                /**
                 * 如果找到对应的Filed就赋值给他
                 * User
                 */
                Field colmunFiled = null;
                for (Field field : colmunFields) {
                    String fieldName = null;
                    if (field.getAnnotation(DbFiled.class) != null) {
                        fieldName = field.getAnnotation(DbFiled.class).value();
                    } else {
                        fieldName = field.getName();
                    }
                    /**
                     * 如果表的列名 等于了  成员变量的注解名字
                     */
                    if (colmunName.equals(fieldName)) {
                        colmunFiled = field;
                        break;
                    }
                }
                //找到了对应关系
                if (colmunFiled != null) {
                    cacheMap.put(colmunName, colmunFiled);
                }
            }

        } catch (Exception e) {

        } finally {
            cursor.close();
        }

    }

    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        Long result = database.insert(tableName, null, values);
        return result;
    }

    /**
     * 讲map 转换成ContentValues
     *
     * @param map
     * @return
     */
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }

        return contentValues;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> filedsIterator = cacheMap.values().iterator();
        /**
         * 循环遍历 映射map的  Filed
         */
        while (filedsIterator.hasNext()) {
            /**
             *
             */
            Field colmunToFiled = filedsIterator.next();
            String cacheKey = null;
            String cacheValue = null;

            // 获取表中列名
            if (colmunToFiled.getAnnotation(DbFiled.class) != null) {
                cacheKey = colmunToFiled.getAnnotation(DbFiled.class).value();
            } else {
                cacheKey = colmunToFiled.getName();
            }
            try {
                if (null == colmunToFiled.get(entity)) {
                    continue;
                }
                // 获取值
                cacheValue = colmunToFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }

        return result;
    }

    @Override
    public int update(T entity, T where) {
        int reslut = -1;
        Map values = getValues(entity);
        /**
         * 将条件对象 转换map
         */
        Map whereClause = getValues(where);

        Condition condition = new Condition(whereClause);
        ContentValues contentValues = getContentValues(values);
        reslut = database.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());
        return reslut;
    }

    /**
     * 封装修改语句
     */
    class Condition {
        /**
         * 查询条件
         * name=? && password =?
         */
        private String whereClause;

        private String[] whereArgs;

        public Condition(Map<String, String> whereClause) {
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(" 1=1 ");
            Set keys = whereClause.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = whereClause.get(key);

                if (value != null) {
                    /*
                    拼接条件查询语句
                    1=1 and name =? and password=?
                     */
                    stringBuilder.append(" and " + key + " =?");
                    /**
                     * ？----》value
                     */
                    list.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);

        }

        public String[] getWhereArgs() {
            return whereArgs;
        }

        public String getWhereClause() {
            return whereClause;
        }
    }


    @Override
    public int delete(T where) {
        Map map = getValues(where);
        Condition condition = new Condition(map);
        int result = database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());

        return result;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValues(where);

        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }


        Condition condition = new Condition(map);
        Cursor cursor = database.query(tableName, null, condition.getWhereClause()
                , condition.getWhereArgs(), null, null, orderBy, limitString);
        List<T> result = getResult(cursor, where);
        cursor.close();
        return result;
    }

    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list = new ArrayList();

        Object item;
        while (cursor.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                /**
                 * 列名  name
                 * 成员变量名  Filed;
                 */
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    /**
                     * 得到列名
                     */
                    String colomunName = (String) entry.getKey();
                    /**
                     * 然后以列名拿到  列名在游标的位子
                     */
                    Integer colmunIndex = cursor.getColumnIndex(colomunName);

                    Field field = (Field) entry.getValue();

                    Class type = field.getType();
                    if (colmunIndex != -1) {
                        if (type == String.class) {
                            //反射方式赋值
                            field.set(item, cursor.getString(colmunIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(colmunIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(colmunIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(colmunIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(colmunIndex));
                            /*
                            不支持的类型
                             */
                        } else {
                            continue;
                        }
                    }

                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    /**
     * 创建表
     *
     * @return
     */
    protected abstract String createTable();

}
