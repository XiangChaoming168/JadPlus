package com.evans.test;

import com.evans.common.Constants;
import com.evans.common.Utils;
import com.evans.controller.jad.Decompile;
import com.evans.controller.jad.FernflowerDecompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

public class Test1 {


    public static void task(){
        String id = UUID.randomUUID().toString();
        for (var i=1; i<10; i++) {
            System.out.println(id  + ": " + + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        System.out.println(List.of(new String[]{"ww", "aa"}));

    }
}
