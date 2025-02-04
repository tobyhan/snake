package com.hanzl.snake;

import javax.swing.*;

public class StartGame {
    public static void main(String[] args) {
        // 1.创建一个窗口
        JFrame frame = new JFrame("贪食蛇");
        frame.setBounds(10, 10, 803, 600);// 设置窗口的位置和大小
        frame.setResizable(false);// 窗口大小不可调整，即固定窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置关闭事件，游戏可以关闭

        // 2.添加我们自己编写的画布背景
        frame.add(new GamePanel());

        frame.setVisible(true);// 将窗口展示出来
    }
}
