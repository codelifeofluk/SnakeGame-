package com.sxt;

import com.sxt.obj.BodyObj;
import com.sxt.obj.FoodObj;
import com.sxt.obj.HeadObj;
import com.sxt.utils.GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameWin extends JFrame {
    //定义双缓存图片
    Image offScreenImage = null;
    //游戏状态 0.未开始   1.游戏中    2.暂停    3.失败   4.通关    5.失败后重启
    public static int state = 0;
    //分数
    public int score = 0;
    //窗口宽高
    int winWidth = 800;
    int winHeigth = 600;
    //蛇头对象
    HeadObj headObj = new HeadObj(GameUtils.rightImg,30,570,this);
    //食物
    public FoodObj foodObj = new FoodObj().getFood();
    //蛇身的集合
    public List<BodyObj> bodyObjList = new ArrayList<>();
    public void launch(){
        //设置窗口是否可见
        this.setVisible(true);
        //窗口大小
        this.setSize(winWidth,winHeigth);
        //窗口居中
        this.setLocationRelativeTo(null);
        //窗口标题
        this.setTitle("SnakeGame");

        bodyObjList.add(new BodyObj(GameUtils.bodyImg,60,570,this));
        bodyObjList.add(new BodyObj(GameUtils.bodyImg,0,570,this));
        //键盘事件
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    switch (state){
                        case 0:
                            state = 1;
                            break;
                        case 1:
                            state = 2;
                            repaint();
                            break;
                        case 2:
                            state = 1;
                            break;
                        case 3:
                            state = 5;
                            break;
                        default:
                            break;
                    }
                }
            }
        });


        while (true){
            if (state == 1)  repaint();
            //失败重启
            if (state == 5){
                state = 0;
                resetGame();
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void paint(Graphics g) {
        //初始化双缓冲图片
        if (offScreenImage == null){
            offScreenImage = this.createImage(winWidth,winHeigth);
        }
        //获取图片对应的graphics对象
        Graphics gImage = offScreenImage.getGraphics();
        //灰色背景
        gImage.setColor(Color.gray);
        gImage.fillRect(0,0,winWidth,winHeigth);
        //网格线
        gImage.setColor(Color.black);
        for (int i = 0 ; i <= 20 ; i++) {
            gImage.drawLine(0,i * 30,600,i * 30);
            gImage.drawLine(i * 30,0,i * 30,600);
        }
        //绘制蛇身体(反向遍历)
        for (int i = bodyObjList.size() - 1; i >= 0; i--) {
            bodyObjList.get(i).paintSelf(gImage);
        }
        //绘制蛇头
        headObj.paintSelf(gImage);
        foodObj.paintSelf(gImage);
        //分数绘制
        GameUtils.drawWord(gImage,score + "",Color.BLACK,50,650,300);
        gImage.setColor(Color.gray);
        prompt(gImage);
        //将双缓冲图片绘制到窗口
        g.drawImage(offScreenImage,0,0,null);
    }
    //绘制提示语
    void prompt(Graphics g){
        //未开始
        if (state == 0) {
            g.fillRect(120,240,400,70);
            GameUtils.drawWord(g,"按下空格开始游戏",Color.YELLOW,35,150,290);
        }
        if (state == 2) {
            g.fillRect(120, 240, 400, 70);
            GameUtils.drawWord(g, "按下空格继续游戏", Color.YELLOW, 35, 150, 290);
        }
        if (state == 3) {
            g.fillRect(120, 240, 400, 70);
            GameUtils.drawWord(g, "游戏失败", Color.RED, 35, 150, 290);
        }
        }
    void resetGame(){
        //关闭窗口
        this.dispose();
        //新窗口
        String[] args = {};
        main(args);
    }
    public static void main(String[] args){
        GameWin gamewin = new GameWin();
        gamewin.launch();
        JFrame frame = new JFrame();
        // 设置窗口的默认关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
