package com.evans.test;

import com.evans.common.Constants;
import com.evans.common.Utils;
import com.evans.controller.jad.Decompile;
import com.evans.model.JavaFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {

    public static void main(String[] args) throws Exception {
        // 读取自定义配置文件
        System.out.println(Utils.matchKeys("aaa", ".*"));

        Decompile decompile = new Decompile();
        JavaFile jf = new JavaFile();
        jf.setClassPath("D:\\temp\\test\\MainClient.class");
//        jf.setDestDirPath("D:\\temp\\test");
//        decompile.decompileClass(jf, new ArrayList<>());
        //decompile.decompileJarSecond(new File("D:\\temp\\test\\webwolf-8.1.0.jar"), "D:\\temp\\test\\123", new ArrayList<>(), new String[]{"include", ".*"});
        decompile.decompileClass(jf, new ArrayList<>());

    }

}
