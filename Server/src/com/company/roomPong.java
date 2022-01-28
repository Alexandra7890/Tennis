package com.company;

import Pong.Pong;
import java.io.IOException;

public class roomPong {
    private final boolean locked;
    private final int key;
    private final Server.clientThreadTCP clientFirstTCP;
    private Server.clientThreadTCP clientSecondTCP = null;
    private Pong pong;
    private final Server server;
    private boolean endGame = false;
    public boolean mesFromLeftPong = false;
    public boolean mesFromRightPong = false;
    public String FromLeftPong = "";
    public String FromRightPong = "";

    public roomPong(Server.clientThreadTCP clientFirstTCP,Server server, boolean locked, int key){
        this.server = server;
        this.key = key;
        this.clientFirstTCP = clientFirstTCP;
        this.locked = locked;
    }

    public Server.clientThreadTCP getClientFirst() {
        return clientFirstTCP;
    }

    public Server.clientThreadTCP getClientSecond() {
        return clientSecondTCP;
    }

    public int getKey() {
        return key;
    }

    public boolean getEndGame(){
        return endGame;
    }

    public boolean isLocked(){
        return locked;
    }

    public void setClientSecond(Server.clientThreadTCP clientSecond){
        this.clientSecondTCP = clientSecond;
    }

    public void startGamePong(){
        pong = new Pong();
        startGamePong st = new startGamePong();
        st.start();
    }

    public void endGame(Server.clientThreadTCP client) throws IOException {
        endGame = true;
        if(client == clientFirstTCP){
            clientSecondTCP.out.println("You win!");
            clientFirstTCP.out.println("You lose!");
            server.OverwritingStats(false, clientFirstTCP);
            server.OverwritingStats(true, clientSecondTCP);
            System.out.println("End game!");
        }
        if(client == clientSecondTCP){
            clientFirstTCP.out.println("You win!");
            clientSecondTCP.out.println("You lose!");
            server.OverwritingStats(true, clientFirstTCP);
            server.OverwritingStats(false, clientSecondTCP);
            System.out.println("End game!");
        }
    }

    class startGamePong extends Thread{
        public void run() {
            while(!endGame){
                try {
                    if(pong.b != null) {
                        if (pong.b.gameOver){
                            if(pong.b.getScore1() == 2)
                                endGame(clientSecondTCP);

                            if(pong.b.getScore2() == 2)
                                endGame(clientFirstTCP);
                        }
                        if (pong.b.flag) {
                            clientFirstTCP.out.println("Ball " + pong.b.getX() + " " + pong.b.getY() + " "
                                    + pong.b.getXDirection() + " " + pong.b.getYDirection() + " ");
                            clientSecondTCP.out.println("Ball " + pong.b.getX() + " " + pong.b.getY() + " "
                                    + pong.b.getXDirection() + " " + pong.b.getYDirection() + " ");
                        }
                        if (pong.b.flagScore) {
                            clientFirstTCP.out.println("Score " + pong.b.getScore1() + " " + pong.b.getScore2());
                            clientSecondTCP.out.println("Score " + pong.b.getScore1() + " " + pong.b.getScore2());
                            pong.b.flagScore = false;
                        }
                        if (mesFromLeftPong) {
                            String[] str = FromLeftPong.split(" ");
                            clientSecondTCP.out.println("Paddle Player1 " + str[2] + " " + str[3]);
                            pong.b.setPadY1(Integer.parseInt(str[2]));
                            mesFromLeftPong = false;
                        }
                        if (mesFromRightPong) {
                            String[] str = FromRightPong.split(" ");
                            clientFirstTCP.out.println("Paddle Player2 " + str[2] + " " + str[3]);
                            pong.b.setPadY2(Integer.parseInt(str[2]));
                            mesFromRightPong = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}