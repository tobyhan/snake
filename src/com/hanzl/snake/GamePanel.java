package com.hanzl.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    // 蛇
    int lenth;// 蛇的长度
    int[] snakeX = new int[1000];// 蛇的坐标x
    int[] snakeY = new int[1000];// 蛇的坐标y
    String fx = "R";// 蛇的方向： R:右  L:左  U:上  D:下

    // 食物
    int foodx;
    int foody;
    Random random = new Random();

    // 分数
    int score;

    // 控制
    boolean isStart;// 游戏是否开始
    boolean isFail;// 游戏是否结束
    Timer timer = new Timer(160, this);// 定时器：第一个参数，就是定时执行时间

    // 构造方法
    public GamePanel() {
        init();
        this.setFocusable(true);// 获取焦点事件
        this.addKeyListener(this);// 键盘监听事件
        timer.start();// 时间开始！
    }

    // 初始化方法
    public void init() {
        // 初始小蛇有三节，包括小脑袋
        lenth = 3;

        // 初始化开始的蛇，给蛇定位
        snakeX[0] = 73;
        snakeY[0] = 141;
        snakeX[1] = 48;
        snakeY[1] = 141;
        snakeX[2] = 23;
        snakeY[2] = 141;

        // 初始化食物数据
        foodx = 23 + 25 * random.nextInt(30 - 1);
        foody = 41 + 25 * random.nextInt(20 - 1);

        // 初始化游戏分数
        score = 0;

        // 游戏是否开始
        isStart = false;

        // 游戏是否结束
        isFail = false;

        // 蛇头的方向初始化
        fx = "R";
    }

    // 画组件
    public void paintComponent(Graphics g) {
        super.paintComponent(g);// 清屏
        this.setBackground(Color.WHITE);// 设置面板的背景色
        g.fillRect(23, 41, 750, 500);// 绘制游戏区域

        // 绘制调试网格（横）
        for (int y = 41; y <= (41 + 500); y += 25) {
            for (int x = 23; x <= (23 + 750); x += 25) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(x, y, (23 + 750), y);
            }
        }
        // 绘制调试网格（竖）
        for (int x = 23; x <= (23 + 750); x += 25) {
            for (int y = 41; y <= (41 + 500); y += 25) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(x, y, x, (41 + 500));
            }
        }

        // 将积分展示出来
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g.drawString("长度：" + lenth, 610, 16);
        g.drawString("分数：" + score, 610, 34);

        // 画食物
        Data.food.paintIcon(this, g, foodx, foody);

        // 把小蛇画上去
        // 蛇的头通过方向变量来判断
        if (fx.equals("R")) {
            Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if (fx.equals("L")) {
            Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if (fx.equals("U")) {
            Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
        } else if (fx.equals("D")) {
            Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
        }
        // 蛇的身体动态绘制
        for (int i = 1; i < lenth; i++) {
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);// 蛇的身体长度根据lenth来控制
        }

        // 游戏提示
        if (isStart == false) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
            g.drawString("按下空格键开始游戏！", 210, 300);
        }

        //失败判断
        if (isFail) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("失败, 按下空格重新开始！", 175, 300);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 键盘按下，然后释放。
    }

    // 键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        // 键盘按下，未释放。
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 释放某个键时调用此方法
        int keyCode = e.getKeyCode();// 获取按下的键盘
        if (keyCode == KeyEvent.VK_SPACE) {// 如果是空格
            if (isFail) {// 如果游戏失败，从头再来！
                isFail = false;
                init();// 重新初始化
            } else {// 否则，暂停游戏
                isStart = !isStart;
            }
            repaint();
        }

        // 键盘控制走向
        if (keyCode == KeyEvent.VK_LEFT) {
            fx = (fx == "R") ? fx : "L";
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            fx = (fx == "L") ? fx : "R";
        } else if (keyCode == KeyEvent.VK_UP) {
            fx = (fx == "D") ? fx : "U";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            fx = (fx == "U") ? fx : "D";
        }
    }

    // 定时执行的操作
    @Override
    public void actionPerformed(ActionEvent e) {
        // 如果游戏处于开始状态，则小蛇自动右移
        if (isStart && isFail == false) {
            // 右移：即让后一个移到前一个的位置即可 !
            for (int i = lenth - 1; i > 0; i--) {// 除了脑袋都往前移：身体移动
                snakeX[i] = snakeX[i - 1];// 即第i节(后一节)的位置变为(i-1：前一节)节的位置！
                snakeY[i] = snakeY[i - 1];
            }
            // 通过方向控制，头部移动
            if (fx.equals("R")) {
                snakeX[0] = snakeX[0] + 25;
                if (snakeX[0] > (23 + 750 - 25)) {
                    snakeX[0] = 23;
                }
            } else if (fx.equals("L")) {
                snakeX[0] = snakeX[0] - 25;
                if (snakeX[0] < 23) {
                    snakeX[0] = (23 + 750 - 25);
                }
            } else if (fx.equals("U")) {
                snakeY[0] = snakeY[0] - 25;
                if (snakeY[0] < 41) {
                    snakeY[0] = (41 + 500 - 25);
                }
            } else if (fx.equals("D")) {
                snakeY[0] = snakeY[0] + 25;
                if (snakeY[0] > (41 + 500 - 25)) {
                    snakeY[0] = 41;
                }
            }
            // 吃食物：当蛇的头和食物一样时,算吃到食物！
            if (snakeX[0] == foodx && snakeY[0] == foody) {
                // 1.长度加一
                lenth++;
                // 每吃一个食物，增加积分
                score = score + 10;
                // 2.重新生成食物
                foodx = 23 + 25 * random.nextInt(30 - 1);
                foody = 41 + 25 * random.nextInt(20 - 1);
                // 3.把新增的body坐标填入数组
                if (lenth < 1000) {
                    snakeX[lenth - 1] = snakeX[lenth - 2];
                    snakeY[lenth - 1] = snakeY[lenth - 2];
                } else {
                    isFail = true;
                }
            }
            // 结束判断，头和身体撞到了
            for (int i = 1; i < lenth; i++) {
                // 如果头和身体碰撞，那就说明游戏失败
                if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                    isFail = true;
                }
            }

            repaint();// 需要不断的更新页面实现动画
        }
        timer.start();// 让时间动起来！
    }
}
