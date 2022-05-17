package com.evans.view;

import com.evans.common.Constants;
import com.evans.common.FileOperation;
import com.evans.common.Utils;
import com.evans.controller.jad.Decompile;
import com.evans.model.JavaFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

public class MainClient {

    //创建主界面
    JFrame mainFrame = new JFrame("JadPlus2.4+");

    //选择部署包
    JLabel deployLabel = new JLabel("文件路径：");
    JTextField deployTextField = new JTextField("", 36);

    // 选择反编译释放路径
    JLabel workLabel = new JLabel("释放路径：");
    JTextField workTextField = new JTextField("",36);

    // 反编译启动按钮
    JButton startButton = new JButton("开始");

    // 反编译设置按钮
    JButton settingButton = new JButton("高级设置");

    // 设置依赖包编译策略
    JLabel dependencyLabel = new JLabel("依赖包反编译策略：");
    JComboBox<String> dependencySelect;
    JTextField dependencyTextField = new JTextField(".*");


    //进度条
    JLabel progressLabel = new JLabel("执行进度：");
    JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    JButton progressButton = new JButton("<<<");

    DetailPanel detailPane = new DetailPanel();
    JadSettingPanel jadSettingPanel = new JadSettingPanel();

    public void init() {

        detailPane.init();
        jadSettingPanel.init();

        // 1.文件源路径与输出路径
        Box deployBox = Box.createHorizontalBox();
        deployBox.add(deployLabel);
        deployBox.add(deployTextField);

        Box workBox = Box.createHorizontalBox();
        workBox.add(workLabel);
        workBox.add(workTextField);

        // 2.进度条
        Box progressBox = Box.createHorizontalBox();
        //根据选择决定是否绘制进度条边框
        progressBar.setBorderPainted(true);
        //设置进度条中绘制完成百分比
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setValue(0);
        progressBox.add(progressLabel);
        progressBox.add(progressBar);
        progressBox.add(progressButton);

        // 3.操作相关
        Box operationBox = Box.createHorizontalBox();
        operationBox.add(settingButton);
        operationBox.add(Box.createHorizontalGlue());
        operationBox.add(dependencyLabel);
        Vector<String> dependencyList = new Vector<>();
        dependencyList.add("include");
        dependencyList.add("exclude");
        dependencySelect = new JComboBox<>(dependencyList);
        operationBox.add(dependencySelect);
        operationBox.add(dependencyTextField);
        operationBox.add(Box.createHorizontalGlue());
        operationBox.add(startButton);

        // 4.设置卡片布局
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        mainPanel.add("细节显示", detailPane);
        mainPanel.add("高级设置", jadSettingPanel);


        // 5.将盒子装配Panel
        Box mainBox = Box.createVerticalBox();
        mainBox.add(deployBox);
        mainBox.add(workBox);
        mainBox.add(progressBox);
        mainBox.add(operationBox);
        mainBox.add(mainPanel);


        // 6.Frame装配
        mainFrame.add(mainBox);
        // 设置jFrame最佳大小并可见
        mainFrame.pack();
        mainFrame.setLocation(100, 100);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        // 设置关闭窗口时推出程序
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //*********************************************************************************
        // 设置监听
        // 开始按钮添加监听

        DetailPanelTimer detailPanelTimer = new DetailPanelTimer(this);

        //*********************************************************************************
        //设置监听
        // 开始按钮添加监听
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("开始".equals(startButton.getText())) {

                    String deployT = deployTextField.getText();
                    String workT = workTextField.getText();
                    File workDir = new File(workT);
                    if (Utils.isEmpty(deployT) || !(new File(deployT).exists())) {
                        JOptionPane.showMessageDialog(mainPanel, "文件路径不存在", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (Utils.isEmpty(workT)) {
                        JOptionPane.showMessageDialog(mainPanel, "释放路径为不存在", "警告", JOptionPane.WARNING_MESSAGE);
                    } else {
                        detailPane.setDetail("");
                        detailPane.setDetail("任务开始...");

                        cardLayout.show(mainPanel, "细节显示");
                        startButton.setText("进行中");

                        detailPane.setDetail("文件路径：" + deployTextField.getText().trim());
                        detailPane.setDetail("释放路径：" + workTextField.getText().trim());
                        detailPane.setDetail("高级配置：" + jadSettingPanel.getOptionsMap().toString());
                        detailPane.setDetail("依赖包反编译策略：" + dependencySelect.getSelectedItem().toString()+ ": "+dependencyTextField.getText());
                        detailPane.setDetail("任务进行中...");

                        // 启动反编译
                        Decompile decompile = new Decompile();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                decompile.decompileJarAll(new File(deployTextField.getText().trim()), workTextField.getText().trim(), jadSettingPanel.getOptionsMap(),
                                        new String[]{dependencySelect.getSelectedItem().toString(), dependencyTextField.getText()});

                            }
                        }).start();

                        // 启动监听
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                detailPanelTimer.runTimer();
                            }
                        }).start();
                    }

                }else {
                    cardLayout.show(mainPanel, "细节显示");
                }
            }
        });

        // 设置按钮监听
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ("高级设置".equals(settingButton.getText())) {
                    cardLayout.show(mainPanel, "高级设置");
                    settingButton.setText("返回");
                }else if("返回".equals(settingButton.getText())) {
                    cardLayout.show(mainPanel, "细节显示");
                    settingButton.setText("高级设置");
                }
            }
        });
    }


    public static void main(String[] args) {

        new MainClient().init();
    }

}

class DetailPanelTimer {

    public MainClient frame;

    public DetailPanelTimer (MainClient mainClient) {
        this.frame = mainClient;
    }


    public void runTimer() {
        long start = System.currentTimeMillis();
        while (true) {

            int fileCount = Constants.FILE_COUNT;
            int javaCount = Constants.JAVA_COUNT;
            int classCount = Constants.ClASS_COUNT.size();

            // 1.设置进程检测间隔时间
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 2.监听结束标志判断 TIMER_COUNT
            if (Constants.TIMER_COUNT >= 4) {
                frame.detailPane.setDetail("正在进行特殊class文件反编译，请勿关闭窗口!");
                // 干掉漏网之鱼
                java.util.List<File> fileList = FileOperation.traverseFolder(new File(frame.workTextField.getText()));
                java.util.List<File> classList = FileOperation.filterFiles(fileList, ".class");
                for (File file : classList) {
                    JavaFile javaFile = new JavaFile();
                    javaFile.setClassPath(file.getAbsolutePath());
                    javaFile.setDestDirPath(file.getParent());
                    new Decompile().decompileClass(javaFile, frame.jadSettingPanel.getOptionsMap());
                }

                java.util.List<File> javaList = FileOperation.filterFiles(fileList, ".java");

                frame.detailPane.setDetail("生成java文件 " + javaList.size() + " 个，总文件 " + fileList.size() + " 个。");
                frame.progressBar.setValue(100);
                frame.progressBar.setString("100%");

                long end = System.currentTimeMillis();
                frame.detailPane.setDetail("总计耗时： " + (end - start) +" ms");
                break;
            }

            // 3.设置界面(进度条、文本域)显示
            int fileCount2 = Constants.FILE_COUNT;
            int javaCount2 = Constants.JAVA_COUNT;
            int classCount2 = Constants.ClASS_COUNT.size();

            frame.detailPane.setDetail("预计生成java文件 " + javaCount2 + " 个，实时剩余class文件 "+ classCount2 + " 个，复制总文件 " + fileCount2 + " 个。");

            // 根据释放路径中 class 与 java 文件的比值算百分比
            String percent = Utils.getPercent(javaCount2, javaCount2+classCount2+1);
            frame.progressBar.setValue((int) Float.parseFloat(percent));
            frame.progressBar.setString(percent + "%");

            // 4.设置监听变量
            if (javaCount2 > 0 &&
                    javaCount == javaCount2 &&
                    fileCount == fileCount2 &&
                    classCount == classCount2 &&
                    classCount2 == 0
            ) {
                Constants.TIMER_COUNT = Constants.TIMER_COUNT + 1;
                frame.detailPane.setDetail("********************开始第 " + Constants.TIMER_COUNT + " 次检查********************");
            }

        }

        // 5.还原变量池
        Constants.TIMER_COUNT = 0;
        Constants.FILE_COUNT = 0;
        Constants.JAVA_COUNT = 0;
        Constants.ClASS_COUNT.clear();

        frame.startButton.setText("开始");
        frame.detailPane.setDetail("任务结束！");

    }
}
