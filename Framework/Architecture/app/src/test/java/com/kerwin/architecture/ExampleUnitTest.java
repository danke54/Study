package com.kerwin.architecture;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        Map map = new HashMap<>();

        Student student = new Student();
        student .setName("lisi");
        student.setAge(88);
        map.put("name", "zhangsan");
        map.put("age", 10);
        map.put("user", student);

        JSON.toJSONString(map);
    }

     class Student{
        private String name;
        private int age;

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getAge() {
             return age;
         }

         public void setAge(int age) {
             this.age = age;
         }
     }


}