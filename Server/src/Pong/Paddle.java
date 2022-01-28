package Pong;

import java.awt.Rectangle;

public class Paddle implements Runnable{
    private Rectangle paddle;

    public Paddle(int x, int y, int id){
        paddle = new Rectangle(x, y, 10, 50);
    }

    public Rectangle getPaddle(){
        return paddle;
    }

    public void setY(int val) {
         paddle.y = val;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(7);
            }
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
}