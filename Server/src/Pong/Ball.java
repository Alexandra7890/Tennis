package Pong;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Ball implements Runnable {
    private int x, y, xDirection, yDirection;
    private int p1score, p2score;
    private final Rectangle ball;
    private final Paddle p1, p2;
    public boolean flag;
    public boolean flagScore = false;
    public boolean gameOver = false;

    public Ball(int x, int y){
        p1 = new Paddle(0, 290, 1);
        p2 = new Paddle(1190, 290, 2);
        p1score = p2score = 0;
        this.x = x;
        this.y = y;

        //Set ball moving randomly
        Random r = new Random();
        int rXDir = 0;
        rXDir--;
        setXDirection(rXDir);
        int rYDir = 0;
        rYDir--;
        setYDirection(rYDir);

        //create "ball"
        ball = new Rectangle(this.x, this.y, 30, 30);
    }

    public Paddle getP1(){
        return p1;
    }

    public Paddle getP2(){
        return p2;
    }

    public int getScore1(){
        return p1score;
    }

    public int getScore2(){
        return p2score;
    }

    public int getX(){
        return ball.x;
    }

    public int getY() {
        return ball.y;
    }

    public void setXDirection(int xDir){
        xDirection = xDir;
    }

    public void setYDirection(int yDir){
        yDirection = yDir;
    }

    public int getXDirection(){
        return xDirection;
    }

    public int getYDirection(){
        return yDirection;
    }

    public void collision(){
        if(ball.intersects(p1.getPaddle()))
            setXDirection(+1);
        if(ball.intersects(p2.getPaddle()))
            setXDirection(-1);
    }

    public void setPadY1(int val){
        p1.setY(val);
    }

    public void setPadY2(int val){
        p2.setY(val);
    }

    public void move() {
        collision();
        ball.x += xDirection;
        ball.y += yDirection;
        if (ball.x <= 0) {
            setXDirection(+1);
            p2score++;
            flagScore = true;
        }
        if (ball.x >= 1170) {
            setXDirection(-1);
            p1score++;
            flagScore = true;
        }
        if(p2score == 2 || p1score == 2){
            gameOver = true;
        }
        if (ball.y <= 0)
            setYDirection(+1);
        if (ball.y >= 570)
            setYDirection(-1);
    }

    @Override
    public void run() {
        try {
            while(true) {
                move();
                flag = true;
                //отправляем положение мячика
                Thread.sleep(6);
            }
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
}