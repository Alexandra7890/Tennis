package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocketTCP;
    private final ArrayList<clientThreadTCP> users;
    private final ArrayList<roomPong> roomPongs;
    private final FileWriter fileLog;
    private final FileWriter fileUsers;
    private final String pathFileUsers;
    private int keyRoomPong = 0;
    public ArrayList<String> onlineUsers;

    public Server(int port) throws IOException {
        String pathFileLog = "C:\\Users\\Alexandra\\Desktop\\kursach\\Kursach\\Server\\logFile.txt";
        pathFileUsers = "C:\\Users\\Alexandra\\Desktop\\kursach\\Kursach\\Server\\Users.txt";
        fileLog = new FileWriter(pathFileLog, false);
        fileUsers = new FileWriter(pathFileUsers, true);
        serverSocketTCP = new ServerSocket(port);
        onlineUsers = new ArrayList<>();
        users = new ArrayList<>();
        roomPongs = new ArrayList<>();
        fileLog.write("Server started\n");
        fileLog.flush();
        System.out.println("Server started");
        ServerStart(this);
    }

    public void ServerStart(Server server) {
        class AcceptThread extends Thread {
            public void run() {
                while (true) {
                    try {
                        Socket clientSocket = serverSocketTCP.accept();
                        clientThreadTCP cliThread = new clientThreadTCP(clientSocket, server);
                        synchronized (users) {
                            users.add(cliThread);
                            cliThread.out.println("Connected");
                            System.out.println(users.size());
                        }
                        fileLog.write("Client connected\n");
                        fileLog.flush();
                        cliThread.start();
                    } catch (IOException ioe) {
                        try {
                            fileLog.write("Error to connect client!\n");
                            fileLog.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        AcceptThread at = new AcceptThread();
        at.start();
    }

    public synchronized void OverwritingStats(boolean win, clientThreadTCP player1) throws IOException {
        Scanner sc = new Scanner(new FileInputStream(pathFileUsers));
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            String st = sc.nextLine();
            String[] mass = st.split(" ");
            if (player1.login.equals(mass[0])) {
                if (win)
                    mass[2] = Integer.toString(Integer.parseInt(mass[2]) + 1);
                if (!win)
                    mass[3] = Integer.toString(Integer.parseInt(mass[3]) + 1);
            }
            sb.append(mass[0]).append(" ").append(mass[1]).append(" ").append(mass[2]).append(" ")
                    .append(mass[3]).append("\n");
            System.out.println(sb.toString());
        }
        clearTheFile();
        FileWriter wr = new FileWriter(pathFileUsers, true);
        wr.write(sb.toString());
        wr.flush();
    }

    public void Registration(String message, clientThreadTCP client) throws IOException {
        String[] str = message.split(" ");
        String login = str[1];
        String password = str[2];
        Scanner sc = new Scanner(new FileInputStream(pathFileUsers));
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            str = tmp.split(" ");
            if (str[0].equals(login)) {
                fileLog.write("Registration false\n");
                fileLog.flush();
                client.out.println("Registration false");
                System.out.println("Reg false");
                return;
            }
        }
        fileUsers.write(login + " " + password + " 0 " + "0\n");
        fileUsers.flush();
        fileLog.write("Registration true " + login + " " + password + " 0 " + "0\n");
        fileLog.flush();
        client.out.println("Registration true " + login);
        client.login = login;
        onlineUsers.add(login);
    }

    public void Login(String message, clientThreadTCP client) throws IOException {
        String[] str = message.split(" ");
        String login = str[1];
        String password = str[2];
        Scanner sc = new Scanner(new FileInputStream(pathFileUsers));
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            str = tmp.split(" ");
            if (str[0].equals(login) && str[1].equals(password)) {
                if (onlineUsers.contains(login)) {
                    fileLog.write("Error! Client " + login + " online!\n");
                    fileLog.flush();
                    client.out.println("Error! Client online!");
                    System.out.println("Error! Client online!");
                    return;
                }
                client.login = login;
                onlineUsers.add(login);
                System.out.println("Add to online : " + onlineUsers.toString());
                fileLog.write("Login true " + login + " " + password + "\n");
                fileLog.flush();
                client.out.println("Login true " + login + " " + password);
                System.out.println("Login true " + login + " " + password);
                return;
            }
        }
        fileLog.write("Login false " + login + " " + password + "\n");
        fileLog.flush();
        client.out.println("Login false");
        System.out.println("Log false");
    }

    public void Stats(String message, clientThreadTCP client) throws IOException {
        String[] str = message.split(" ");
        String login = str[1];
        Scanner sc = new Scanner(new FileInputStream(pathFileUsers));
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            System.out.println(Arrays.toString(str));
            str = tmp.split(" ");
            if (str[0].equals(login)) {
                fileLog.write("Stats request\n");
                fileLog.flush();
                client.out.println("Stats " + tmp);
                System.out.println("Stats " + tmp);
                return;
            }
        }
        System.out.println("Stats false");
    }

    private void changeNick(String message, clientThreadTCP client) throws IOException {
        String[] str = message.split(" ");
        String oldLogin = str[1];
        String newLogin = str[2];
        Scanner sc = new Scanner(new FileInputStream(pathFileUsers));
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            String stud = sc.nextLine();
            String[] mass = stud.split(" ");
            if (newLogin.equals(mass[0])) {
                fileLog.write("Change nickname - " + oldLogin + " false\n");
                fileLog.flush();
                client.out.println("Change false");
                return;
            }
        }
        sc = new Scanner(new FileInputStream(pathFileUsers));
        while (sc.hasNextLine()) {
            String stud = sc.nextLine();
            String[] mass = stud.split(" ");
            if (oldLogin.equals(mass[0]))
                mass[0] = newLogin;
            sb.append(mass[0]).append(" ").append(mass[1]).append(" ").append(mass[2]).append(" ").append(mass[3]).append("\n");
        }
        clearTheFile();
        FileWriter wr = new FileWriter(pathFileUsers, true);
        wr.write(sb.toString());
        wr.flush();
        fileLog.write("Change nickname - " + oldLogin + " true\n");
        fileLog.flush();
        client.out.println("Change true");
    }

    public void clearTheFile() throws IOException {
        FileWriter fwOb = new FileWriter(pathFileUsers, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

    public synchronized void deleteClient() throws IOException {
        users.removeIf(ct -> ct.socket.isClosed());
        System.out.println(users.size());
    }

    public class clientThreadTCP extends Thread {
        Socket socket;
        Server server;
        PrintWriter out;
        BufferedReader in;
        String login;

        clientThreadTCP(Socket s, Server server) throws IOException {
            this.server = server;
            socket = s;
            System.out.println("client connected " + s.getPort());
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public void run() {
            String message;
            while (true) {
                try {
                    Thread.sleep(3);
                    if ((message = in.readLine()) != null) {
                        if (message.contains("Registration")) {
                            System.out.println(message);
                            Registration(message, this);
                        }
                        if (message.contains("Login")) {
                            System.out.println(message);
                            Login(message, this);
                        }
                        if (message.contains("Stats")) {
                            System.out.println(message);
                            Stats(message, this);
                        }
                        if (message.contains("Change")) {
                            System.out.println(message);
                            changeNick(message, this);
                        }
                        if (message.equals("Logout")) {
                            fileLog.write("Client logout\n");
                            fileLog.flush();
                            System.out.println("Client logout");
                            onlineUsers.remove(login);
                            System.out.println("Deleted from online : " + onlineUsers.toString());
                        }
                        if (message.equals("Quit")) {
                            fileLog.write("Client quit\n");
                            fileLog.flush();
                            System.out.println("Client quit\n");
                            onlineUsers.remove(login);
                            System.out.println("Deleted from online : " + onlineUsers.toString());
                            socket.close();
                            deleteClient();
                        }
                        if (message.contains("Create")) {
                            if (message.contains("public")) {
                                if (message.contains("Pong")) {
                                    fileLog.write("Created Pong public room\n");
                                    fileLog.flush();
                                    roomPongs.add(new roomPong(this, server, false, keyRoomPong++));
                                    out.println("Waiting public");
                                    System.out.println("Create public Pong");
                                }
                            }
                            if (message.contains("private")) {
                                if (message.contains("Pong")) {
                                    roomPong rm = new roomPong(this, server, true, keyRoomPong++);
                                    roomPongs.add(rm);
                                    out.println("Waiting! Key " + rm.getKey());
                                    System.out.println("Create private Pong");
                                }
                            }
                        }
                        if (message.contains("Connect")) {
                            if (message.contains("Pong")) {
                                System.out.println("Connect Pong");
                                boolean flagFind = false;
                                for (roomPong room : roomPongs) {
                                    if (room.getClientFirst() == null & room.getClientSecond() == null)
                                        roomPongs.remove(room);
                                    if (room.getClientFirst() != null & room.getClientSecond() == null
                                            & !room.isLocked() & !room.getEndGame()) {
                                        room.setClientSecond(this);
                                        flagFind = true;
                                        room.getClientFirst().out.println("Start game Pong online Left " + room.getKey());//
                                        room.getClientSecond().out.println("Start game Pong online Right " + room.getKey());//
                                        room.startGamePong();
                                        System.out.println("startGame " + room.getKey());
                                        break;
                                    }
                                }
                                if (!flagFind) {
                                    fileLog.write("Error! Room didn't find!\n");
                                    fileLog.flush();
                                    out.println("Error! Room didn't find!");
                                    System.out.println("Error! Room didn't find!");
                                }
                            }
                            out.println("Waiting");
                        }
                        if (message.contains("join")) {
                            if (message.contains("Pong")) {
                                System.out.println("Join Pong");
                                String[] str = message.split(" ");
                                boolean flagFind = false;
                                for (roomPong room : roomPongs) {
                                    if (room.getClientFirst() == null & room.getClientSecond() == null)
                                        roomPongs.remove(room);
                                    if (room.getClientFirst() != null & room.getClientSecond() == null
                                            & room.isLocked() & !room.getEndGame() & room.getKey() == Integer.parseInt(str[2])) {
                                        room.setClientSecond(this);
                                        flagFind = true;
                                        room.getClientFirst().out.println("Start game Pong online Left " + room.getKey());//
                                        room.getClientSecond().out.println("Start game Pong online Right " + room.getKey());//
                                        room.startGamePong();
                                        System.out.println("startGame " + room.getKey());
                                        break;
                                    }
                                }
                                if (!flagFind) {
                                    fileLog.write("Error! Room didn't find!\n");
                                    fileLog.flush();
                                    out.println("Error! Room didn't find!");
                                    System.out.println("Error! Room didn't find!");
                                }
                            }
                            out.println("Waiting");
                        }
                        if (message.contains("Pong Left")) {
                            String[] str = message.split(" ");
                            int keyRoom = Integer.parseInt(str[4]);
                            for (roomPong room : roomPongs) {
                                if (room.getKey() == keyRoom) {
                                    room.FromLeftPong = message;
                                    room.mesFromLeftPong = true;
                                }
                            }
                        }
                        if (message.contains("Pong Right")) {
                            String[] str = message.split(" ");
                            int keyRoom = Integer.parseInt(str[4]);
                            for (roomPong room : roomPongs) {
                                if (room.getKey() == keyRoom) {
                                    room.FromRightPong = message;
                                    room.mesFromRightPong = true;
                                }
                            }
                        }
                        if (message.contains("Out pong")) {
                            String[] str = message.split(" ");
                            int keyRoom = Integer.parseInt(str[2]);
                            System.out.println(keyRoom);
                            for (roomPong room : roomPongs) {
                                if (room.getKey() == keyRoom)
                                    room.endGame(this);
                            }
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    try {
                        onlineUsers.remove(login);
                        socket.close();
                        deleteClient();
                        break;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}
