package PongOnline;

import java.awt.Rectangle;

public class Ball implements Runnable {
    //global variables
    private int xDirection;
    private int yDirection;
    private int p1score, p2score;
    private Rectangle ball;
    private Paddle p1;
    private Paddle p2;

    public Ball(int x, int y){
        p1 = new Paddle(5, 25, "Left");
        p2 = new Paddle(485, 25, "Right");
        p1score = p2score = 0;
        ball = new Rectangle(x, y, 15, 15);
    }

    public Paddle getP1(){
        return p1;
    }

    public Paddle getP2(){
        return p2;
    }

    public int getP1Score(){
        return p1score;
    }

    public int getP2Score(){
        return p2score;
    }

    public void setXDirection(int xDir){
        xDirection = xDir;
    }

    public void setYDirection(int yDir){
        yDirection = yDir;
    }

    public int getX(){
        return ball.x;
    }

    public int getY(){
        return ball.y;
    }

    public void setX(int val){
        ball.x = val;
    }

    public void setY(int val) { ball.y = val; }

    public void setPadY1(int val){
        p1.setY(val);
    }

    public Paddle getP1Paddle() {
        return p1;
    }

    public Paddle getP2Paddle() {
        return p2;
    }

    public void setPadY2(int val){
        p2.setY(val);
    }

    public void setP1score(int p1score) {
        this.p1score = p1score;
    }

    public void setP2score(int p2score) {
        this.p2score = p2score;
    }

    public void setPadYDirection1(int val){
        p1.setYDirection(val);
    }

    public void setPadYDirection2(int val){
        p2.setYDirection(val);
    }

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(8);
            }
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
    }