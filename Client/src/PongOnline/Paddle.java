package PongOnline;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Paddle implements Runnable{
    private int yDirection;
    private final String player;
    private final Rectangle paddle;
    public boolean movement = false;
    public boolean flagStop = false;

    public Paddle(int x, int y, String pl){
        player = pl;
        paddle = new Rectangle(x, y, 10, 50);
    }

    public Rectangle getPaddle(){
        return paddle;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W)
            setYDirection(-1);
        if (e.getKeyCode() == KeyEvent.VK_S)
            setYDirection(1);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
            setYDirection(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S)
            setYDirection(0);
    }

    public void setYDirection(int yDir) {
        yDirection = yDir;
    }

    public int getYDirection() {
        return yDirection;
    }

    public void setY(int y) {
        paddle.y = y;
    }

    public int getY() {
        return paddle.y;
    }

    public void move() {
        paddle.y += yDirection;
        if (paddle.y <= 0)
            paddle.y = 0;
        if (paddle.y >= 550)
            paddle.y = 550;
        movement = true;
    }

    @Override
    public void run() {
        try {
            while(!flagStop) {
                Thread.sleep(5);
                move();
            }
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
}