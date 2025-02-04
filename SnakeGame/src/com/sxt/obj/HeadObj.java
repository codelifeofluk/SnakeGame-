package com.sxt.obj;

import com.sxt.GameWin;
import com.sxt.utils.GameUtils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class HeadObj extends GameObj{

    //方向
    private String direction = "right";

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public HeadObj(Image img, int x, int y, GameWin frame) {
        super(img, x, y, frame);
        //键盘监听
        this.frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e);
            }
        });
    }
    //控制移动方向
    public void changeDirection(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(!"right".equals(direction)){
                    direction = "left";
                    img = GameUtils.leftImg;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(!"left".equals(direction)){
                    direction = "right";
                    img = GameUtils.rightImg;
                }
                break;
            case KeyEvent.VK_UP:
                if(!"down".equals(direction)){
                    direction = "up";
                    img = GameUtils.upImg;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(!"up".equals(direction)){
                    direction = "down";
                    img = GameUtils.downImg;
                }
                break;
            default:
                break;
        }
    }
    public void move(){
        //蛇身移动
        List<BodyObj> bodyObjList = this.frame.bodyObjList;
        for (int i = bodyObjList.size() - 1; i >= 1; i--) {
            bodyObjList.get(i).x = bodyObjList.get(i - 1).x;
            bodyObjList.get(i).y = bodyObjList.get(i - 1).y;
            //身体碰撞
            if (this.x == bodyObjList.get(i).x && this.y == bodyObjList.get(i).y){
                //游戏失败
                GameWin.state = 3;
            }
        }
        bodyObjList.get(0).x = this.x;
        bodyObjList.get(0).y = this.y;
        //蛇头移动
        switch (direction){
            case "up":
                y -= height;
                break;
            case "down":
                y += height;
                break;
            case "left":
                x -= width;
                break;
            case "right":
                x += width;
                break;
            default:
                break;
        }
    }
    //越界处理
    public void OverWin(){
        if (x < 0) x = 570;
        else if (x > 570) x = 30;
        else if (y < 0) y = 570;
        else if (y > 570) y = 30;
    }


    @Override
    public void paintSelf(Graphics g) {
        super.paintSelf(g);
        //蛇吃食物
        FoodObj food = this.frame.foodObj;
        Integer newX = null;
        Integer newY = null;
        if (this.x == food.x && this.y == food.y) {
            this.frame.foodObj = food.getFood();
            //获取蛇身的最后一个元素
            BodyObj lastBody = this.frame.bodyObjList.get(this.frame.bodyObjList.size() - 1);
            newX = lastBody.x;
            newY = lastBody.y;
            this.frame.score++;
        }
        move();
        //move结束后添加新的bodyObj对象到bodyObjList
        if (newX != null && newY != null){
            this.frame.bodyObjList.add(new BodyObj(GameUtils.bodyImg,newX,newY,this.frame));
        }
        OverWin();
    }
}
