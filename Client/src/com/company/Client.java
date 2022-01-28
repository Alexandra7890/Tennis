package com.company;

import PongOnline.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.Socket;

public class Client {
    private String player = "";
    private Window window;
    private boolean end = false;

    public Client(String args0, String args1){
        //String hostName = args0;
        String hostName = "localhost";
        //int portNumber = Integer.parseInt(args1);
        int portNumber = 9080;
        System.out.println("client started");
        try {
            Socket fromServer = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(fromServer.getOutputStream(), true);
            StreamTCP str = new StreamTCP(fromServer);
            while (!end){//считывание с консоли и отправка на сервер
                Thread.sleep(2);//оставить
                if(window!= null){
                    if(window.registerRequest) {
                        out.println("Registration " + window.getRegisterString());
                        window.registerRequest = false;
                        System.out.println("Registration " + window.getLoginString());
                    }
                    if(window.loginRequest) {
                        String string = "Login " + window.getLoginString();
                        out.println(string);
                        window.loginRequest = false;
                        System.out.println(string);
                    }
                    if(window.statsRequest) {
                        out.println("Stats " + str.login);
                        window.statsRequest = false;
                        System.out.println("Stats " + str.login);
                    }
                    if(window.nickRequest) {
                        out.println("Change " + str.login + " " + window.getNickReqString());
                        window.nickRequest = false;
                        System.out.println("Stats " + str.login + " " + window.getNickReqString());
                    }
                    if(window.quit) {
                        end = true;
                        out.println("Quit");
                        window.quit = false;
                        System.out.println("Quit");
                        System.exit(0);
                    }
                    if(window.logout) {
                        out.println("Logout");
                        window.logout = false;
                        System.out.println("Logout");
                    }
                    if(window.createPublicPong) {
                        out.println("Create public Pong");
                        window.createPublicPong = false;
                        System.out.println("Create public Pong");
                    }
                    if(window.createPrivatePong) {
                        out.println("Create private Pong");
                        window.createPrivatePong = false;
                        System.out.println("Create private Pong");
                    }
                    if(window.connectPong) {
                        out.println("Connect Pong");
                        window.connectPong = false;
                        System.out.println("Connect Pong");
                    }
                    if(window.joinPong) {
                        out.println("join Pong " + window.getKeyRoom());
                        window.joinPong = false;
                        System.out.println("join Pong");
                    }
                    if(player.equals("Left") & this.window.pongOnline != null){
                        if(this.window.pongOnline.b.getP1().movement){
                            out.println("Pong Left " + window.pongOnline.b.getP1().getY()
                                    + " " + window.pongOnline.b.getP1().getYDirection()
                                    + " " + window.pongOnline.getKeyRoom());

                            window.pongOnline.b.getP1().movement = false;
                        }
                    }
                    if(player.equals("Right") & this.window.pongOnline != null){
                        if(window.pongOnline.b.getP2().movement){
                            out.println("Pong Right " + window.pongOnline.b.getP2().getY()
                                    + " " + window.pongOnline.b.getP2().getYDirection()
                                    + " " + window.pongOnline.getKeyRoom());


                            window.pongOnline.b.getP2().movement = false;
                        }
                    }
                    if(window.outGamePong){
                        out.println("Out pong " + window.getKeyRoom());
                        window.outGamePong = false;
                        System.out.println("Out pong!");
                    }
                }
            }
        } catch (IOException | InterruptedException ignored) { }
    }

    class StreamTCP extends Thread{
        private final BufferedReader in;
        private String login = "";

        StreamTCP(Socket socket)  throws IOException{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            start();
        }

        public void run(){
            String message;
            try{
                while (!end) {//считывание с сервера
                    if ((message = in.readLine()) != null) {
                        if (message.equals("Connected")){
                            System.out.println(message);
                            window = new Window();
                        }
                        if (message.contains("Registration ")) {
                            String[] str = message.split(" ");
                            System.out.println(message);
                            if(str[1].equals("false")){
                                window.Error("Login already exists");
                            }
                            if(str[1].equals("true")){
                                login = str[2];
                                window.Menu();
                            }
                        }
                        if (message.contains("Login ")) {
                            String[] str = message.split(" ");
                            System.out.println(message);
                            if(str[1].equals("false")){
                                window.Error("Invalid username or password");
                            }
                            if(str[1].equals("true")){
                                login = str[2];
                                //password = str[3];
                                window.Menu();
                            }
                        }
                        if (message.contains("Error! ")) {
                            System.out.println(message);
                            window.Error(message);
                        }
                        if (message.contains("Stats ")) {
                            window.Stats(message);
                        }
                        if (message.contains("Change ")) {
                            if(message.contains("false")){
                                window.Error("Login already exists!");
                            }
                            if(message.contains("true")) {
                                window.Error("Login changed successfully!");
                                login = window.getNickReqString();
                            }
                        }
                        if (message.contains("Waiting")) {
                            if(message.contains("public")){
                                window.Waiting("Pong");
                                System.out.println("Waiting pong");
                            }
                            if(message.contains("Key")){
                                String[] str = message.split(" ");
                                int keyRoom = Integer.parseInt(str[2]);
                                window.WaitingPrivate("Pong", keyRoom);
                                System.out.println("Waiting pong " + keyRoom);
                            }
                        }

                        //////////////////////////////////////////пинг
                        if (message.contains("Start game Pong online ")) {
                            System.out.println(message);
                            String[] str = message.split(" ");
                            if (message.contains("Right"))
                                player = "Right";
                            if (message.contains("Left"))
                                player = "Left";
                            window.pongOnline = new Pong(player,window, str[5]);
                            window.setKeyRoom(str[5]);
                        }
                        if (message.contains("Ball ")) {
                            String[] coordinate = message.split(" ");
                            window.pongOnline.b.setX(Integer.parseInt(coordinate[1]));
                            window.pongOnline.b.setY(Integer.parseInt(coordinate[2]));
                            window.pongOnline.b.setXDirection(Integer.parseInt(coordinate[3]));
                            window.pongOnline.b.setYDirection(Integer.parseInt(coordinate[4]));
                        }
                        if (message.contains("Score ")){
                            String[] str = message.split(" ");
                            window.pongOnline.b.setP1score(Integer.parseInt(str[1]));
                            window.pongOnline.b.setP2score(Integer.parseInt(str[2]));
                        }
                        if (message.contains("Paddle Player1 ")) {
                            String[] str = message.split(" ");
                            window.pongOnline.b.setPadY1(Integer.parseInt(str[2]));
                            window.pongOnline.b.setPadYDirection1(Integer.parseInt(str[3]));
                        }
                        if (message.contains("Paddle Player2 ")) {
                            String[] str = message.split(" ");
                            window.pongOnline.b.setPadY2(Integer.parseInt(str[2]));
                            window.pongOnline.b.setPadYDirection2(Integer.parseInt(str[3]));
                        }
                        if (message.contains("You win!")){
                            window.PongEndGame("Win");
                            System.out.println(message);
                        }
                        if (message.contains("You lose!")){
                            window.PongEndGame("Lose");
                            System.out.println(message);
                        }
                    }
                }
            }
            catch(IOException ignored){ } catch (UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}