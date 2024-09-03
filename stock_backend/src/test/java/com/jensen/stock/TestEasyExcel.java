package com.jensen.stock;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.jensen.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jensen
 * @date 2024-08-28 16:34
 * @description
 */
public class TestEasyExcel {
    public List<User> init(){
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海"+i);
            user.setUserName("张三"+i);
            user.setBirthday(new Date());
            user.setAge(10+i);
            users.add(user);
        }
        return users;
    }

    @Test
    public void test02(){
        List<User> users = init();
        //不做任何注解处理时，表头名称与实体类属性名称一致
        EasyExcel.write("D:\\Document\\Downloads\\baidudisk\\项目1-今日指数课件\\day03-股票数据报表与导出\\用户.xls",User.class).sheet("用户信息").doWrite(users);
    }

    /**
     * excel数据格式必须与实体类定义一致，否则数据读取不到
     */
    @Test
    public void readExcel(){
        ArrayList<User> users = new ArrayList<>();
        //读取数据
        EasyExcel.read("D:\\Document\\Downloads\\baidudisk\\项目1-今日指数课件\\day03-股票数据报表与导出\\用户.xls", User.class, new AnalysisEventListener<User>() {
            @Override
            public void invoke(User o, AnalysisContext analysisContext) {
                System.out.println(o);
                users.add(o);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("完成。。。。");
            }
        }).sheet().doRead();
        System.out.println(users);
    }

}
