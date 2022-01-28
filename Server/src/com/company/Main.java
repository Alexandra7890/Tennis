package com.company;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Server server = new Server(Integer.parseInt(args[0]), "logFile.txt");
        Server server = new Server(9080);
    }
}
