package com.evans.model;

import java.util.List;

public class JavaFile {

    // jar包路径
    private String jarPath;
    // class文件路径
    private String classPath;
    // 内部类及代理class路径
    private List<String> proxyClassPath;
    // 解压路径
    private String destDirPath;

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public List<String> getProxyClassPath() {
        return proxyClassPath;
    }

    public void setProxyClassPath(List<String> proxyClassPath) {
        this.proxyClassPath = proxyClassPath;
    }

    public String getDestDirPath() {
        return destDirPath;
    }

    public void setDestDirPath(String destDirPath) {
        this.destDirPath = destDirPath;
    }

    @Override
    public String toString() {
        return "JavaFile{" +
                "jarPath='" + jarPath + '\'' +
                ", classPath='" + classPath + '\'' +
                ", proxyClassPath=" + proxyClassPath +
                ", destDirPath='" + destDirPath + '\'' +
                '}';
    }
}
