package com.dalaoyang.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: springboot_mybitsplus_swagger
 * @description: 格式化时间
 * @author: Mr.Wang
 * @create: 2018-11-29 13:15
 **/
public class DateUtils {

    public static Date dateToStr(String  dataDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(dataDate, pos);
        return strtodate;
    }
}
