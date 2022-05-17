package com.evans.controller.jad;

import com.evans.common.Constants;
import com.evans.common.FileOperation;
import com.evans.common.Utils;
import com.evans.model.JavaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Decompile {

    /**
     * 解压一个jar包， 并封装成模型
     * @param jarFile jar包路径
     * @param destDirPath 反编译路径
     */
    public List<JavaFile> getJavaModel(File jarFile, String destDirPath) {
        List<JavaFile> javaFileList = new ArrayList<>();

        List<File> fileList = FileOperation.unZip(jarFile, destDirPath);

        // 获取所有的class文件, 然后封装模型
        List<File> clsFileList = FileOperation.filterFiles(fileList, ".class");

        for (File clsFile: clsFileList) {

            String clsPath = clsFile.getAbsolutePath();

            if (!clsPath.contains("$")) {
                // Java文件模型
                JavaFile javaFile = new JavaFile();
                javaFile.setJarPath(jarFile.getAbsolutePath());
                javaFile.setClassPath(clsPath);
                javaFile.setDestDirPath(clsFile.getParent());

                // 封装代理类
                File destDir = new File(clsFile.getParent());
                List<String> tempList = new ArrayList<>();
                for (File file: destDir.listFiles()) {
                    String proxyPath = file.getAbsolutePath();
                    if (proxyPath.contains("$") &&
                                    (proxyPath.startsWith(clsPath.substring(0, clsPath.length()-6)+"_$") ||
                                    proxyPath.startsWith(clsPath.substring(0, clsPath.length()-6)+"$"))

                    ) {
                        tempList.add(proxyPath);
                    }
                }
                javaFile.setProxyClassPath(tempList);
                javaFileList.add(javaFile);

                Constants.ClASS_COUNT.add(1);
            }


        }
        return javaFileList;
    }

    /**
     * 反编译class文件
     * @param jf JavaFile模型
     * @param mapOptions 反编译过程参数配置
     */
    public void decompileClass(JavaFile jf, List<String> mapOptions) {

        List<String> params = new ArrayList<>(mapOptions);

        params.add(jf.getClassPath());

        if (null != jf.getProxyClassPath() && jf.getProxyClassPath().size() > 0){
            params.addAll(jf.getProxyClassPath());
        }

        params.add(jf.getDestDirPath());

        // 将转化为
        String[] paramsArray = new String[params.size()];
        for (int i=0; i< params.size(); i++) {
            paramsArray[i] = params.get(i);
        }

        // 开始反编译class
        new FernflowerDecompiler(paramsArray).mainWork();

        // 编译完成后删除 class
        new File(jf.getDestDirPath() + File.separator + new File(jf.getClassPath()).getName()).delete();
        if (null != jf.getProxyClassPath()) {
            for (String cp : jf.getProxyClassPath()) {
                new File(jf.getDestDirPath() + File.separator + new File(cp).getName()).delete();
            }
        }

    }

    /**
     * 反编译一个jar包
     * @param jarFile jar包路径
     * @param destDirPath 反编译路径
     * @param mapOptions 反编译过程参数配置
     */
    public void decompileJar(File jarFile, String destDirPath, List<String> mapOptions) {

        List<JavaFile> javaModel = getJavaModel(jarFile, destDirPath);

        for (JavaFile jf: javaModel) {

            Constants.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!Constants.ClASS_COUNT.isEmpty()){
                            Constants.ClASS_COUNT.remove(0);
                        }
                        decompileClass(jf, mapOptions);
                    } catch (Exception e) {
                        Constants.LOGGER.error("反编译出错->" + jf.toString());
                    }
                }
            });

            Constants.JAVA_COUNT = Constants.JAVA_COUNT + 1;
        }

    }

    /**
     * 反编译jar包及其依赖包
     * @param jarFile jar包路径
     * @param destDirPath 解压目录
     * @param mapOptions 反编译过程参数配置
     * @param strategy  依赖包反编译策略，确定是否反编译子包的规则
     */
    public void decompileJarSecond(File jarFile, String destDirPath, List<String> mapOptions, String[] strategy) {
        Constants.LOGGER.info("正在反编译jar " + jarFile.getAbsolutePath());

        decompileJar(jarFile, destDirPath, mapOptions);

        // 扫描该jar包中的依赖包，然后解压
        // 依赖包解压后的路径：destDirPath + jar包包名
        String dependcyPath = destDirPath + File.separator + jarFile.getName().substring(0, jarFile.getName().length()-4);
        List<File> fileList = FileOperation.traverseFolder(new File(dependcyPath));
        for (File file : FileOperation.filterFiles(fileList, ".jar")) {
            // 反编译依赖包
            // 依赖包解压后的路径：destDirPath + dependencies + 依赖包包名
            String newDest = destDirPath + File.separator + "dependencies" + File.separator + file.getName().substring(0, file.getName().length()-4);
            File newDestDir = new File(newDest);
            // 如果解压路径已经存在，说明已经编译好了，不需要重复编译
            if (!newDestDir.exists()) {
                // 策略判断那些依赖包需要编译
                if ("include".equals(strategy[0]) && Utils.matchKeys(file.getName(), strategy[1])) {
                    Constants.LOGGER.info("正在反编译 " + jarFile.getAbsolutePath() + " 的依赖jar " + file.getName());
                    decompileJar(file, newDestDir.getParent(), mapOptions);
                }
                if ("exclude".equals(strategy[0]) && !Utils.matchKeys(file.getName(), strategy[1])) {
                    Constants.LOGGER.info("正在反编译 " + jarFile.getAbsolutePath() + " 的依赖jar " + file.getName());
                    decompileJar(file, newDestDir.getParent(), mapOptions);
                }

            } else {
                Constants.LOGGER.info("已经编译过了, 不再重复编译 " + jarFile.getAbsolutePath() + " 的依赖jar " + file.getName());
            }
        }

    }

    /**
     * 反编译目录下所有文件
     * @param srcDir 文件目录
     * @param destDirPath 解压目录
     * @param mapOptions 反编译过程参数配置
     * @param strategy  依赖包反编译策略，确定是否反编译子包的规则
     */
    public void decompileJarAll(File srcDir, String destDirPath, List<String> mapOptions, String[] strategy){

        Constants.FILE_COUNT = 1;
        Constants.JAVA_COUNT = 1;

        if (srcDir.isDirectory()) {
            // 获取目录下所有文件
            for (File file : FileOperation.traverseFolder(srcDir)) {
                if (file.isFile() && (file.getName().endsWith(".jar") || file.getName().endsWith(".war"))) {
                    decompileJarSecond(file, destDirPath, mapOptions, strategy);
                }
            }
        }

        if (srcDir.isFile()) {
            if (srcDir.getName().endsWith("jar") || srcDir.getName().endsWith("war")) {
                decompileJarSecond(srcDir, destDirPath, mapOptions, strategy);
            } else if (srcDir.getName().endsWith(".class")) {

                JavaFile javaFile = new JavaFile();
                javaFile.setClassPath(srcDir.getAbsolutePath());
                javaFile.setDestDirPath(destDirPath);
                File destDir = new File(destDirPath);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                // Constants.ClASS_COUNT.add(1);
                decompileClass(javaFile, mapOptions);
            }

        }

    }

}
