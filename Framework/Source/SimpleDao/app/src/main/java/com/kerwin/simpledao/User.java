package com.kerwin.simpledao;


import com.kerwin.simpledao.db.annotion.DbFiled;
import com.kerwin.simpledao.db.annotion.DbTable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@DbTable("tb_user")
public class User {
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User( ) {
    }

    @DbFiled("name")
    public String name;
    //123456
    @DbFiled("password")
    public String password;
}
