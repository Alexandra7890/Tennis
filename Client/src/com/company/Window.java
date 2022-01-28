package com.company;

import PongOnline.*;
import PongOffline.PongOffline;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static com.company.Paths.*;

public class Window extends JFrame implements KeyListener {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private final Color myBlue;
    private Clip clipMainSound;
    private Clip clipStatsSound;
    private Clip clipPongSound;
    public PongOffline pongOffline;
    public Pong pongOnline;
    private String registerString = "";
    private String loginString = "";
    private String nickReqString = "";
    private String keyRoom = "";
    public boolean registerRequest = false;
    public boolean logout = false;
    public boolean loginRequest = false;
    public boolean statsRequest = false;
    public boolean nickRequest = false;
    public boolean createPrivatePong = false;
    public boolean createPublicPong = false;
    public boolean connectPong = false;
    public boolean joinPong = false;
    public boolean outGamePong = false;

    public boolean quit = false;

    public Window() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();
        myBlue = new Color(0, 150, 230);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setUndecorated(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        playMainMusic("Start");
        LoginRegistration();
    }

    public void ClearPanel(){
        getContentPane().removeAll();
        repaint();
    }

    public void LoginRegistration(){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,100,300,300);
        add(Pong);

        JButton btnLogin = new JButton("Login");
        CreateBtn(btnLogin,390,400,350,70);

        JButton btnRegister = new JButton("Register");
        CreateBtn(btnRegister,810,400,350,70);

        JButton btnQuit = new JButton("Exit");
        CreateBtn(btnQuit,600,600,350,70);

        SetBackground();

        btnLogin.addActionListener(e -> Login());

        btnQuit.addActionListener(e -> Quit());

        btnRegister.addActionListener(e -> Registration());

        //Показать фрейм
        setVisible(true);
        repaint();
    }

    private void Login() {
        ClearPanel();

        JLabel nameStr = new JLabel("Name:");
        nameStr.setFont(new Font("Serif", Font.PLAIN, 50));
        nameStr.setBounds(510, 350, 250, 100);
        add(nameStr);

        JLabel passwordStr = new JLabel("Password:");
        passwordStr.setFont(new Font("Serif", Font.PLAIN, 50));
        passwordStr.setBounds(510, 450, 250, 50);
        add(passwordStr);

        JTextField login = new JTextField();
        login.setBounds(510+250, 390, 250, 30);
        add(login);

        JPasswordField password = new JPasswordField();
        password.setBounds(510+250, 470, 250, 30);
        password.setEchoChar('*');
        add(password);

        JButton btnBack = new JButton("Close");
        CreateBtn(btnBack,504,600,244,72);

        JButton btnGo = new JButton("Ok");
        CreateBtn(btnGo,764,600,244,72);

        btnBack.addActionListener(e -> LoginRegistration());

        btnGo.addActionListener(e -> {
            String log = login.getText();
            String pass = new String(password.getPassword());
            if(log.length() > 15){
                Error("Login must not exceed 15 characters!");
            }
            else if(pass.length() > 15){
                Error("Password must not exceed 15 characters!");
            }
            else if(log.length() < 5){
                Error("Login must be more than 4 characters!");
            }
            else if(pass.length() < 5){
                Error("Password must be more than 4 characters!");
            }
            else{
                loginString = log + " " + pass;
                loginRequest = true;
            }
        });

        SetBackground();
        repaint();
    }

    private void Registration() {
        ClearPanel();

        JLabel nameStr = new JLabel("Name:");
        nameStr.setFont(new Font("Serif", Font.PLAIN, 50));
        nameStr.setBounds(510, 350, 250, 100);
        add(nameStr);

        JLabel passwordStr = new JLabel("Password:");
        passwordStr.setFont(new Font("Serif", Font.PLAIN, 50));
        passwordStr.setBounds(510, 450, 250, 50);
        add(passwordStr);

        JTextField login = new JTextField();
        login.setBounds(510+250, 390, 250, 30);
        add(login);

        JPasswordField password = new JPasswordField();
        password.setBounds(510+250, 470, 250, 30);
        password.setEchoChar('*');
        add(password);

        JButton btnBack = new JButton("Close");
        btnBack.setFont(new Font("Serif", Font.PLAIN, 30));
        btnBack.setBounds(504,600,244,72);
        btnBack.setBackground(myBlue);
        add(btnBack);

        JButton btnSave = new JButton("Save");
        btnSave.setFont(new Font("Serif", Font.PLAIN, 30));
        btnSave.setBounds(764,600,244,72);
        btnSave.setBackground(myBlue);
        add(btnSave);

        btnBack.addActionListener(e -> LoginRegistration());

        btnSave.addActionListener(e -> {
            String log = login.getText();
            if(log.length() > 15){
                System.out.println(log.length());
                Error("Login must not exceed 15 characters!");
            }
            char[] reg = password.getPassword();
            if(reg.length > 15){
                Error("Password must not exceed 15 characters!");
            }
            else if(log.length() < 5){
                Error("Login must be more than 4 characters!");
            }
            else {
                StringBuilder tmp = new StringBuilder();
                for (char c : reg)
                    tmp.append(c);
                registerString = log + " " + tmp.toString();
                registerRequest = true;
            }
        });

        SetBackground();
        repaint();
    }

    public void SetBackground(){
        setLayout(null);
        ImageIcon img = new ImageIcon(pathBackground);
        JLabel background = new JLabel("", img,JLabel.CENTER);
        background.setBounds(0,0,1550,900);
        add(background);
    }

    public void Stats(String stats){
        ClearPanel();

        String[] str = stats.split(" ");

        JLabel pongWin = new JLabel("Win : " + str[3]);
        pongWin.setFont(new Font("Serif", Font.PLAIN, 60));
        pongWin.setBounds(365,350,600,60);
        add(pongWin);

        JLabel pongLose = new JLabel("Lose : " + str[4]);
        pongLose.setFont(new Font("Serif", Font.PLAIN, 60));
        pongLose.setBounds(960,350,600,60);
        add(pongLose);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,665,625,200,80);

        btnBack.addActionListener(e -> Menu());

        btnBack.addActionListener(e -> {
            statsRequest = true;
            try {
                playStatsMusic("Stop");
                playMainMusic("Start");
                Menu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                ioException.printStackTrace();
            }
        });
        SetBackground();
        repaint();
    }

    public void Menu(){
        ClearPanel();

        JButton btnPong = new JButton("Tennis");
        CreateBtn(btnPong,560,150,404,114);

        JButton btnStats = new JButton("Stats");
        CreateBtn(btnStats,130,370,404,114);

        JButton btnChangeName = new JButton("Rename");
        CreateBtn(btnChangeName,560,370,404,114);

        JButton btnLogout = new JButton("Logout");
        CreateBtn(btnLogout,990,370,404,114);

        JButton btnQuit = new JButton("Exit");
        CreateBtn(btnQuit,560,550,404,114);

        btnPong.addActionListener(e -> OnlineOffline("Pong"));

        btnStats.addActionListener(e -> {
            statsRequest = true;
            try {
                playMainMusic("Stop");
                playStatsMusic("Start");
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                ioException.printStackTrace();
            }
        });

        btnChangeName.addActionListener(e -> ChangeLogin());

        btnLogout.addActionListener(e -> {
            logout = true;
            LoginRegistration();
        });

        btnQuit.addActionListener(e -> Quit());

        SetBackground();
        repaint();
    }

    public void OnlineOffline(String nameOfGame){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JButton btnOnline = new JButton("Online");
        CreateBtn(btnOnline,560,300,404,114);

        JButton btnOffline = new JButton("Offline");
        CreateBtn(btnOffline,560,425,404,114);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,560,600,404,114);

        add(btnOnline);
        add(btnOffline);

        btnOnline.addActionListener(e -> StartLobby(nameOfGame));

        btnOffline.addActionListener(e -> {
//            try {
                if (nameOfGame.equals("Pong")) {
                    BotDifficulty();

                }
//            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
//                ioException.printStackTrace();
//            }
        });

        btnBack.addActionListener(e -> Menu());
        SetBackground();
        repaint();
    }

    public void BotDifficulty(){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JLabel jLabelAILevel = new JLabel("Выберете уровень AI:");
        jLabelAILevel.setFont(new Font("Verdana", Font.PLAIN, 50));
        jLabelAILevel.setBounds(500,320,800,100);
        add(jLabelAILevel);

        JRadioButton radioBtnEasy = new JRadioButton("Easy");
        radioBtnEasy.setFont(new Font("Verdana", Font.PLAIN, 50));
        radioBtnEasy.setBounds(700,420,400,100);
        radioBtnEasy.setOpaque(false);
        add(radioBtnEasy);
        radioBtnEasy.setSelected(true);

        JRadioButton radioBtnMedium = new JRadioButton("Medium");
        radioBtnMedium.setFont(new Font("Verdana", Font.PLAIN, 50));
        radioBtnMedium.setBounds(700,520,400,100);
        radioBtnMedium.setOpaque(false);
        add(radioBtnMedium);
        radioBtnMedium.setSelected(false);

        JRadioButton radioBtnHard = new JRadioButton("Hard");
        radioBtnHard.setFont(new Font("Verdana", Font.PLAIN, 50));
        radioBtnHard.setBounds(700,620,400,100);
        radioBtnHard.setOpaque(false);
        add(radioBtnHard);
        radioBtnHard.setSelected(false);

        final int[] level = {0};

        radioBtnEasy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnEasy.isSelected()) {
                    radioBtnMedium.setSelected(false);
                    radioBtnHard.setSelected(false);
                    level[0] = 0;
                }
            }
        });

        radioBtnMedium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnMedium.isSelected()) {
                    radioBtnEasy.setSelected(false);
                    radioBtnHard.setSelected(false);
                    level[0] = 1;

                }
            }
        });

        radioBtnHard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnHard.isSelected()) {
                    radioBtnEasy.setSelected(false);
                    radioBtnMedium.setSelected(false);
                    level[0] = 2;

                }
            }
        });


        JButton btnPlay = new JButton("Play");
        CreateBtn(btnPlay,835,720,250,100);

        btnPlay.addActionListener(e -> {
            try {
                playMainMusic("Stop");
                playPongMusic("Start");
                pongOffline = null;
                pongOffline = new PongOffline(this, level[0]);
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                ioException.printStackTrace();
            }
        });

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,485,720,250,100);

        btnBack.addActionListener(e -> {
            OnlineOffline("Pong");
        });
        SetBackground();
        repaint();
    }

    public void StartLobby(String nameOfGame){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JButton btnConnect = new JButton("Connect");
        CreateBtn(btnConnect,560,300,404,114);

        JButton btnCreate = new JButton("Create");
        CreateBtn(btnCreate,560,425,404,114);

        JButton btnJoin = new JButton("Join");
        CreateBtn(btnJoin,560,550,404,114);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,560,675,404,114);

        btnConnect.addActionListener(e -> ConnectLobby(nameOfGame));

        btnCreate.addActionListener(e -> PublicPrivate(nameOfGame));

        btnJoin.addActionListener(e -> JoinLobby(nameOfGame));

        btnBack.addActionListener(e -> OnlineOffline(nameOfGame));

        SetBackground();
        repaint();
    }

    public void ConnectLobby(String nameOfGame){
        if(nameOfGame.equals("Pong"))
            connectPong = true;
    }

    public void PublicPrivate(String nameOfGame){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JButton btnPrivate = new JButton("Private");
        CreateBtn(btnPrivate,560,350,404,114);

        JButton btnPublic = new JButton("Public");
        CreateBtn(btnPublic,560,500,404,114);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,560,650,404,114);

        btnBack.addActionListener(e -> StartLobby(nameOfGame));

        btnPrivate.addActionListener(e -> PrivateRoom(nameOfGame));

        btnPublic.addActionListener(e -> PublicRoom(nameOfGame));

        SetBackground();
        repaint();
    }

    public void PrivateRoom(String nameOfGame){
        if(nameOfGame.equals("Pong")){
            createPrivatePong = true;
        }
    }

    public void PublicRoom(String nameOfGame){
        if(nameOfGame.equals("Pong"))
            createPublicPong = true;
    }

    public void JoinLobby(String nameOfGame){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JLabel nameStr = new JLabel("Room key:");
        nameStr.setFont(new Font("Serif", Font.PLAIN, 50));
        nameStr.setBounds(250, 350, 350, 100);
        add(nameStr);

        JTextField inputKeyRoom = new JTextField();
        inputKeyRoom.setBounds(650, 390, 250, 30);
        add(inputKeyRoom);

        JButton btnJoin = new JButton("Join");
        CreateBtn(btnJoin,1000,380,244,72);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,560,625,404,114);

        btnJoin.addActionListener(e -> {
            keyRoom = inputKeyRoom.getText();
            joinPong = true;
            System.out.println("Join = " + keyRoom);
        });

        btnBack.addActionListener(e -> StartLobby("Pong"));
        SetBackground();
        repaint();
    }

    public void ChangeLogin(){
        ClearPanel();

        JLabel Pong = new JLabel("Tennis");
        Pong.setFont(new Font("Verdana", Font.PLAIN, 90));
        Pong.setBounds(620,50,300,300);
        add(Pong);

        JLabel nameStr = new JLabel("New nickname:");
        nameStr.setFont(new Font("Serif", Font.PLAIN, 50));
        nameStr.setBounds(250, 350, 350, 100);
        add(nameStr);

        JTextField login = new JTextField();
        login.setBounds(650, 390, 250, 30);
        add(login);

        JButton btnSave = new JButton("Save");
        CreateBtn(btnSave,1000,380,244,72);

        JButton btnBack = new JButton("Back");
        CreateBtn(btnBack,560,625,404,114);

        btnSave.addActionListener(e -> {
            String log = login.getText();
            if(log.length() > 15){
                Error("Login must not exceed 15 characters!");
            }
            else if(log.length() < 5){
                Error("Login must be more than 4 characters!");
            }
            else{
                nickReqString = log;
                nickRequest = true;
                System.out.println("log = " + log);
            }
        });

        btnBack.addActionListener(e -> Menu());
        SetBackground();
        repaint();
    }

    public void Waiting(String game){
        ClearPanel();
        JLabel waiting = new JLabel("Waiting opponent...");
        waiting.setFont(new Font("Serif", Font.PLAIN, 80));
        waiting.setBounds(450,400,800,120);
        add(waiting);

        JButton back = new JButton("Back");
        CreateBtn(back,560,650,404,114);

        back.addActionListener(e -> PublicPrivate(game));
        SetBackground();
        repaint();
    }

    public void WaitingPrivate(String game, int key){
        ClearPanel();
        JLabel waiting = new JLabel("Waiting opponent...");
        waiting.setFont(new Font("Serif", Font.PLAIN, 80));
        waiting.setBounds(450,250,800,100);
        add(waiting);

        JLabel keyRoom = new JLabel("Your room key: " + key);
        keyRoom.setFont(new Font("Serif", Font.PLAIN, 80));
        keyRoom.setBounds(500,450,800,100);
        add(keyRoom);

        JButton back = new JButton("Back");
        CreateBtn(back,560,650,404,114);

        back.addActionListener(e -> PublicPrivate(game));
        SetBackground();
        repaint();
    }

    public void Quit(){
        quit = true;
        dispose();
    }

    public void Error(String error){
        JFrame err = new JFrame();
        err.setBounds(575,350,350,150);
        JLabel textError = new JLabel(error);

        textError.setFont(new Font("Serif", Font.PLAIN, 15));
        textError.setBounds(50,10,300,40);
        err.add(textError);

        JButton btnOk = new JButton("OK");
        btnOk.setFont(new Font("Serif", Font.PLAIN, 15));
        btnOk.setBounds(120,60,100,40);
        btnOk.setBackground(myBlue);
        err.add(btnOk);

        btnOk.addActionListener(e -> err.dispose());

        err.setLayout(null);
        err.setVisible(true);
    }

    public void CreateBtn(JButton btn, int x, int y, int w, int h){
        btn.setBackground(myBlue);
        btn.setFont(new Font("Verdana", Font.PLAIN, 50));//Calibri , Serief
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBounds(x,y,w,h);
        add(btn);
    }

    public String getRegisterString() {
        return registerString;
    }

    public String getLoginString() {
        return loginString;
    }

    public String getKeyRoom(){
        return keyRoom;
    }

    public void setKeyRoom(String key){
         keyRoom = key;
    }

    public String getNickReqString(){ return nickReqString;}

    public void playMainMusic(String state) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if(state.equals("Start")){
            File f = new File(Paths.pathMainMusic);
            DataLine.Info info = new DataLine.Info(Clip.class, AudioSystem.getAudioFileFormat(f).getFormat());
            clipMainSound = (Clip) AudioSystem.getLine(info);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clipMainSound.open(ais);

            FloatControl gainControl = (FloatControl) clipMainSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(0.1f));

            clipMainSound.start();
            clipMainSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        if(state.equals("Stop")){
            clipMainSound.stop();
            clipMainSound.setFramePosition(0);
        }
    }

    public void playStatsMusic(String state) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if(state.equals("Start")){
            File f = new File(pathStatsMusic);
            DataLine.Info info = new DataLine.Info(Clip.class, AudioSystem.getAudioFileFormat(f).getFormat());
            clipStatsSound = (Clip) AudioSystem.getLine(info);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clipStatsSound.open(ais);

            FloatControl gainControl = (FloatControl) clipStatsSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(0.2f));

            clipStatsSound.start();
            clipStatsSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        if(state.equals("Stop")){
            clipStatsSound.stop();
            clipStatsSound.setFramePosition(0);
        }
    }

    public void playPongMusic(String state) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if(state.equals("Start")){
            File f = new File(pathPongMusic);
            DataLine.Info info = new DataLine.Info(Clip.class, AudioSystem.getAudioFileFormat(f).getFormat());
            clipPongSound = (Clip) AudioSystem.getLine(info);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clipPongSound.open(ais);

            FloatControl gainControl = (FloatControl) clipPongSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(0.2f));

            clipPongSound.start();
            clipPongSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        if(state.equals("Stop")){
            clipPongSound.stop();
            clipPongSound.setFramePosition(0);
        }
    }

    public void PongEndGame(String result) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        ClearPanel();
        pongOnline.stop();
        JLabel label;
        if(result.equals("Win"))
            label = new JLabel("You win!");
        else
            label = new JLabel("You lose!");
        label.setFont(new Font("Verdana", Font.PLAIN, 90));
        label.setBounds(550,200,500,500);
        label.setLayout(null);
        add(label);
        JButton ok = new JButton("OK");
        CreateBtn(ok,560,600,404,114);
        add(ok);

        ok.addActionListener(e -> {
            try {
                playPongMusic("Stop");
                playMainMusic("Start");
                OnlineOffline("Pong");
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                ioException.printStackTrace();
            }
        });
        SetBackground();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(pongOffline != null) {
            PongOffline.b.getP1Paddle().keyPressed(e);
            PongOffline.b.getP2Paddle().keyPressed(e);
        }
        if(pongOnline != null) {
            if(pongOnline.getPlayer().equals("Right"))
                pongOnline.b.getP2Paddle().keyPressed(e);
            if(pongOnline.getPlayer().equals("Left"))
                pongOnline.b.getP1Paddle().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pongOffline != null) {
            PongOffline.b.getP1Paddle().keyReleased(e);
            PongOffline.b.getP2Paddle().keyReleased(e);
        }
        if(pongOnline != null) {
            if(pongOnline.getPlayer().equals("Right"))
                pongOnline.b.getP2Paddle().keyReleased(e);//static
            if(pongOnline.getPlayer().equals("Left"))
                pongOnline.b.getP1Paddle().keyReleased(e);//static
        }
    }
}