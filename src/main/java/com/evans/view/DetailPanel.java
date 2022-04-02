package com.evans.view;

import javax.swing.*;

public class DetailPanel extends JPanel {

    // 细节显示窗口
    JTextArea detailTextArea = new JTextArea("Author:Xiang  QQ:239036082", 40, 101);

    public void init() {
        // 设置不可编辑
        detailTextArea.setEditable(false);
        // 设置自动换行
        detailTextArea.setLineWrap(true);
        // 实时显示
        JScrollPane detailScrollPane = new JScrollPane(detailTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(detailScrollPane);
    }

    public void setDetail(String msg) {
        detailTextArea.setText(detailTextArea.getText() + "\r\n" + msg);
        detailTextArea.setCaretPosition(detailTextArea.getText().length());
    }

}
