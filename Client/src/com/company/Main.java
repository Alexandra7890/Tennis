package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Client cl = new Client(args[0], args[1]);
        Client cl = new Client("localhost", "10000");
    }
}
