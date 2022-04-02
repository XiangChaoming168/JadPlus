package com.evans.common;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    // 判断字符串是否包含一条规则
    public static boolean matchKeys(String s, String... regex){
        boolean flag = false;
        for (String reg : regex) {
            Matcher matcher = Pattern.compile(reg).matcher(s);
            if (matcher.matches()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //判断字符串是不是空
    public static boolean isEmpty(String s){
        boolean flag = false;

        if (null==s || s.trim().equals("")) {
            flag = true;
        }
        return flag;
    }

    //求百分比
    public static String getPercent(int number, int denominator){
        double result = (float) number / (float) denominator * 100;
        return String.format("%.2f", result);
    }
}
