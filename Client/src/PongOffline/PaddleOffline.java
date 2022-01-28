package PongOffline;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PaddleOffline implements Runnable{
    private int yDirection;
    private final int id;
    private final Rectangle paddle, b;
    public boolean flagStop = false;
    private final int difficult;

    public PaddleOffline(int x, int y, int id, Rectangle b, int difficult){
        this.difficult = difficult;
        this.b = b;
        this.id = id;
        paddle = new Rectangle(x, y, 10, 50);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            setYDirection(-1);
        if(e.getKeyCode() == KeyEvent.VK_S)
            setYDirection(1);
    }

    public Rectangle getPaddle(){
        return paddle;
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            setYDirection(0);
        if(e.getKeyCode() == KeyEvent.VK_S)
            setYDirection(0);
    }

    public void setYDirection(int yDir) {
        yDirection = yDir;
    }

    public void move() {
        if(id == 2){
            if(b.y + b.height/2 < paddle.y + paddle.height/2)
                setYDirection(-1);
            if(b.y + b.height/2 > paddle.y + paddle.height/2)
                setYDirection(1);
            if(b.y + b.height/2 == paddle.y + paddle.height/2)
                setYDirection(0);
        }
        paddle.y += yDirection;
        if (paddle.y <= 0)
            paddle.y = 0;
        if (paddle.y >= 550)
            paddle.y = 550;
    }

    @Override
    public void run() {
        try {
            while(!flagStop) {
                move();
                if(id == 2) {
                    if (difficult == 0)
                        Thread.sleep(5);
                    if (difficult == 1) {
                        int rand = (int) (Math.random() * 2);
                        if(rand == 1)
                            Thread.sleep(2);
                        Thread.sleep(2);
                    }
                    if (difficult == 2) {
                        int rand = (int) (Math.random() * 7);
                        if (rand == 1)
                            Thread.sleep(1);
                        Thread.sleep(2);
                    }
                }
                else
                    Thread.sleep(6);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
}