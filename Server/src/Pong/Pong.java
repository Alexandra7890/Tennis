package Pong;

public class Pong{
    public Ball b;//добавил public

    public Pong() {
        b = new Ball(250, 200);
        Thread ball = new Thread(b);
        ball.start();
        Thread p1 = new Thread(b.getP1());
        Thread p2 = new Thread(b.getP2());
        p2.start();
        p1.start();
    }
}