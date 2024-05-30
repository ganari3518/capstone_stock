package com.syu.capstone_stock;

import com.syu.capstone_stock.util.PythonExec;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class CapstoneStockApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void pyTest() {
//        List<String> res = PythonExec.exec("top_10_stock.py");
//        System.out.println(res.get(0));

        PythonExec.exec("graph.py", "005930");

//        res = PythonExec.exec("graph.py","005930");
////        res = PythonExec.exec("graph.py","000660");
//        PythonExec.print(res);
    }

    @Test
    void PathTest(){
//        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));
        //파이썬의 경우
        //print(os.path.expanduser("~"))
    }

    @Test
    void allStockTest() {
        System.out.println(PythonExec.execByzt("all_stock_code_name.py"));
    }

    @Test
    void exchange(){
        List<String> res = PythonExec.exec("exchange_rate.py");
        System.out.println(res.get(0).toString());
    }

    @Test
    void info(){
        List<String> res = PythonExec.exec("stock_info.py", "005930");
        System.out.println(res.get(0).toString());
    }
}