package PongOffline;

import java.awt.Rectangle;

public class BallOffline implements Runnable {
    private int xDirection;
    private int yDirection;
    private int p1score, p2score;
    private final PaddleOffline p1, p2;
    private final Rectangle ball;
    private final int difficult;

    public BallOffline(int x, int y, int difficult){
        this.difficult = difficult;
        p1score = p2score = 0;

        setXDirection(1);
        setYDirection(-1);

        //create "ball"
        ball = new Rectangle(x, y, 30, 30);
        p1 = new PaddleOffline(0, 290, 1, ball, difficult);
        p2 = new PaddleOffline(1190, 290, 2, ball,difficult);
    }

    public void setXDirection(int xDir){
        xDirection = xDir;
    }

    public void setYDirection(int yDir){
        yDirection = yDir;
    }

    public PaddleOffline getP1Paddle(){
        return p1;
    }

    public PaddleOffline getP2Paddle(){
        return p2;
    }

    public int getP1score() {
        return p1score;
    }

    public int getP2score() {
        return p2score;
    }

    public int getX(){
        return  ball.x;
    }

    public int getY(){
        return  ball.y;
    }

    public void collision(){
        if(ball.intersects(p1.getPaddle())){
            setXDirection(1);
        }
        if(ball.intersects(p2.getPaddle())){
            setXDirection(-1);
        }
    }

    public void move() {
        collision();
        ball.x += xDirection;
        ball.y += yDirection;
        if (ball.x <= 0) {
            setXDirection(+1);
            p2score++;
        }
        if (ball.x >= 1170) {
            setXDirection(-1);
            p1score++;
        }
        if (ball.y <= 0)
            setYDirection(+1);
        if (ball.y >= 570)
            setYDirection(-1);
    }

    @Override
    public void run() {
        try {
            while(!p1.flagStop) {
                move();
                if(difficult == 0)
                    Thread.sleep(4);
                if(difficult == 1)
                    Thread.sleep(3);
                if(difficult == 2)
                    Thread.sleep(2);
            }
        }catch(Exception e) { System.err.println(e.getMessage()); }

    }

}