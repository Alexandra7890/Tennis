package PongOnline;

import com.company.Window;
import java.awt.*;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import static com.company.Paths.pathBall;

public class Pong extends JFrame {
    private String player;
    private final Window window;
    private final Thread p1, p2, ball;
    public Ball b;
    private final JLabel panelBall = new JLabel();
    private final JPanel panel = new JPanel();
    private final AcceptThread at = new AcceptThread();
    public boolean flagStop = false;
    private final String keyRoom;

    public Pong(String pl, Window window, String keyRoom) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        window.playMainMusic("Stop");
        window.playPongMusic("Start");
        this.keyRoom = keyRoom;
        b = new Ball(600, 250);
        this.window = window;
        if (pl.equals("Right"))
            player = "Right";
        if (pl.equals("Left"))
            player = "Left";
        this.setTitle("Pong!");

        window.ClearPanel();
        ball = new Thread(b);
        ball.start();
        p1 = new Thread(b.getP1Paddle());
        p2 = new Thread(b.getP2Paddle());
        p2.start();
        p1.start();
        start();
    }

    public void stop() throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        flagStop = true;
        b.getP1Paddle().flagStop = true;
        b.getP2Paddle().flagStop = true;

        p1.interrupt();
        p1.join();

        p2.interrupt();
        p2.join();

        ball.interrupt();
        ball.join();

        at.interrupt();
        at.join();
    }

    public void start(){
        Color myBlue = new Color(0, 150, 230);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Serif", Font.PLAIN, 30));
        btnBack.setBounds(650,750,200,60);
        btnBack.setBackground(myBlue);
        window.add(btnBack);

        setComponentForPanel(pathBall, 30,30,0,0,panelBall);

        panel.setLayout(null);
        panel.setBounds(160,100,1200,600);
        panel.setBackground(Color.DARK_GRAY);
        window.add(panel);
        panel.add(panelBall);

        at.start();

        btnBack.addActionListener(e -> {
            try {
                window.outGamePong = true;
                stop();
            } catch (InterruptedException | LineUnavailableException | IOException | UnsupportedAudioFileException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        window.SetBackground();
        window.repaint();
    }

    public String getKeyRoom() {
        return keyRoom;
    }

    class AcceptThread extends Thread {
        public void run() {
            JPanel panel1 = new JPanel();
            panel1.setBounds(0,100,10,50);
            panel1.setBackground(Color.magenta);
            panel.add(panel1);

            JPanel panel2 = new JPanel();
            panel2.setBounds(1275,100,10,50);
            panel2.setBackground(Color.YELLOW);
            panel.add(panel2);

            JLabel score1 = new JLabel("0");
            score1.setBounds(400,285,50,30);
            score1.setFont(new Font("Serif", Font.PLAIN, 30));
            score1.setForeground(Color.WHITE);
            panel.add(score1);

            JLabel score2 = new JLabel("0");
            score2.setBounds(800,285,50,30);
            score2.setFont(new Font("Serif", Font.PLAIN, 30));
            score2.setForeground(Color.WHITE);
            panel.add(score2);

            panel1.setBounds(0, b.getP1Paddle().getPaddle().y,10,50);
            panel2.setBounds(1190, b.getP2Paddle().getPaddle().y,10,50);

            window.repaint();
            while (!flagStop){
                try {
                    if(isInterrupted())
                        Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                panel1.setBounds(0, b.getP1Paddle().getPaddle().y,10,50);
                panel2.setBounds(1190, b.getP2Paddle().getPaddle().y,10,50);
                score1.setText(Integer.toString(b.getP1Score()));
                score2.setText(Integer.toString(b.getP2Score()));
                panelBall.setBounds(b.getX(), b.getY(),30,30);
                panel.repaint();
            }
        }
    }

    public void setComponentForPanel(String path, int width, int height, int x, int y, JLabel component){
        Image im = new ImageIcon(path).getImage();
        im = im.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(im);
        component.setBounds(x,y,width,height);
        component.setIcon(image);
        image.setImageObserver(component);
    }

    public String getPlayer() {
        return player;
    }
}