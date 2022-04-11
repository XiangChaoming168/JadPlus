package com.evans.common;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class FileOperation {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压单个文件
     * @param srcFile  zip文件路径
     * @param destDirPath  释放路径
     */
    public static List<File> unZip(File srcFile, String destDirPath){
        List<File> zipFilesPathList = new ArrayList<>();

        Constants.LOGGER.info("开始解压 " + srcFile.getAbsolutePath());
        long start = System.currentTimeMillis();
        // 开始解压
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(destDirPath+ File.separator +
                        srcFile.getName().substring(0, srcFile.getName().lastIndexOf(".")) + File.separator +
                        zipEntry.getName());
                // 如果是文件夹，就创建个文件夹
                if (zipEntry.isDirectory()) {
                    targetFile.mkdirs();
                } else {

                    // 保证这个文件的父文件夹必须要存在
                    if(!targetFile.getParentFile().exists()){
                        targetFile.getParentFile().mkdirs();
                    }

                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }

                    // 把释放文件路径添加到集合
                    zipFilesPathList.add(targetFile);

                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();

                    // 统计文件数量
                    Constants.FILE_COUNT = Constants.FILE_COUNT + 1;
                }
            }
        } catch (IOException e) {
            Constants.LOGGER.error("解压出错 " + srcFile.getAbsolutePath() + " ,文件不存在！");
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Constants.LOGGER.info("完成解压 " + srcFile.getAbsolutePath() + " ，耗时 " + (end - start) +" ms");
        return zipFilesPathList;
    }

    /**
     * 根据文件类型获取集合中的文件
     * @param fileList 文件集合
     * @param type 文件类型（.class）
     * @return
     */
    public static List<File> filterFiles(List<File> fileList, String type) {
        List<File> temp = new ArrayList<>();
        for (File file: fileList) {
            if (file.isFile()) {
                if (type == null) {
                    temp.add(file);
                } else {
                    if (file.getName().endsWith(type)) {
                        temp.add(file);
                    }
                }
            }
        }
        return temp;
    }

//    // 多线程写文件
//    public static void writeMessage(File file, String msg) {
//        try {
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            //对该文件加锁
//            RandomAccessFile out = new RandomAccessFile(file, "rw");
//            FileChannel fcout = out.getChannel();
//            FileLock flout;
//
//            while(true){
//                try {
//                    flout = fcout.tryLock();
//                    break;
//                } catch (Exception e) {
//                    FileOperation.writeMessage(Constants.LOG_FILE, "有其他线程正在操作该文件 " + file.getName() + "，当前线程休眠40毫秒");
//                    Thread.sleep(40);
//                }
//
//            }
//
//            // 写文件
//            long fileLength = out.length();
//            out.seek(fileLength);
//            out.writeBytes(msg);
//
//            // 关闭资源
//            flout.release();
//            fcout.close();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 获取文件夹下的所有文件
     * @param dirPath 文件夹路径
     * @return
     */
    public static List<File> traverseFolder(File dirPath) {
        List<File> fileList = new ArrayList<>();
        if (dirPath.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = dirPath.listFiles();
            for(File file2: files) {
                if (file2.isDirectory()) {
                    //System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                } else {
                    fileList.add(file2);
                    //System.out.println("文件:" + file2.getAbsolutePath());
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2: files) {
                    if (file2.isDirectory()) {
                        //System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                    } else {
                        fileList.add(file2);
                        //System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            Constants.LOGGER.error("文件 " + dirPath.getAbsolutePath() +" 不存在!");
        }
        return fileList;
    }


}
